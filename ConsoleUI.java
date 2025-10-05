import java.util.Scanner;
public class ConsoleUI {
    private final TaskManager manager;
    private final Scanner scanner;
    public ConsoleUI(TaskManager manager){
        this.manager = manager;
        this.scanner = new Scanner(System.in);
    }
    public void run(){
        System.out.println("\n--- TO-DO LIST MANAGER ---");
        runMainLoop();
    }

    private void runMainLoop(){
        while (true) { 
            showMainMenu();
            String choice = scanner.nextLine();
            switch(choice){
                case "1" -> showAllTasks();
                case "2" -> addNewTask();
                case "3" -> showSearchMenu();
                case "4" -> showEditMenu();
                case "5" -> deleteTask();
                case "6" -> showOverdueTasks();
                case "7" -> showStatistics();
                case "8" -> showSortMenu();
                case "0" -> {
                    manager.saveToFile();
                    System.out.println("Сохранено, выход...");
                    return;
                }
                default -> System.out.println("Неверный ввод!");
            }
        }
    }
    private void showAllTasks(){
        System.out.println(manager.getAllTasks());
    }
    private void addNewTask(){
        System.out.println("Введите заголовок задачи: ");
        String title = scanner.nextLine();
        if(title.isEmpty()){
            System.out.println("Ошибка, заголовок не может быть пустым!");
            return;
        }
        Task t = new Task(title);
        manager.addTask(t);
        System.out.println("Задача добавлена! ID: "+ t.getId());

    }
    private void deleteTask(){
        System.out.println("Введите айди задачи, которую Вы хотите удалить: ");
        int id = scanner.nextInt();
        if(manager.removeTaskById(id)){
            System.out.println("Задача удалена! ");
        }
        else{
            System.out.println("Задача не найдена! ");
        }
    }
    private void showOverdueTasks(){
        System.out.println(manager.getOverdueTasks());
    }
    private void searchById(){
        System.out.println("Введите айди задачи: ");
        int id = scanner.nextInt();
        System.out.println(manager.getTaskById(id));
    }
    private void searchByTitle(){
        System.out.println("Введите заголовок задачи: ");
        String title = scanner.nextLine();
        System.out.println(manager.getTasksByTitle(title));
    }
    private void searchByStatus(){
        System.out.println("Введите статус задачи: ");
        String status = scanner.nextLine();
        System.out.println(manager.getTasksByStatus(status));
    }
    private void searchByPriority(){
        System.out.println("Введите приоритет задачи: ");
        String priority = scanner.nextLine();
        System.out.println(manager.getTasksByPriority(priority));
    }


    //методы меню
    private void showMainMenu(){
        System.out.println("\n--- ГЛАВНОЕ МЕНЮ ---");
        System.out.println("1. Показать все задачи");
        System.out.println("2. Добавить новую задачу");
        System.out.println("3. Поиск задач");
        System.out.println("4. Редактикровать задачу");
        System.out.println("5. Удалить задачу");
        System.out.println("6. Просроченные задачи");
        System.out.println("7. Статистика");
        System.out.println("8. Сортировка задач");
        System.out.println("0. Выход и сохранение");
        System.out.print("Выберите действие: ");

    }
    private void showSearchMenu(){
        System.out.println("\n--- ПОИСК ЗАДАЧ ---");
        System.out.println("1. Поиск по id");
        System.out.println("2. Поиск по названию");
        System.out.println("3. Поиск по статусу");
        System.out.println("4. Поиск по приоритету");
        System.out.println("0. Назад в главное меню");
        System.out.print("Выберите действие: ");

        String choice = scanner.nextLine();
    
        switch (choice) {
            case "1" -> searchById();
            case "2" -> searchByTitle();
            case "3" -> searchByStatus();
            case "4" -> searchByPriority();
            case "0" -> { return; } 
            default -> {
                System.out.println("Неверный ввод!");
                showSearchMenu(); 
            }
        }
    }
    private void showEditMenu(){
        System.out.println("\n--- РЕДАКТИКРОВАНИЕ ЗАДАЧИ ---");
        System.out.println("1. Изменить статус и приоритет");
        System.out.println("2. Изменить срок выполнения");
        System.out.println("0. Назад в главное меню");
        System.out.print("Выберите действие: ");
    }
    private void showSortMenu(){
        System.out.println("\n--- СОРТИРОВКА ЗАДАЧ ---");
        System.out.println("1. По дате создания");
        System.out.println("2. По сроку выполнения");
        System.out.println("0. Назад в главное меню");
        System.out.print("Выберите действие: ");
    }
    private void showStatistics(){
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
/*

        System.out.println("2.");
        System.out.println("3.");
        System.out.println("4.");
        System.out.println("5.");
        System.out.println("6.");
        System.out.println("7. ");
        System.out.println("8. ");
        System.out.println("0. ");*/