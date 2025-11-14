package structures;
public class Edge<V>{
    V to;
    int weight;
    
    Edge(V to, int weight){
        this.to = to;
        this.weight = weight;
    }

    V getTo(){
        return to;
    }
    int getWeight(){
        return weight;
    }
}