import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Класс для взаимодействия с пользователем через консоль.
 * Реализует пользовательский интерфейс в виде меню.
 * 
 * @author Ревин Дмитрий
 * @version 1.0
 */
public class ConsoleUI {
    
    private final TaskManager manager;
    private final Scanner scanner;
    
    /**
     * Создает новый пользовательский интерфейс.
     * 
     * @param manager менеджер задач для управления данными
     */
    public ConsoleUI(TaskManager manager) {
        this.manager = manager;
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Запускает главный цикл приложения.
     */
    public void run() {
        System.out.println("\n--- TO-DO LIST MANAGER ---");
        runMainLoop();
    }
    
    private int readIntChoice() {
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                return choice;
            } catch (NumberFormatException exception) {
                System.out.println("Ошибка: введите число!");
            }
        }
    }

    private void runMainLoop() {
        while (true) {
            showMainMenu();
            int choice = readIntChoice();
            switch (choice) {
                case 1 -> showAllTasks();
                case 2 -> addNewTask();
                case 3 -> showSearchMenu();
                case 4 -> showEditMenu();
                case 5 -> deleteTask();
                case 6 -> showOverdueTasks();
                case 7 -> showStatistics();
                case 8 -> showSortMenu();
                case 0 -> {
                    manager.saveToFile();
                    System.out.println("Сохранено, выход...");
                    return;
                }
                default -> System.out.println("Неверный ввод!");
            }
        }
    }
    
    private void showAllTasks() {
        ArrayList<Task> tasks = manager.getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("Нет задач");
            return;
        }
        System.out.println("\n--- ВСЕ ЗАДАЧИ (" + tasks.size() + ") ---");
        tasks.forEach(task -> System.out.println(" - " + task));
    }
    
    private void addNewTask() {
        System.out.println("Введите заголовок задачи: ");
        String title = scanner.nextLine();
        if (title.isEmpty()) {
            System.out.println("Ошибка, заголовок не может быть пустым!");
            return;
        }
        Task task = new Task(title);
        manager.addTask(task);
        System.out.println("Задача добавлена! ID: " + task.getId());
    }
    
    private void deleteTask() {
        System.out.println("Введите ID задачи, которую Вы хотите удалить: ");
        int id = readIntChoice();
        if (manager.removeTaskById(id)) {
            System.out.println("Задача удалена!");
        } else {
            System.out.println("Задача не найдена!");
        }
    }
    
    private void showOverdueTasks() {
        ArrayList<Task> overdueTasks = manager.getOverdueTasks();
        if (overdueTasks.isEmpty()) {
            System.out.println("Нет просроченных задач!");
            return;
        }
        System.out.println("\n--- ПРОСРОЧЕННЫЕ ЗАДАЧИ(" + overdueTasks.size() + ") ---");
        overdueTasks.forEach(task -> System.out.println(" - " + task));
    }
    
    private void searchById() {
        System.out.println("Введите ID задачи: ");
        int id = readIntChoice();
        Task task = manager.getTaskById(id);
        if (task == null) {
            System.out.println("Задача не найдена");
        } else {
            System.out.println(task);
        }
    }
    
    private void searchByTitle() {
        System.out.println("Введите заголовок задачи: ");
        String title = scanner.nextLine();
        ArrayList<Task> tasks = manager.getTasksByTitle(title);
        if (tasks.isEmpty()) {
            System.out.println("Задачи не найдены");
        } else {
            tasks.forEach(System.out::println);
        }
    }
    
    private void searchByStatus() {
        System.out.println("\n--- ВЫБОР СТАТУСА ---");
        System.out.println("1. Новая (NEW)");
        System.out.println("2. В процессе (IN_PROGRESS)");
        System.out.println("3. Завершена (COMPLETED)");
        System.out.println("0. Вернуться в главное меню");
        System.out.print("Выберите статус: ");
        
        int statusChoice = readIntChoice();
        String newStatus = switch (statusChoice) {
            case 1 -> "NEW";
            case 2 -> "IN_PROGRESS";
            case 3 -> "COMPLETED";
            case 0 -> null;
            default -> {
                System.out.println("Неверный ввод!");
                yield null;
            }
        };
        
        if (newStatus == null) {
            return;
        }
        
        ArrayList<Task> tasks = manager.getTasksByStatus(newStatus);
        if (tasks.isEmpty()) {
            System.out.println("Задачи не найдены");
        } else {
            tasks.forEach(System.out::println);
        }
    }
    
    private void searchByPriority() {
        System.out.println("\n--- ВЫБОР ПРИОРИТЕТА ---");
        System.out.println("1. Высокий (HIGH)");
        System.out.println("2. Средний (MEDIUM)");
        System.out.println("3. Низкий (LOW)");
        System.out.println("0. Вернуться в главное меню");
        System.out.print("Выберите приоритет: ");
        
        int priorityChoice = readIntChoice();
        String newPriority = switch (priorityChoice) {
            case 1 -> "HIGH";
            case 2 -> "MEDIUM";
            case 3 -> "LOW";
            case 0 -> null;
            default -> {
                System.out.println("Неверный ввод!");
                yield null;
            }
        };
        
        if (newPriority == null) {
            return;
        }
        
        ArrayList<Task> tasks = manager.getTasksByPriority(newPriority);
        if (tasks.isEmpty()) {
            System.out.println("Задачи не найдены");
        } else {
            tasks.forEach(System.out::println);
        }
    }
    
    private void changeStatusAndPriority() {
        System.out.print("Введите ID задачи для редактирования: ");
        try {
            int id = readIntChoice();
            Task task = manager.getTaskById(id);
            
            if (task == null) {
                System.out.println("Задача с ID " + id + " не найдена");
                return;
            }
            
            System.out.println("Текущая задача: " + task);
            Task.Status currentStatus = task.getStatus();
            Task.Priority currentPriority = task.getPriority();
            
            System.out.println("\n--- ВЫБОР СТАТУСА ---");
            System.out.println("1. Новая (NEW)");
            System.out.println("2. В процессе (IN_PROGRESS)");
            System.out.println("3. Завершена (COMPLETED)");
            System.out.println("0. Оставить текущее (" + currentStatus + ")");
            System.out.print("Выберите статус: ");
            
            int statusChoice = readIntChoice();
            Task.Status newStatus = switch (statusChoice) {
                case 1 -> Task.Status.NEW;
                case 2 -> Task.Status.IN_PROGRESS;
                case 3 -> Task.Status.COMPLETED;
                case 0 -> currentStatus;
                default -> throw new IllegalArgumentException("Неверный выбор статуса: " + statusChoice);
            };
            
            System.out.println("\n--- ВЫБОР ПРИОРИТЕТА ---");
            System.out.println("1. Высокий (HIGH)");
            System.out.println("2. Средний (MEDIUM)");
            System.out.println("3. Низкий (LOW)");
            System.out.println("0. Оставить текущее (" + currentPriority + ")");
            System.out.print("Выберите приоритет: ");
            
            int priorityChoice = readIntChoice();
            Task.Priority newPriority = switch (priorityChoice) {
                case 1 -> Task.Priority.HIGH;
                case 2 -> Task.Priority.MEDIUM;
                case 3 -> Task.Priority.LOW;
                case 0 -> currentPriority;
                default -> throw new IllegalArgumentException("Неверный выбор приоритета: " + priorityChoice);
            };
            
            if (manager.updateTask(id, newStatus, newPriority)) {
                System.out.println("Задача успешно обновлена!");
            } else {
                System.out.println("Ошибка при обновлении задачи");
            }
            
        } catch (NumberFormatException exception) {
            System.out.println("Ошибка: введите корректный ID (число)!");
        } catch (IllegalArgumentException exception) {
            System.out.println("Ошибка: " + exception.getMessage());
        }
    }
    
    private void changeDueDate() {
        System.out.print("Введите ID задачи для редактирования: ");
        try {
            int id = readIntChoice();
            Task task = manager.getTaskById(id);
            
            if (task == null) {
                System.out.println("Задача с ID " + id + " не найдена");
                return;
            }

            System.out.println("Текущая задача: " + task);
            System.out.println("\nВведите новую дату выполнения: ");
            System.out.print("Год (например 2025): ");
            int year = readIntChoice();
            System.out.print("Месяц (1-12): ");
            int month = readIntChoice();
            System.out.print("День (1-31): ");
            int day = readIntChoice();
            System.out.print("Час (0-23): ");
            int hour = readIntChoice();
            System.out.print("Минута (0-59): ");
            int minute = readIntChoice();
            
            LocalDateTime dueDate = LocalDateTime.of(year, month, day, hour, minute);

            if (manager.changeDueDate(id, dueDate)) {
                System.out.println("Срок выполнения обновлен: " + dueDate);
            }
            
        } catch (NumberFormatException exception) {
            System.out.println("Ошибка: введите корректный ID (число)!");
        } catch (DateTimeException exception) {
            System.out.println("Ошибка: некорректная дата! " + exception.getMessage());
        }
    }
    
    private void sortByStartDate() {
        ArrayList<Task> sortedTasks = manager.getTasksSortedByCreationDate();
        sortedTasks.forEach(task -> System.out.println(" - " + task));
    }
    
    private void sortByDueDate() {
        ArrayList<Task> sortedTasks = manager.getTasksSortedByDueDate();
        sortedTasks.forEach(task -> {
            if (task.getDueDate() != null) {
                System.out.println(" - " + task + " |Срок: " + task.getDueDate());
            } else {
                System.out.println(" - " + task + " |Без срока");
            }
        });
    }
    
    // Методы меню
    private void showMainMenu() {
        System.out.println("\n--- ГЛАВНОЕ МЕНЮ ---");
        System.out.println("1. Показать все задачи");
        System.out.println("2. Добавить новую задачу");
        System.out.println("3. Поиск задач");
        System.out.println("4. Редактировать задачу");
        System.out.println("5. Удалить задачу");
        System.out.println("6. Просроченные задачи");
        System.out.println("7. Статистика");
        System.out.println("8. Сортировка задач");
        System.out.println("0. Выход и сохранение");
        System.out.print("Выберите действие: ");
    }
    
    private void showSearchMenu() {
        System.out.println("\n--- ПОИСК ЗАДАЧ ---");
        System.out.println("1. Поиск по ID");
        System.out.println("2. Поиск по названию");
        System.out.println("3. Поиск по статусу");
        System.out.println("4. Поиск по приоритету");
        System.out.println("0. Назад в главное меню");
        System.out.print("Выберите действие: ");

        int choice = readIntChoice();
        
        switch (choice) {
            case 1 -> searchById();
            case 2 -> searchByTitle();
            case 3 -> searchByStatus();
            case 4 -> searchByPriority();
            case 0 -> { break; }
            default -> {
                System.out.println("Неверный ввод!");
                showSearchMenu();
            }
        }
    }
    
    private void showEditMenu() {
        System.out.println("\n--- РЕДАКТИРОВАНИЕ ЗАДАЧИ ---");
        System.out.println("1. Изменить статус и приоритет");
        System.out.println("2. Изменить срок выполнения");
        System.out.println("0. Назад в главное меню");
        System.out.print("Выберите действие: ");

        int choice = readIntChoice();
        switch (choice) {
            case 1 -> changeStatusAndPriority();
            case 2 -> changeDueDate();
            case 0 -> { break; }
            default -> {
                System.out.println("Неверный ввод!");
                showEditMenu();
            }
        }
    }
    
    private void showSortMenu() {
        System.out.println("\n--- СОРТИРОВКА ЗАДАЧ ---");
        System.out.println("1. По дате создания");
        System.out.println("2. По сроку выполнения");
        System.out.println("0. Назад в главное меню");
        System.out.print("Выберите действие: ");

        int choice = readIntChoice();
        switch (choice) {
            case 1 -> sortByStartDate();
            case 2 -> sortByDueDate();
            case 0 -> { break; }
            default -> {
                System.out.println("Неверный ввод!");
                showSortMenu();
            }
        }
    }

    private void showStatistics() {
        System.out.println("\n--- СТАТИСТИКА ---");
        System.out.println("Общее количество задач: " + manager.getAllTasks().size());
        System.out.println("Выполненные задачи: " + manager.getTasksByStatus("COMPLETED").size());
        System.out.println("Задачи в процессе: " + manager.getTasksByStatus("IN_PROGRESS").size());
        System.out.println("Новые задачи: " + manager.getTasksByStatus("NEW").size());
        System.out.println("Просроченные задачи: " + manager.getOverdueTasks().size());
        System.out.println("Высокий приоритет: " + manager.getTasksByPriority("HIGH").size());
        System.out.println("Средний приоритет: " + manager.getTasksByPriority("MEDIUM").size());
        System.out.println("Низкий приоритет: " + manager.getTasksByPriority("LOW").size());
    }
}


