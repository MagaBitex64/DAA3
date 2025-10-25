import com.google.gson.*;
import java.io.*;
import java.util.*;

public class PerformanceAnalyzer {

    public static void main(String[] args) {
        String outputFile = "output.json";
        String csvFile = "performance_analysis.csv";

        analyzeResults(outputFile, csvFile);
    }

    public static void analyzeResults(String outputFile, String csvFile) {
        System.out.println();

        try (FileReader reader = new FileReader(outputFile)) {
            JsonObject data = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray results = data.getAsJsonArray("results");

            List<AnalysisRow> analysisData = new ArrayList<>();


            for (JsonElement elem : results) {
                JsonObject result = elem.getAsJsonObject();
                AnalysisRow row = processResult(result);
                analysisData.add(row);
            }
            writeCSV(analysisData, csvFile);

        } catch (FileNotFoundException e) {
            System.out.println("Error: " + outputFile + " not found!");
            System.out.println("Please make sure output.json exists in the current directory.");
        } catch (Exception e) {
            System.out.println("Error analyzing results: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static AnalysisRow processResult(JsonObject result) {
        int graphId = result.get("graph_id").getAsInt();
        JsonObject stats = result.getAsJsonObject("input_stats");
        int vertices = stats.get("vertices").getAsInt();
        int edges = stats.get("edges").getAsInt();

        JsonObject prim = result.getAsJsonObject("prim");
        JsonObject kruskal = result.getAsJsonObject("kruskal");

        double primCost = prim.get("total_cost").getAsDouble();
        double kruskalCost = kruskal.get("total_cost").getAsDouble();
        double primTime = prim.get("execution_time_ms").getAsDouble();
        double kruskalTime = kruskal.get("execution_time_ms").getAsDouble();
        int primOps = prim.get("operations_count").getAsInt();
        int kruskalOps = kruskal.get("operations_count").getAsInt();

        double maxEdges = vertices * (vertices - 1) / 2.0;
        double density = maxEdges > 0 ? (edges / maxEdges * 100) : 0;

        String faster;
        double speedup;
        if (primTime < kruskalTime) {
            faster = "Prim";
            speedup = kruskalTime > 0 ? kruskalTime / primTime : 0;
        } else {
            faster = "Kruskal";
            speedup = primTime > 0 ? primTime / kruskalTime : 0;
        }

        boolean costMatch = Math.abs(primCost - kruskalCost) < 0.001;

        return new AnalysisRow(graphId, vertices, edges, density,
                primCost, kruskalCost, costMatch,
                primTime, kruskalTime, primOps, kruskalOps,
                faster, speedup);
    }


    private static void writeCSV(List<AnalysisRow> data, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Graph_ID,Vertices,Edges,Density_%,Prim_Cost,Kruskal_Cost,Cost_Match," +
                    "Prim_Time_ms,Kruskal_Time_ms,Prim_Operations,Kruskal_Operations," +
                    "Faster_Algorithm,Speedup_Factor");

            for (AnalysisRow row : data) {
                writer.printf("%d,%d,%d,%.2f,%.2f,%.2f,%s,%.4f,%.4f,%d,%d,%s,%.2f%n",
                        row.graphId, row.vertices, row.edges, row.density,
                        row.primCost, row.kruskalCost, row.costMatch ? "Yes" : "No",
                        row.primTime, row.kruskalTime, row.primOps, row.kruskalOps,
                        row.faster, row.speedup);
            }

            System.out.println("Analysis saved to " + filename);

        } catch (IOException e) {
            System.out.println("Error writing CSV: " + e.getMessage());
        }
    }

    static class AnalysisRow {
        int graphId;
        int vertices;
        int edges;
        double density;
        double primCost;
        double kruskalCost;
        boolean costMatch;
        double primTime;
        double kruskalTime;
        int primOps;
        int kruskalOps;
        String faster;
        double speedup;

        AnalysisRow(int graphId, int vertices, int edges, double density,
                    double primCost, double kruskalCost, boolean costMatch,
                    double primTime, double kruskalTime, int primOps, int kruskalOps,
                    String faster, double speedup) {
            this.graphId = graphId;
            this.vertices = vertices;
            this.edges = edges;
            this.density = density;
            this.primCost = primCost;
            this.kruskalCost = kruskalCost;
            this.costMatch = costMatch;
            this.primTime = primTime;
            this.kruskalTime = kruskalTime;
            this.primOps = primOps;
            this.kruskalOps = kruskalOps;
            this.faster = faster;
            this.speedup = speedup;
        }
    }
}