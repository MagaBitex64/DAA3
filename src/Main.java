import com.google.gson.*;
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String inputPath = "assign_3_input.json";
        String outputPath = "output.json";

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<JsonObject> results = new ArrayList<>();

        try (FileReader reader = new FileReader(inputPath)) {
            JsonObject data = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray graphs = data.getAsJsonArray("graphs");
            for (JsonElement gElem : graphs) {
                JsonObject gObj = gElem.getAsJsonObject();
                int id = gObj.get("id").getAsInt();

                List<String> nodes = new ArrayList<>();
                for (JsonElement node : gObj.getAsJsonArray("nodes")) {
                    nodes.add(node.getAsString());
                }

                List<Edge> edges = new ArrayList<>();
                for (JsonElement eElem : gObj.getAsJsonArray("edges")) {
                    JsonObject eObj = eElem.getAsJsonObject();
                    edges.add(new Edge(
                            eObj.get("from").getAsString(),
                            eObj.get("to").getAsString(),
                            eObj.get("weight").getAsDouble()
                    ));
                }

                Graph graph = new Graph(nodes, edges);

                MSTResult prim = Prim.run(graph);
                MSTResult kruskal = Kruskal.run(graph);

                if(id <= 3)
                {
                    System.out.println("Graph of small id"+ id);
                    GraphDrawer.draw(graph, prim,id);
                }

                JsonObject result = new JsonObject();
                result.addProperty("graph_id", id);

                JsonObject stats = new JsonObject();
                stats.addProperty("vertices", graph.Vertices());
                stats.addProperty("edges", graph.Edges());
                result.add("input_stats", stats);

                result.add("prim", mstToJson(prim, gson));
                result.add("kruskal", mstToJson(kruskal, gson));

                results.add(result);
            }

            JsonObject out = new JsonObject();
            JsonArray arr = new JsonArray();
            results.forEach(arr::add);
            out.add("results", arr);

            try (FileWriter writer = new FileWriter(outputPath)) {
                gson.toJson(out, writer);
            }

            System.out.println("Results saved" + outputPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static JsonObject mstToJson(MSTResult mst, Gson gson) {
        JsonObject obj = new JsonObject();
        JsonArray edgesArr = new JsonArray();
        for (Edge e : mst.mstEdges) {
            JsonObject edgeObj = new JsonObject();
            edgeObj.addProperty("from", e.from);
            edgeObj.addProperty("to", e.to);
            edgeObj.addProperty("weight", e.weight);
            edgesArr.add(edgeObj);
        }
        obj.add("mst_edges", edgesArr);
        obj.addProperty("total_cost", mst.totalCost);
        obj.addProperty("operations_count", mst.operations);
        obj.addProperty("execution_time_ms", mst.executionTimeMs);
        return obj;
    }
}
