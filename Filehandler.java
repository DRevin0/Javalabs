import java.io.*;
import java.util.ArrayList;


public class Filehandler {
    private static final String FILE = "tasks.dat";


//Сохранение всех задач в файл
    public void saveTasks(ArrayList<Task> tasks){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE))) {
            oos.writeObject(tasks);
            System.out.println("Задачи сохранены в файл");
        } catch (IOException e) {
            System.out.println("Ошибка сохранения: " +e.getMessage());
        }
    }

//Загрузка всех задач с файла
    public ArrayList<Task> loadTasks(){
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE))) {
            Object obj = ois.readObject();
            if(obj instanceof  ArrayList<?>){
                return(ArrayList<Task>) obj;
            }
            else{
                throw new IOException("Неправильный тип обьекта в файле");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден, начнем с путого списка");
            return new ArrayList<>();
        } catch(IOException | ClassNotFoundException e){
            System.out.println("Ошибка загрузки, начнем с пустого списка: "+e.getMessage());
            return new ArrayList<>();
        }
    }

}
