import java.io.*;
import java.util.*;

// O(V^2 * FLOW) - Maximum flow Ford-Fulkerson
public class MaxFlowFordFulkerson {
    public static int maxFlow(int[][] cap, int s, int t) {
        for (int flow = 0;;) {
            int df = dfs(cap, new boolean[cap.length], s, t, Integer.MAX_VALUE);
            if (df == 0) return flow;

            flow += df;
        }
    }

    // dfs
    static int dfs(int[][] cap, boolean[] vis, int u, int t, int f) {
        if (u == t) return f;

        vis[u] = true;
        for (int v = 0; v < vis.length; v++)
            if (!vis[v] && cap[u][v] > 0) {
                int df = dfs(cap, vis, v, t, Math.min(f, cap[u][v]));
                if (df > 0) {
                    cap[u][v] -= df;
                    cap[v][u] += df;
                    return df;
                }
            }

        return 0;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader f = new BufferedReader(new FileReader("1.in"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("1.out")));
        StringTokenizer st = new StringTokenizer(f.readLine());
        int V = Integer.parseInt(st.nextToken());
        int S = Integer.parseInt(st.nextToken());       // source
        int T = Integer.parseInt(st.nextToken());       // sink
        int[][] graph = new int[V][V];

        for (int i = 0; i < V; i++) {
            st = new StringTokenizer(f.readLine());
            for (int j = 0; j < V; j++)
                graph[i][j] = Integer.parseInt(st.nextToken());
        }

        out.println(maxFlow(graph, S, T));
        out.close();
    }
}