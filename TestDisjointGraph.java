import java.util.*;

public class TestDisjointGraph {
    public static void main(String[] args) {
        Graph graph = new Graph(6);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(3, 4);
        graph.addEdge(5, 4);
        List<Graph> graphs = MAS.getDisjoint(graph);
        for (Graph g: graphs) {
            System.out.println("===== New Graph =====");
            g.printAdjacency();
        }
    }
}