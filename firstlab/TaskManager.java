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
        this.nextId = tasks.size() + 1;
    }
    
    /**
     * Добавляет новую задачу в менеджер.
     * 
     * @param task задача для добавления
     */
    public void addTask(Task task) {
        task.setId(nextId++); 
        tasks.add(task);
    }
    
    /**
     * Выполняет ручное сохранение всех задач в файл.
     */
    public void saveToFile() {
        fileHandler.saveTasks(tasks);
    }
    
    /**
     * Возвращает задачу по идентификатору.
     * 
     * @param id идентификатор задачи
     * @return задача или null если не найдена
     */
    public Task getTaskById(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    /**
     * Возвращает список всех задач.
     * 
     * @return список всех задач
     */
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }
    
    /**
     * Возвращает задачи по статусу.
     * 
     * @param statusString строковое представление статуса
     * @return список задач с указанным статусом
     */
    public ArrayList<Task> getTasksByStatus(String statusString) {
        ArrayList<Task> result = new ArrayList<>();
        try {
            Task.Status status = Task.Status.valueOf(statusString.toUpperCase());
            for (Task task : tasks) {
                if (task.getStatus() == status) {
                    result.add(task);
                }
            }
        } catch (IllegalArgumentException exception) {
            System.err.println("Задача с таким статусом не найдена: " + statusString);
        }
        return result;
    }

    /**
     * Возвращает задачи по заголовку.
     * 
     * @param title заголовок для поиска
     * @return список задач с указанным заголовком
     */
    public ArrayList<Task> getTasksByTitle(String title) {
        if (title == null) {
            return new ArrayList<>();
        }
        ArrayList<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getTitle().equalsIgnoreCase(title)) {
                result.add(task);
            }
        }
        return result;
    }

    /**
     * Возвращает задачи по приоритету.
     * 
     * @param priorityString строковое представление приоритета
     * @return список задач с указанным приоритетом
     */
    public ArrayList<Task> getTasksByPriority(String priorityString) {
        ArrayList<Task> result = new ArrayList<>();
        try {
            Task.Priority priority = Task.Priority.valueOf(priorityString.toUpperCase());
            for (Task task : tasks) {
                if (task.getPriority() == priority) {
                    result.add(task);
                }
            }
        } catch (IllegalArgumentException exception) {
            System.err.println("Задача с таким приоритетом не найдена: " + priorityString);
        }
        return result;
    }
    
    /**
     * Возвращает просроченные задачи (срок прошел, но статус не COMPLETED).
     * 
     * @return список просроченных задач
     */
    public ArrayList<Task> getOverdueTasks() {
        ArrayList<Task> result = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (Task task : tasks) {
            if (task.getDueDate() != null 
                    && task.getDueDate().isBefore(now)
                    && task.getStatus() != Task.Status.COMPLETED) {
                result.add(task);
            }
        }
        return result;
    }

    /**
     * Обновляет статус и приоритет задачи.
     * 
     * @param id идентификатор задачи
     * @param newStatus новый статус
     * @param newPriority новый приоритет
     * @return true если обновление успешно, false если задача не найдена
     */
    public boolean updateTask(int id, Task.Status newStatus, Task.Priority newPriority) {
        Task task = getTaskById(id);
        if (task != null) {
            task.setStatus(newStatus);
            task.setPriority(newPriority);
            return true;
        }
        return false;
    }

    /**
     * Изменяет срок выполнения задачи.
     * 
     * @param id идентификатор задачи
     * @param newDueDate новый срок выполнения
     * @return true если обновление успешно, false если задача не найдена
     */
    public boolean changeDueDate(int id, LocalDateTime newDueDate) {
        Task task = getTaskById(id);
        if (task != null) {
            task.setDueDate(newDueDate);
            return true;
        }
        return false;
    }

    /**
     * Возвращает задачи, отсортированные по дате создания.
     * 
     * @return отсортированный список задач
     */
    public ArrayList<Task> getTasksSortedByCreationDate() {
        ArrayList<Task> sorted = new ArrayList<>(tasks);
        sorted.sort(Comparator.comparing(Task::getCreationDate));
        return sorted;
    }

    /**
     * Возвращает задачи, отсортированные по сроку выполнения.
     * 
     * @return отсортированный список задач
     */
    public ArrayList<Task> getTasksSortedByDueDate() {
        ArrayList<Task> sorted = new ArrayList<>(tasks);
        sorted.sort(Comparator.comparing(Task::getDueDate,
                Comparator.nullsLast(Comparator.naturalOrder())));
        return sorted;
    }

    /**
     * Удаляет задачу по идентификатору.
     * 
     * @param id идентификатор задачи для удаления
     * @return true если удаление успешно, false если задача не найдена
     */
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

