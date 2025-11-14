package structures;
import exceptions.*;
import collections.customhashmap.*;
import collections.DynamicArray;

public class Graph<V>{
    private boolean isDirected;
    private CustomHashMap<V, DynamicArray<Edge<V>>> adjList;

    public Graph(boolean isDirected){
        this.isDirected = isDirected;
        this.adjList = new CustomHashMap<>();
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
        adjList.remove(v);
        DynamicArray<V> allVertices = adjList.keySet();
        for(int i = 0;i<allVertices.getSize();i++){
            V u = allVertices.get(i);
            DynamicArray<Edge<V>> edges = adjList.get(u);

            for(int j = edges.getSize() - 1;j>=0;j--){
                Edge<V> edge = edges.get(j);
                if(edge.to.equals(v)){
                    edges.remove(j);
                }
            }
        }

    }
    public void removeEdge(V from, V to){
        if(adjList.containsKey(from)){
            DynamicArray<Edge<V>> edgesFrom = adjList.get(from);
            removeEdgeTo(edgesFrom, to);
        }
        if(!isDirected && adjList.containsKey(to)){
            DynamicArray<Edge<V>> edgesTo = adjList.get(to);
            removeEdgeTo(edgesTo, from);
        }
    }
    public DynamicArray<V> getAdjacent(V v){
        if(v == null){
            return new DynamicArray<>(10);
        }
        if(!adjList.containsKey(v)){
            return new DynamicArray<>(10);
        }
        DynamicArray<Edge<V>> edges = adjList.get(v);
        DynamicArray<V> adjVertices = new DynamicArray<>(10);
        for(int i = 0;i<edges.getSize();i++){
            Edge<V> edge = edges.get(i);
            adjVertices.add(edge.to);
        }
        return adjVertices;

    }
    void dfs(V start){

    }
    void bfs(V start){

    }
}