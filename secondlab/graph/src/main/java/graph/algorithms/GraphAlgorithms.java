package algorithms;
import structures.Graph;
import collections.*;
import collections.customhashmap.CustomHashMap;
import exceptions.*;

public class GraphAlgorithms {

    public static <V> DynamicArray<V> dfs(Graph<V> graph, V start){
        if(start == null){
            throw new InvalidVertexException("Start vertex cannot be null");
        }
        
        CustomHashMap<V, Boolean> visited = new CustomHashMap<>();
        DynamicArray<V> stack = new DynamicArray<>(10);
        DynamicArray<V> result = new DynamicArray<>(10);

        stack.add(start);

        while (!stack.isEmpty()) {
            V v = stack.get(stack.getSize() - 1);
            stack.remove(stack.getSize() - 1);

            if (!visited.containsKey(v)) {
                visited.put(v, true);
                result.add(v);

                DynamicArray<V> neighbors = graph.getAdjacent(v);
                for (int i = neighbors.getSize() - 1; i >= 0; i--) {
                    V u = neighbors.get(i);
                    if (!visited.containsKey(u)) {
                        stack.add(u);
                    }
                }
            }
        }

        return result;
    }


    public static <V> DynamicArray<V> bfs(Graph<V> graph, V start){
        if(start == null){
            throw new InvalidVertexException("Start vertex cannot be null");
        }

        CustomHashMap<V, Boolean> visited = new CustomHashMap<>();
        Queue<V> q = new Queue<>();
        DynamicArray<V> result = new DynamicArray<>(10);
        visited.put(start, true);
        q.enqueue(start);
        while(!q.isEmpty()){
            V v = q.dequeue();
            result.add(v);

            DynamicArray<V> neighbors = graph.getAdjacent(v);
            for (int i = 0; i < neighbors.getSize(); i++) {
                V u = neighbors.get(i);
                if (!visited.containsKey(u)) {
                    visited.put(u, true);
                    q.enqueue(u);
                }
            }
        }
        return result;

    }
    public static <V> CustomHashMap<V, Integer> dijkstra(Graph<V> graph, V start){
        if(start == null){
            throw new InvalidVertexException("Start vertex cannot be null");
        }
        CustomHashMap<V, Integer> d = new CustomHashMap<>();
        CustomHashMap<V, Boolean> S = new CustomHashMap<>();
        DynamicArray<V> allVertices = graph.getAllVertices();
        for (int i = 0; i < allVertices.getSize(); i++) {
            V v = allVertices.get(i);
            d.put(v, Integer.MAX_VALUE);
        }
        d.put(start, 0);

        for (int i = 0; i < allVertices.getSize(); i++) {
            V u = null;
            int minDist = Integer.MAX_VALUE;
            for (int j = 0; j < allVertices.getSize(); j++) {
                V v = allVertices.get(j);
                if (!S.containsKey(v)) {
                    int dist = d.get(v);
                    if (dist < minDist) {
                        minDist = dist;
                        u = v;
                    }
                }
            }
            if (u == null || minDist == Integer.MAX_VALUE) {
            break; 
            }
            S.put(u, true);
            DynamicArray<V> neighbors = graph.getAdjacent(u);
            for (int j = 0; j < neighbors.getSize(); j++) {
                V v = neighbors.get(j);
                int weight = graph.getEdgeWeight(u, v);
                int newDist = d.get(u) + weight;
                if (newDist < d.get(v)) {
                    d.put(v, newDist);
                }
            }
        }
        return d;
    }
}