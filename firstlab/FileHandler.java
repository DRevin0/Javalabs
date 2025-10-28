import java.io.*;
import java.util.ArrayList;

/**
 * Класс для работы с файловым хранилищем задач.
 * Позволяет сохранять и загружать список задачи в бинарном формате.
 * Использует java serialization для сохранения состояний обьектов.
 * 
 * @author Ревин Дмитрий
 * @version 1.0
 * @see Task
 * @see ObjectOutputStream
 * @see ObjectInputStream
 */
public class FileHandler {
    /**
     * Имя файла для хранения задач в бинарном формате.
     * Формат .dat указывает на использование java serialization
     */
    private static final String TASKS_DATA_FILE = "tasks.dat";


    /**
     * Сохраняет список задач в файл в бинарном формате.
     * Если файл существует, он будет перезаписан.
     * В случае ошибки записи, выводит сообщение в консоль.
     * 
     * @param tasks список задач для сохранения, не должен быть null
     * @throws IOException если ошибка ввода-вывода при записи в файл
     * @see #loadTasks()
     * @see ObjectOutputStream
     */
    public void saveTasks(ArrayList<Task> tasks){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TASKS_DATA_FILE))) {
            oos.writeObject(tasks);
            System.out.println("Задачи сохранены в файл");
        } catch (IOException e) {
            System.out.println("Ошибка сохранения: " +e.getMessage());
        }
    }

    /**
     * Загружает список задач из файла. 
     * Если файл не существует или поврежден, возращает пустой список.
     * Выполняет проверку типа загружаемых данных для безопасностию
     * 
     * 
     * @return список загруженных задач, никогда не возвращет null
     * @throws IOException если произошла ошибка ввода-вывода при чтении файла
     * @throws ClassNotFoundException если класс Task не найден в classpath
     * @see #saveTasks(ArrayList)
     * @see ObjectInputStream
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Task> loadTasks(){
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(TASKS_DATA_FILE))) {
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
