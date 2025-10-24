import java.util.*;
public class Graph {
    public final List<String> nodes;
    public final List<Edge> edges;
    private final Map<String, Integer> indexMap;


    public Graph(List<String> nodes, List<Edge> edges) {
        this.nodes = new ArrayList<>(nodes);
        this.edges = new ArrayList<>(edges);
        this.indexMap = new HashMap<>();
        for (int i = 0; i < this.nodes.size(); i++) {
            indexMap.put(this.nodes.get(i), i);
        }
    }

    public int Vertices(){
        return nodes.size();
    }
    public int Edges()
    {
        return edges.size();
    }
    public int indexM(String node)
    {
        return indexMap.getOrDefault(node, -1);
    }

    public List<List<Edge>> adjacencyList() {
        List<List<Edge>> adj = new ArrayList<>();
        for (int i = 0; i < Vertices(); i++) adj.add(new ArrayList<>());

        for (Edge e : edges) {
            int u = indexM(e.from);
            int v = indexM(e.to);

            if (u == -1 || v == -1) {
                System.err.println("Edge ignored: invalid node " + e.from + " - " + e.to);
                continue;
            }

            adj.get(u).add(new Edge(e.from, e.to, e.weight));
            adj.get(v).add(new Edge(e.to, e.from, e.weight));
        }
        return adj;
    }

}
