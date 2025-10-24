import java.util.*;

public class Kruskal {
    public static MSTResult run(Graph graph) {
        List<Edge> edges = new ArrayList<>(graph.edges);
        edges.sort(Comparator.comparingDouble(e -> e.weight));

        int n = graph.Vertices();
        int[] parent = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;

        long start = System.nanoTime();
        List<Edge> mst = new ArrayList<>();
        double totalCost = 0;
        int operations = 0;

        for (Edge e : edges) {
            int u = graph.indexM(e.from);
            int v = graph.indexM(e.to);
            if (find(u, parent) != find(v, parent)) {
                union(u, v, parent);
                mst.add(e);
                totalCost += e.weight;
                operations++;
            }
        }

        long end = System.nanoTime();
        double timeMs = (end - start) / 1_000_000.0;
        return new MSTResult(mst, totalCost, operations, timeMs);
    }

    private static int find(int x, int[] parent) {
        if (parent[x] != x) parent[x] = find(parent[x], parent);
        return parent[x];
    }

    private static void union(int a, int b, int[] parent) {
        parent[find(a, parent)] = find(b, parent);
    }
}
