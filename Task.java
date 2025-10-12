import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Класс представляет задачу в системе управления задачами (TO-DO LIST).
 * Содержит основную информацию о задаче: идентификатор, заголовок, описание,
 * даты создания и выполнения, приоритет и статус.
 * 
 * @author Ревин Дмитрий 
 * @version 1.0
 * @since 2025
 */
public class Task implements Serializable {

    /** Уникальный идентификатор задачи. Генерируется автоматически при создании. */
    private int id;
    
    /** Краткое название задачи. Не может быть null или пустым. */
    private String title;

    /** Дата создания (автоматически). */
    private final LocalDateTime creationDate;
    
    /** Срок выполнения (опционально). */
    private LocalDateTime dueDate;
    
    /** Уровень важности. */
    private Priority priority;
    
    /** Текущий статус. */
    private Status status;
    
    /**
     * Создает новую задачу с указанным заголовком.
     * Автоматически устанавливает дату создания, приоритет "LOW" и статус "NEW".
     * 
     * @param title заголовок задачи, не может быть null или пустым
     * @throws IllegalArgumentException если заголовок null или пустой
     */
    public Task(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Заголовок не может быть null или пустым");
        }
        this.title = title;
        this.creationDate = LocalDateTime.now();
        this.priority = Priority.LOW; 
        this.status = Status.NEW;
    }

    // Геттеры
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public Status getStatus() {
        return status;
    }

    // Сеттеры
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        if (title != null && !title.isBlank()) {
            this.title = title;
        }
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Задача №" + id + ": '" + title + "', статус: [" + status + "]";
    }
    
    /** Набор вариантов приоритетов задачи. */
    public enum Priority {
        LOW,
        MEDIUM,
        HIGH
    }
    
    /** Набор вариантов статусов задачи. */
    public enum Status {
        NEW,
        IN_PROGRESS,
        COMPLETED
    }
}

