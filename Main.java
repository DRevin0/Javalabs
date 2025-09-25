public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        // Создание нескольких задач
        Task task1 = new Task("Купить продукты");
        Task task2 = new Task("Посмотреть фильм");
        Task task3 = new Task("Позвонить маме");

        // Добавляем задачи в менеджер
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);

        System.out.println("\nВсе задачи:");
        for (Task t : manager.getAllTasks())
            System.out.println(t.toString());

        // Обновляем статус одной задачи
        manager.updateTask(task1.getId(), "IN_PROGRESS", "HIGH");

        System.out.println("\nОбновленные задачи:");
        for (Task t : manager.getAllTasks())
            System.out.println(t.toString());

        // Проверяем получение задачи по ID
        Task foundTask = manager.getTaskById(task2.getId());
        System.out.println("\nПолученная задача по ID: " + foundTask.toString());

        // Сортируем задачи по дате создания
        System.out.println("\nЗадачи, отсортированные по дате создания:");
        for (Task t : manager.getTasksSortedByCreationDate())
            System.out.println(t.toString());
    }
}
