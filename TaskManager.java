import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
/**
 * Основной класс для управления задачами.
 * Обеспечивает создание, поиск, редактирование, удаление и сортировку задач.
 * Автоматически загружает задачи из файла при создании и предоставляет 
 * методы для ручного сохранения изменений.
 * 
 * @author Ревин Дмитрий
 * @version 1.0
 * @see Task
 * @see FileHandler
 */
public class TaskManager {
    private final ArrayList<Task> tasks;
    private final FileHandler fileHandler;
    private int nextId;
    /**
     * Создает новый менеджер задач.
     * Автоматически загружает задачи из файла и инициализирует счетчик ID.
     */
    public TaskManager() {
        this.fileHandler = new FileHandler();
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
        if(title == null) return new ArrayList<>();
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
    /**
     * Возвращает просроченные задачи(срок прошел, но статус не completed)
     */
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
        sorted.sort(Comparator.comparing(Task::getDueDate,
            Comparator.nullsLast(Comparator.naturalOrder())));
        return sorted;
    }

//Удаление задачи 
    public boolean removeTaskById(int id) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == id) {
                tasks.remove(i); 
                return true;
            }
        }
        return false;
    }
}