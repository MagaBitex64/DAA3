import java.util.*;

public class Prim {
    public static MSTResult run(Graph graph) {
        int n = graph.Vertices();
        List<List<Edge>> adj = graph.adjacencyList();
        boolean[] visited = new boolean[n];
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingDouble(e -> e.weight));

        double totalCost = 0;
        int operations = 0;
        List<Edge> mstEdges = new ArrayList<>();

        visited[0] = true;
        pq.addAll(adj.get(0));

        long start = System.nanoTime();

        while (!pq.isEmpty() && mstEdges.size() < n - 1) {
            Edge e = pq.poll();
            int v = graph.indexM(e.to);
            if (visited[v]) continue;

            visited[v] = true;
            mstEdges.add(e);
            totalCost += e.weight;
            operations++;

            for (Edge next : adj.get(v)) {
                if (!visited[graph.indexM(next.to)]) pq.add(next);
            }
        }

        long end = System.nanoTime();
        double timeMs = (end - start) / 1_000_000.0;

        return new MSTResult(mstEdges, totalCost, operations, timeMs);
    }
}
