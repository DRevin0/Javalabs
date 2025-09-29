import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
public class TaskManager {
    private ArrayList<Task> tasks;
    private Filehandler fileHandler;
    private int nextId;

    public TaskManager() {
        this.fileHandler = new Filehandler();
        this.tasks = fileHandler.loadTasks();
        this.nextId = tasks.size()+1;
    }
//Создание задачи
    public void addTask(Task t) {
        t.setId(nextId++); 
        tasks.add(t);
    }
//Ручное сохранение всех задач в файл
public void saveToFile(){
    fileHandler.saveTasks(tasks);
}
//Методы поиска задачи
    public Task getTaskById(int id) {
        for (Task t : tasks) {
            if (t.getId() == id)
                return t;
        }
        return null;
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public ArrayList<Task> getTasksByStatus(String statusStr) {
        ArrayList<Task> result = new ArrayList<>();
        try {
            Task.Status status = Task.Status.valueOf(statusStr.toUpperCase());
            for (Task t : tasks) {
                if (t.getStatus() == status){
                    result.add(t);
                }
            }
        } catch(IllegalArgumentException e){
            System.err.println("Задача с таким статусом не найдена: "+statusStr);
        }
        return result;
    }

    public ArrayList<Task> getTasksByTitle(String title) {
        ArrayList<Task> result = new ArrayList<>();
        for (Task t : tasks) {
            if (t.getTitle().equalsIgnoreCase(title))
                result.add(t);
        }
        return result;
    }

    public ArrayList<Task> getTasksByPriority(String priorityStr) {
        ArrayList<Task> result = new ArrayList<>();
        try {
            Task.Priority priority = Task.Priority.valueOf(priorityStr.toUpperCase());
            for (Task t : tasks) {
                if (t.getPriority() == priority){
                    result.add(t);
                }
            }
        } catch(IllegalArgumentException e){
            System.err.println("Задача с таким приоритетом не найдена: "+priorityStr);
        }
        return result;
    }

    public ArrayList<Task> getOverdueTasks() {
        ArrayList<Task> result = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (Task t : tasks) {
            if (t.getDueDate() != null && t.getDueDate().isBefore(now) &&
               t.getStatus() != Task.Status.COMPLETED)
                result.add(t);
        }
        return result;
    }

//Редактирование задачи
    public boolean updateTask(int id, Task.Status newStatus, Task.Priority newPriority) {
        Task t = getTaskById(id);
        if (t != null) {
            t.setStatus(newStatus);
            t.setPriority(newPriority);
            return true;
        }
        return false;
    }

    public boolean changeDueDate(int id, LocalDateTime newDueDate) {
        Task t = getTaskById(id);
        if (t != null) {
            t.setDueDate(newDueDate);
            return true;
        }
        return false;
    }

//Сортировки задач
    public ArrayList<Task> getTasksSortedByCreationDate() {
        ArrayList<Task> sorted = new ArrayList<>(tasks);
        sorted.sort(Comparator.comparing(Task::getCreationDate));
        return sorted;
    }


    public ArrayList<Task> getTasksSortedByDueDate() {
        ArrayList<Task> sorted = new ArrayList<>(tasks);
        sorted.sort(Comparator.comparing(Task::getDueDate));
        return sorted;
    }

//Удаление задачи 
    public boolean removeTaskById(int id) {
        Task toRemove = getTaskById(id);
        if (toRemove != null) {
            tasks.remove(toRemove);
            return true;
        }
        return false;
    }
}