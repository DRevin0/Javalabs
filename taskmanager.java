import java.util.ArrayList;
public class taskmanager{
    private ArrayList<task> tasks;
    private int nextId;

    public taskmanager(){
        this.tasks = new ArrayList<task>();
        this.nextId = 1;
    } 
    public void addTask(task t){
        t.setId(nextId);
        nextId++;
        tasks.add(t);
    }

    public task getTaskById(int id){
        for(task t : tasks){
            if(t.getId()==id){
                return t;
            }
        }
        return null;
    }
    public boolean delTaskById(int id){
        for(int i = 0;i<tasks.size();i++){
            if(tasks.get(i).getId()==id){
                tasks.remove(i);//можно удалять по обьекту, но это медленее
                return true;
            }
        }
        return false;
    }
    public ArrayList<task> getAllTasks(){
        return tasks;
    }
//Добавить методы для редактирования задачи, смены приоритетов, сортировки по датам, поиска по атрибутам(а не только по id) и метод чтобы указывать срок выполнения
}