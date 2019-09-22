// O(V^2 * E) - Maximum flow Dinic's
import java.io.*;
import java.util.*;

public class MaxFlowDinic {
    static class Edge {
        int t, rev, cap, flow;

        public Edge(int t, int rev, int cap) {
            this.t = t;
            this.rev = rev;
            this.cap = cap;
        }
    }

    public static List<Edge>[] createGraph(int V) {
        List<Edge>[] graph = new List[V];
        for (int i = 0; i < V; i++) {
            graph[i] = new ArrayList<>();
        }
        
        return graph;
    }

    public static void addEdge(List<Edge>[] graph, int s, int t, int cap) {
        graph[s].add(new Edge(t, graph[t].size(), cap));
        graph[t].add(new Edge(s, graph[s].size() - 1, 0));
    }

    static boolean bfs(List<Edge>[] graph, int src, int dest, int[] dist) {
        Arrays.fill(dist, -1);
        dist[src] = 0;
        int[] Q = new int[graph.length];
        int sizeQ = 0;
        Q[sizeQ++] = src;
        
        for (int i = 0; i < sizeQ; i++) {
            int u = Q[i];
            for (Edge e : graph[u]) {
                if (dist[e.t] < 0 && e.flow < e.cap) {
                    dist[e.t] = dist[u] + 1;
                    Q[sizeQ++] = e.t;
                }
            }
        }
        
        return dist[dest] >= 0;
    }

    static int dfs(List<Edge>[] graph, int[] ptr, int[] dist, int dest, int u, int f) {
        if (u == dest)
            return f;

        for (; ptr[u] < graph[u].size(); ++ptr[u]) {
            Edge e = graph[u].get(ptr[u]);
            if (dist[e.t] == dist[u] + 1 && e.flow < e.cap) {
                int df = dfs(graph, ptr, dist, dest, e.t, Math.min(f, e.cap - e.flow));
                if (df > 0) {
                    e.flow += df;
                    graph[e.t].get(e.rev).flow -= df;
                    return df;
                }
            }
        }

        return 0;
    }

    public static int maxFlow(List<Edge>[] graph, int src, int dest) {
        int flow = 0;
        int[] dist = new int[graph.length];
        while (bfs(graph, src, dest, dist)) {
            int[] ptr = new int[graph.length];
            while (true) {
                int df = dfs(graph, ptr, dist, dest, src, Integer.MAX_VALUE);
                if (df == 0) {
                    break;
                }
                flow += df;
            }
        }
        
        return flow;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader f = new BufferedReader(new FileReader("3.in"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("3.out")));
        StringTokenizer st = new StringTokenizer(f.readLine());
        int V = Integer.parseInt(st.nextToken());
        int E = Integer.parseInt(st.nextToken());
        int S = Integer.parseInt(st.nextToken());       // source
        int T = Integer.parseInt(st.nextToken());       // sink
        List<Edge>[] graph = createGraph(V);

        for (int i = 0; i < E; i++) {
            st = new StringTokenizer(f.readLine());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int capacity = Integer.parseInt(st.nextToken());
            addEdge(graph, from, to, capacity);
        }

        out.println(maxFlow(graph, S, T));
        out.close();
    }
}