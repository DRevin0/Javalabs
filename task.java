import java.time.LocalDateTime;


public class task {
    private int id;
    private String title;
    private LocalDateTime creationdate;
    private LocalDateTime duedate;
    private String priotity;
    private String status;
    private static int totaltasks = 0;

    public task(String title){
        this.title = title;
        this.creationdate = LocalDateTime.now();
        this.priotity="Бабаба";//Заглушка поменять
        this.status = "Бубубу";//Заглушка поменять
        totaltasks++;

    }
    //Геттеры
    public int getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public LocalDateTime getCreatioDate(){
        return creationdate;
    }
    public LocalDateTime getDueDate(){
        return duedate;
    }
    public String getPriority(){
        return priotity;
    }
    public String getStatus(){
        return status;
    }
    public static int getTotalTasks(){
        return totaltasks;
    }
    //Сеттеры
    public void setId(int id){
        this.id = id;
    }
    public void setTitle(String title){
        if(title != null && !title.trim().isEmpty()){
            this.title = title;
        }
    }
    public void setDueDate(LocalDateTime duedate){
        this.duedate = duedate;
    }
    public void setPriority(String priority){
        this.priotity = priority;
    }
    public void setStatus(String status){
        this.status = status;
    }


    @Override
    public String toString(){
        return "Задача #" +id + ": '" +title + "' [" +status + "]";
    }
}
