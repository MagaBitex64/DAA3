import java.util.*;
public class Kruskal {
    public static MSTResult run(Graph g)
    {
        long start = System.nanoTime();
        List<Edge> edges = new ArrayList<>(g.edges);
        Collections.sort(edges);
        UF uf = new UF(g.Vertices());
        List<Edge> mst = new ArrayList<>();
        long ops = 0; // comparisons + union/finds
        for (Edge e : edges) {
            ops++; // edge consideration
            int u = g.indexM(e.from);
            int v = g.indexM(e.to);
            if (u == -1 || v == -1) continue;
            int ru = uf.find(u); ops++;
            int rv = uf.find(v); ops++;
            if (ru != rv) {
                boolean merged = uf.union(ru, rv); ops++;
                if (merged) mst.add(e);
            }
            if (mst.size() == g.Vertices() - 1) break;
        }
        long total = mst.stream().mapToLong(x -> x.weight).sum();
        double timeMs = (System.nanoTime() - start) / 1e6;
        long opsTotal = ops + uf.findCount + uf.unionCount;
        return new MSTResult(mst, total, opsTotal, timeMs);
    }
}
