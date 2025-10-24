import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;


public class MSTTest {
    @Test
    public void testSmallGraphConsistentCost() {
        List<String> nodes = Arrays.asList("A","B","C","D");
        List<Edge> edges = Arrays.asList(
                new Edge("A","B",1),
                new Edge("A","C",4),
                new Edge("B","C",2),
                new Edge("C","D",3),
                new Edge("B","D",5)
        );
        Graph g = new Graph(nodes, edges);
        MSTResult p = Prim.run(g);
        MSTResult k = Kruskal.run(g);
        assertEquals(p.totalCost, k.totalCost);
        assertEquals(nodes.size() - 1, p.mstEdges.size());
        assertEquals(nodes.size() - 1, k.mstEdges.size());
    }


    @Test
    public void testDisconnectedGraphHandled() {
        List<String> nodes = Arrays.asList("A","B","C","D");
        List<Edge> edges = Arrays.asList(
                new Edge("A","B",1),
                new Edge("C","D",2)
        );
        Graph g = new Graph(nodes, edges);
        MSTResult p = Prim.run(g);
        MSTResult k = Kruskal.run(g);
        assertTrue(p.mstEdges.size() < nodes.size() - 1);
        assertTrue(k.mstEdges.size() < nodes.size() - 1);
    }
}