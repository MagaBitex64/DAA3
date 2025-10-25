import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.ext.JGraphXAdapter;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.util.mxCellRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class GraphDrawer {
    public static void draw(Graph graph, MSTResult result) {
        SimpleWeightedGraph<String, DefaultWeightedEdge> jGraph =
                new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        for (String node : graph.nodes)
            jGraph.addVertex(node);

        for (Edge e : graph.edges) {
            jGraph.addEdge(e.from, e.to);
            jGraph.setEdgeWeight(e.from, e.to, e.weight);
        }

        JGraphXAdapter<String, DefaultWeightedEdge> graphAdapter =
                new JGraphXAdapter<>(jGraph);

        mxCircleLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        try {
            BufferedImage image = mxCellRenderer.createBufferedImage(
                    graphAdapter, null, 2, java.awt.Color.WHITE, true, null);
            File imgFile = new File("mst_graph_" + System.currentTimeMillis() + ".png");
            ImageIO.write(image, "PNG", imgFile);
            System.out.println("Saved graph image: " + imgFile.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
