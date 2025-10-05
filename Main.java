import java.time.LocalDateTime;
/**
 * Класс для демонстрации работы программы
 * @author Ревин Дмитрий
 */
public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        
        System.out.println(" ДЕМОНСТРАЦИЯ TO-DO LIST ");

        //Демонстрация добавления задач
        System.out.println("1. ДОБАВЛЕНИЕ ЗАДАЧ");
        Task task1 = new Task("Купить еды");
        Task task2 = new Task("Сделать лабораторную работу");
        Task task3 = new Task("Сходить на пары в вышку");
        Task task4 = new Task("Прочитать книгу");
        
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        manager.addTask(task4);
        
        //Устанавливаем разные приоритеты и статусы
        task1.setPriority(Task.Priority.HIGH);
        task2.setPriority(Task.Priority.MEDIUM);
        task3.setPriority(Task.Priority.LOW);
        
        //Устанавливаем сроки выполнения
        task1.setDueDate(LocalDateTime.now().minusDays(1)); 
        task2.setDueDate(LocalDateTime.now().plusDays(3));  
        task4.setDueDate(LocalDateTime.now().plusHours(2)); 
        System.out.println("Добавлено 4 задачи с разными приоритетами и сроками");

        //Демонстрация получения всех задач
        System.out.println("\n2.ВСЕ ЗАДАЧИ");
        manager.getAllTasks().forEach(task -> 
            System.out.println("   " + task.toString() + " | Приоритет: " + task.getPriority()));

        //Демонстрация поиска по ID
        System.out.println("\n3.ПОИСК ПО ID");
        Task foundTask = manager.getTaskById(task2.getId());
        System.out.println("Найдена задача: " + foundTask);

        //Демонстрация поиска по статусу
        System.out.println("\n4.ПОИСК ПО СТАТУСУ");
        System.out.println("Новые задачи:");
        manager.getTasksByStatus("NEW").forEach(task -> 
            System.out.println("   - " + task.getTitle()));

        //Демонстрация поиска по приоритету
        System.out.println("\n5.ПОИСК ПО ПРИОРИТЕТУ");
        System.out.println("Высокоприоритетные задачи:");
        manager.getTasksByPriority("HIGH").forEach(task -> 
            System.out.println("   - " + task.getTitle()));

        //Демонстрация поиска по названию
        System.out.println("\n6.ПОИСК ПО НАЗВАНИЮ");
        System.out.println("Задачи с 'Сделать лабораторную работу':");
        manager.getTasksByTitle("Сделать лабораторную работу").forEach(task -> 
            System.out.println("   - " + task.getTitle()));

        //Демонстрация просроченных задач
        System.out.println("\n7.ПРОСРОЧЕННЫЕ ЗАДАЧИ");
        System.out.println("Просроченные:");
        manager.getOverdueTasks().forEach(task -> 
            System.out.println("   - " + task.getTitle() + " (срок: " + task.getDueDate() + ")"));

        //Демонстрация сортировки по дате создания
        System.out.println("\n8.СОРТИРОВКА ПО ДАТЕ СОЗДАНИЯ");
        System.out.println("От старых к новым:");
        manager.getTasksSortedByCreationDate().forEach(task -> 
            System.out.println("   - " + task.getTitle() + " | Создана: " + task.getCreationDate()));

        //Демонстрация сортировки по сроку выполнения
        System.out.println("\n9.СОРТИРОВКА ПО СРОКУ ВЫПОЛНЕНИЯ");
        System.out.println("От ближайших к дальним:");
        manager.getTasksSortedByDueDate().forEach(task -> {
            String dueInfo = (task.getDueDate() != null) ? 
                task.getDueDate().toString() : "нет срока";
            System.out.println("   - " + task.getTitle() + " | Срок: " + dueInfo);
        });

        //Демонстрация редактирования задачи
        System.out.println("\n10.РЕДАКТИРОВАНИЕ ЗАДАЧИ");
        boolean updated = manager.updateTask(task1.getId(), Task.Status.IN_PROGRESS, Task.Priority.MEDIUM);
        System.out.println("Обновлена задача '" + task1.getTitle() + "': " + 
            (updated ? "УСПЕХ" : "ОШИБКА"));

        //Демонстрация изменения срока
        System.out.println("\n11.ИЗМЕНЕНИЕ СРОКА ВЫПОЛНЕНИЯ");
        LocalDateTime newDueDate = LocalDateTime.now().plusDays(5);
        boolean dateChanged = manager.changeDueDate(task3.getId(), newDueDate);
        System.out.println("Новый срок для '" + task3.getTitle() + "': " + 
            (dateChanged ? newDueDate : "НЕ УДАЛОСЬ"));

        //Демонстрация удаления задачи
        System.out.println("\n12.УДАЛЕНИЕ ЗАДАЧИ");
        boolean deleted = manager.removeTaskById(task4.getId());
        System.out.println("Удаление задачи '" + task4.getTitle() + "': " + 
            (deleted ? "УСПЕХ" : "ОШИБКА"));

        //Финальное состояние
        System.out.println("\n13.ФИНАЛЬНОЕ СОСТОЯНИЕ");
        System.out.println("Оставшиеся задачи:");
        manager.getAllTasks().forEach(task -> 
            System.out.println("   - " + task.toString() + " | Приоритет: " + task.getPriority()));

        //Сохранение в файл
        System.out.println("\n14.СОХРАНЕНИЕ В ФАЙЛ");
        manager.saveToFile();
        System.out.println("Все задачи сохранены в файл!");

        System.out.println("\n ДЕМОНСТРАЦИЯ ЗАВЕРШЕНА ");
    }
}
