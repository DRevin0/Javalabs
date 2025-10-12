
/**
 * Класс для запуска программы
 * 
 * @author Ревин Дмитрий
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        ConsoleUI ui = new ConsoleUI(manager);
        ui.run();
    }
}

