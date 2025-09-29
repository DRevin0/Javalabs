import java.io.Serializable;
import java.time.LocalDateTime;
public class Task implements Serializable {
    private int id;
    private String title;
    private LocalDateTime creationDate;
    private LocalDateTime dueDate;
    private Priority priority;
    private Status status;

    public enum Priority {
        LOW,
        MEDIUM,
        HIGH
    }
    public enum Status {
        NEW,
        IN_PROGRESS,
        COMPLETED
    }



    // Конструктор задачи с минимальным набором полей
    public Task(String title) {
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
}
