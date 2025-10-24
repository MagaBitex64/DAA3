import java.util.List;

public class MSTResult {
    public final List<Edge> mstEdges;
    public final double totalCost;
    public final int operations;
    public final double executionTimeMs;

    public MSTResult(List<Edge> mstEdges, double totalCost, int operations, double executionTimeMs) {
        this.mstEdges = mstEdges;
        this.totalCost = totalCost;
        this.operations = operations;
        this.executionTimeMs = executionTimeMs;
    }
}
