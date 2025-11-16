import java.util.Scanner;
import collections.*;
import structures.*;
import collections.customhashmap.*;
import algorithms.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Graph<String> graph = null;
    public static void main(String[] args) {
        System.out.println("Интерактивный граф");
        createGraph();

        while (true) {
            showMenu();
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (!handleChoice(choice)) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
                scanner.nextLine();
                System.out.println("Нажмите Enter для продолжения...");
                scanner.nextLine();
            }
        }
        System.out.println("Работа завершена");
        scanner.close();
    }

    private static void createGraph() {
        System.out.println("Создание графа:");
        System.out.print("Ориентированный? (да/нет): ");
        String input = scanner.nextLine().trim().toLowerCase();
        boolean isDirected = input.equals("да") || input.equals("y") || input.equals("1");
        graph = new Graph<>(isDirected);
        System.out.println("Граф создан: " + (isDirected ? "ориентированный" : "неориентированный"));
    }

    private static void showMenu() {
        System.out.println("\n--- Меню ---");
        System.out.println("1. Добавить вершину");
        System.out.println("2. Добавить ребро");
        System.out.println("3. Удалить вершину");
        System.out.println("4. Удалить ребро");
        System.out.println("5. Показать соседей вершины");
        System.out.println("6. Показать все вершины");
        System.out.println("7. Выполнить DFS");
        System.out.println("8. Выполнить BFS");
        System.out.println("9. Выполнить Дейкстру");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private static boolean handleChoice(int choice) {
        switch (choice) {
            case 1: addVertex(); break;
            case 2: addEdge(); break;
            case 3: removeVertex(); break;
            case 4: removeEdge(); break;
            case 5: showNeighbors(); break;
            case 6: showAllVertices(); break;
            case 7: runDFS(); break;
            case 8: runBFS(); break;
            case 9: runDijkstra(); break;
            case 0: return false;
            default: System.out.println("Неверный выбор");
        }
        System.out.println("Нажмите Enter для продолжения...");
        scanner.nextLine();
        return true;
    }

    private static void addVertex() {
        System.out.print("Введите имя вершины: ");
        String v = scanner.nextLine().trim();
        if (v.isEmpty()) {
            System.out.println("Имя не может быть пустым");
            return;
        }
        graph.addVertex(v);
        System.out.println("Вершина добавлена: " + v);
    }

    private static void addEdge() {
        System.out.print("Введите начальную вершину: ");
        String from = scanner.nextLine().trim();
        System.out.print("Введите конечную вершину: ");
        String to = scanner.nextLine().trim();
        System.out.print("Введите вес ребра: ");
        try {
            int weight = Integer.parseInt(scanner.nextLine().trim());
            graph.addEdge(from, to, weight);
            System.out.println("Ребро добавлено: " + from + " -> " + to + " (вес " + weight + ")");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: вес должен быть целым числом");
        }
    }

    private static void removeVertex() {
        System.out.print("Введите имя вершины для удаления: ");
        String v = scanner.nextLine().trim();
        graph.removeVertex(v);
        System.out.println("Вершина удалена (если существовала): " + v);
    }

    private static void removeEdge() {
        System.out.print("Введите начальную вершину: ");
        String from = scanner.nextLine().trim();
        System.out.print("Введите конечную вершину: ");
        String to = scanner.nextLine().trim();
        graph.removeEdge(from, to);
        System.out.println("Ребро удалено (если существовало): " + from + " -> " + to);
    }

    private static void showNeighbors() {
        System.out.print("Введите вершину: ");
        String v = scanner.nextLine().trim();
        try {
            DynamicArray<String> neighbors = graph.getAdjacent(v);
            System.out.println("Соседи " + v + ": " + toString(neighbors));
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void showAllVertices() {
        try {
            DynamicArray<String> vertices = graph.getAllVertices();
            System.out.println("Все вершины: " + toString(vertices));
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void runDFS() {
        System.out.print("Введите стартовую вершину: ");
        String start = scanner.nextLine().trim();
        try {
            DynamicArray<String> result = GraphAlgorithms.dfs(graph, start);
            System.out.println("DFS (" + start + "): " + toString(result));
        } catch (Exception e) {
            System.out.println("Ошибка при выполнении DFS: " + e.getMessage());
        }
    }

    private static void runBFS() {
        System.out.print("Введите стартовую вершину: ");
        String start = scanner.nextLine().trim();
        try {
            DynamicArray<String> result = GraphAlgorithms.bfs(graph, start);
            System.out.println("BFS (" + start + "): " + toString(result));
        } catch (Exception e) {
            System.out.println("Ошибка при выполнении BFS: " + e.getMessage());
        }
    }

    private static void runDijkstra() {
        System.out.print("Введите стартовую вершину: ");
        String start = scanner.nextLine().trim();
        try {
            CustomHashMap<String, Integer> dist = GraphAlgorithms.dijkstra(graph, start);
            System.out.println("Кратчайшие расстояния от " + start + ":");
            DynamicArray<String> keys = dist.keySet();
            for (int i = 0; i < keys.getSize(); i++) {
                String v = keys.get(i);
                System.out.println("  " + v + ": " + dist.get(v));
            }
        } catch (Exception e) {
            System.out.println("Ошибка при выполнении Дейкстры: " + e.getMessage());
            System.out.println("Убедитесь, что граф ориентированный и все рёбра добавлены корректно.");
        }
    }

    private static <T> String toString(DynamicArray<T> list) {
        if (list == null) return "null";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < list.getSize(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(list.get(i));
        }
        sb.append("]");
        return sb.toString();
    }
}

