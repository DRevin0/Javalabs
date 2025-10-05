
/**
 * Класс для демонстрации работы программы
 * @author Ревин Дмитрий
 */
public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        ConsoleUI ui = new ConsoleUI(manager);
        ui.run();
        
    }
}
