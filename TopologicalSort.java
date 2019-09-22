import java.io.*;
import java.util.*;

public class TopologicalSort {
    static void dfs(List<Integer>[] graph, boolean[] used, List<Integer> res, int u) {
        used[u] = true;
        for (int v : graph[u]) {
            if (!used[v])
                dfs(graph, used, res, v);
        }

        res.add(u);
    }

    public static List<Integer> topologicalSort(List<Integer>[] graph) {
        int n = graph.length;
        boolean[] used = new boolean[n];
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!used[i])
                dfs(graph, used, res, i);
        }

        Collections.reverse(res);
        return res;
    }

    public static List<Integer>[] createGraph(int V) {
        List<Integer>[] graph = new List[V];
        for (int i = 0; i < V; i++)
            graph[i] = new ArrayList<>();

        return graph;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader f = new BufferedReader(new FileReader("2.in"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("2.out")));
        StringTokenizer st = new StringTokenizer(f.readLine());
        int V = Integer.parseInt(st.nextToken());
        int E = Integer.parseInt(st.nextToken());

        List<Integer>[] graph = createGraph(V);

        for (int i = 0; i < E; i++) {
            st = new StringTokenizer(f.readLine());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            graph[from].add(to);
        }

        List<Integer> res = topologicalSort(graph);
        out.println(res);
        out.close();
    }
}