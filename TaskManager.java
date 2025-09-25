import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
public class TaskManager {
    private ArrayList<Task> tasks;
    private int nextId;

    public TaskManager() {
        this.tasks = new ArrayList<>();
        this.nextId = 1;
    }
//Создание задачи
    public void addTask(Task t) {
        t.setId(nextId++); 
        tasks.add(t);
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

    public ArrayList<Task> getTasksByStatus(String status) {
        ArrayList<Task> result = new ArrayList<>();
        for (Task t : tasks) {
            if (t.getStatus().equalsIgnoreCase(status))
                result.add(t);
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

    public ArrayList<Task> getTasksByPriority(String priority) {
        ArrayList<Task> result = new ArrayList<>();
        for (Task t : tasks) {
            if (t.getPriority().equalsIgnoreCase(priority))
                result.add(t);
        }
        return result;
    }

    public ArrayList<Task> getOverdueTasks() {
        ArrayList<Task> result = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (Task t : tasks) {
            if (t.getDueDate() != null && t.getDueDate().isBefore(now) &&
               !"DONE".equalsIgnoreCase(t.getStatus()))
                result.add(t);
        }
        return result;
    }

//Редактирование задачи
    public boolean updateTask(int id, String newStatus, String newPriority) {
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