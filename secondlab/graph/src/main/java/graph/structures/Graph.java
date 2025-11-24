package structures;
import exceptions.*;
import collections.customhashmap.*;

import java.util.ArrayList;
import java.util.List;

import algorithms.GraphAlgorithms;
import collections.DynamicArray;

public class Graph<V>{
    private static final int DEFAULT_CAPACITY = 10;

    private boolean isDirected;
    private CustomHashMap<V, DynamicArray<Edge<V>>> adjList;
    private DynamicArray<V> lastDfsResult;
    private DynamicArray<V> lastBfsResult;
    private CustomHashMap<V, Integer> lastDijkstraResult;

    public Graph(boolean isDirected){
        this.isDirected = isDirected;
        this.adjList = new CustomHashMap<>();
        this.lastDfsResult = null;
        this.lastBfsResult = null;
        this.lastDijkstraResult = null;
    }
    private void removeEdgeTo(DynamicArray<Edge<V>> edgeList, V target){
        for(int i = edgeList.getSize()-1;i>=0;i--){
            Edge<V> edge = edgeList.get(i);
            if(edge.to.equals(target)){
                edgeList.remove(i);
            }
        }
    }

    public void addVertex(V v){
        if(v == null){
            throw new InvalidVertexException("Vertex cannot be null");
        }
        if(!adjList.containsKey(v)){
            adjList.put(v, new DynamicArray<Edge<V>>(10));
        }
    }
    public void addEdge(V from, V to, int weight){
        if(from == null||to==null){
            throw new InvalidVertexException("Vertices cannot be null");
        }
        addVertex(from);
        addVertex(to);
        DynamicArray<Edge<V>> fromEdges = adjList.get(from);
        fromEdges.add(new Edge<>(to, weight));
        if(!isDirected){
            DynamicArray<Edge<V>> toEdges = adjList.get(to);
            toEdges.add(new Edge<>(from, weight));
        }
    }
    public void removeVertex(V v){
        if(!adjList.containsKey(v)){
            return;
        }
        DynamicArray<V> allVertices = adjList.keySet();
        for (int i = 0; i < allVertices.getSize(); i++){
            V u = allVertices.get(i);
            if (!u.equals(v)){
                DynamicArray<Edge<V>> edges = adjList.get(u);
                for (int j = edges.getSize() - 1; j >= 0; j--){
                    Edge<V> edge = edges.get(j);
                    if (edge.to.equals(v)){
                        edges.remove(j);
                    }
                }
            }
        }
        adjList.remove(v);
    }
    public void removeEdge(V from, V to){
        if (from == null || to == null) {
            throw new InvalidVertexException("Vertices cannot be null");
        }
        if(adjList.containsKey(from)){
            DynamicArray<Edge<V>> edgesFrom = adjList.get(from);
            removeEdgeTo(edgesFrom, to);
        }
        if(!isDirected && adjList.containsKey(to)){
            DynamicArray<Edge<V>> edgesTo = adjList.get(to);
            removeEdgeTo(edgesTo, from);
        }
    }
    public List<V> getAdjacent(V v){
        if(v == null){
            return new ArrayList<>();
        }
        if(!adjList.containsKey(v)){
            return new ArrayList<>();
        }
        DynamicArray<Edge<V>> edges = adjList.get(v);
        List<V> adjVertices = new ArrayList<>();
        for(int i = 0;i<edges.getSize();i++){
            Edge<V> edge = edges.get(i);
            adjVertices.add(edge.to);
        }
        return adjVertices;

    }
    public DynamicArray<V> getArrayAdjacent(V v){
        if(v == null){
            return new DynamicArray<>(DEFAULT_CAPACITY);
        }
        if(!adjList.containsKey(v)){
            return new DynamicArray<>(DEFAULT_CAPACITY);
        }
        DynamicArray<Edge<V>> edges = adjList.get(v);
        DynamicArray<V> adjArrayVertices = new DynamicArray<>(DEFAULT_CAPACITY);
        for(int i = 0;i<edges.getSize();i++){
            Edge<V> edge = edges.get(i);
            adjArrayVertices.add(edge.to);
        }
        return adjArrayVertices;
    }
    public int getVertexCount(){
        return adjList.getSize();
    }
    public int getEdgeWeight(V from, V to){
        if(from == null||to==null){
            throw new InvalidVertexException("Vertices cannot be null");
        }
        if(!adjList.containsKey(from)){
            throw new InvalidVertexException("Vertex "+from+" does not exist");
        }
        DynamicArray<Edge<V>> edges = adjList.get(from);
        for (int i = 0; i < edges.getSize(); i++) {
            Edge<V> edge = edges.get(i);
            if (edge.getTo().equals(to)) {
                return edge.getWeight();
            }
        }
        throw new InvalidVertexException("No edge from " +from+ " to " +to);
    }

    public void dfs(V start){
        if(!adjList.containsKey(start)){
            throw new InvalidVertexException("Vertex " +start+ " does not exist");
        }
        this.lastDfsResult = GraphAlgorithms.dfs(this, start);
    }
    public void bfs(V start){
        if(!adjList.containsKey(start)){
            throw new InvalidVertexException("Vertex " +start+ " does not exist");
        }
        this.lastBfsResult = GraphAlgorithms.bfs(this, start);
    }
    public void dijkstra(V start){
        if(!adjList.containsKey(start)){
            throw new InvalidVertexException("Vertex " +start+ " does not exist");
        }
        this.lastDijkstraResult = GraphAlgorithms.dijkstra(this, start);
    }
    public DynamicArray<V> getAllVertices(){
        return adjList.keySet();
    }
    public DynamicArray<V> getLastDfsResult(){
        return lastDfsResult;
    }
    public DynamicArray<V> getLastBfsResult(){
        return lastBfsResult;
    }
    public CustomHashMap<V, Integer> getLastDijkstraResult(){
        return lastDijkstraResult;
    }
}