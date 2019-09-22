// O(min(E^2*V, E*FLOW )) - Maximum flow Edmonds-Karp
import java.io.*;
import java.util.*;

public class MaxFlowEdmondsKarp {
    static class Edge {
        int s, t, rev, cap, flow;

        public Edge(int s, int t, int rev, int cap) {
            this.s = s;
            this.t = t;
            this.rev = rev;
            this.cap = cap;
        }
    }

    public static List<Edge>[] createGraph(int V) {
        List<Edge>[] graph = new List[V];
        for (int i = 0; i < V; i++)
            graph[i] = new ArrayList<>();

        return graph;
    }

    public static void addEdge(List<Edge>[] graph, int s, int t, int cap) {
        graph[s].add(new Edge(s, t, graph[t].size(), cap));
        graph[t].add(new Edge(t, s, graph[s].size() - 1, 0));
    }

    public static int maxFlow(List<Edge>[] graph, int s, int t) {
        int flow = 0;
        int[] q = new int[graph.length + 1];
        while (true) {
            int qt = 0;
            q[qt++] = s;
            Edge[] pred = new Edge[graph.length];
            for (int qh = 0; qh < qt && pred[t] == null; qh++) {
                int cur = q[qh];
                for (Edge e : graph[cur]) {
                    if (pred[e.t] == null && e.cap > e.flow) {
                        pred[e.t] = e;
                        q[qt++] = e.t;
                    }
                }
            }
            
            if (pred[t] == null)
                break;

            int df = Integer.MAX_VALUE;
            for (int u = t; u != s; u = pred[u].s) {
                df = Math.min(df, pred[u].cap - pred[u].flow);
            }
            
            for (int u = t; u != s; u = pred[u].s) {
                pred[u].flow += df;
                graph[pred[u].t].get(pred[u].rev).flow -= df;
            }
            flow += df;
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
