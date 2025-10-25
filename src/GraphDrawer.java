import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.ext.JGraphXAdapter;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.model.mxCell;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

public class GraphDrawer {
    public static void draw(Graph graph, MSTResult result, int id) {
        SimpleWeightedGraph<String, DefaultWeightedEdge> jGraph =
                new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        for (String node : graph.nodes)
            jGraph.addVertex(node);

        for (Edge e : graph.edges) {
            jGraph.addEdge(e.from, e.to);
            jGraph.setEdgeWeight(e.from, e.to, e.weight);
        }

        JGraphXAdapter<String, DefaultWeightedEdge> adapter = new JGraphXAdapter<>(jGraph);
        mxCircleLayout layout = new mxCircleLayout(adapter);
        layout.execute(adapter.getDefaultParent());

        for (DefaultWeightedEdge edge : jGraph.edgeSet()) {
            String from = jGraph.getEdgeSource(edge);
            String to = jGraph.getEdgeTarget(edge);
            double weight = jGraph.getEdgeWeight(edge);

            Object cellObj = adapter.getEdgeToCellMap().get(edge);
            if (cellObj == null) continue;

            if (cellObj instanceof mxCell) {
                mxCell cell = (mxCell) cellObj;
                cell.setValue(String.format("%.1f", weight));

                boolean inMST = result.mstEdges.stream().anyMatch(me ->
                        (me.from.equals(from) && me.to.equals(to) && Double.compare(me.weight, weight) == 0) ||
                                (me.from.equals(to) && me.to.equals(from) && Double.compare(me.weight, weight) == 0)
                );

                String color = inMST ? "red" : "gray";
                adapter.setCellStyle("strokeColor=" + color + ";strokeWidth=" + (inMST ? "3" : "1"), new Object[]{cell});
            }
        }


        try {
            BufferedImage image = mxCellRenderer.createBufferedImage(adapter, null, 2, Color.WHITE, true, null);
            File imgFile = new File("mst_graph_" + id + ".png");
            ImageIO.write(image, "PNG", imgFile);
            System.out.println("Saved graph image: " + imgFile.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
