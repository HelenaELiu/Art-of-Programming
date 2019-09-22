import java.io.*;
import java.util.*;

public class EulerianTour {
    public static void main(String[] args) throws IOException {
        // chap 3.3 Riding the Fences - fully tested with 8 test cases
        BufferedReader f = new BufferedReader(new FileReader("8.in"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("8.out")));
        int V = 500;   
        int E = Integer.parseInt(f.readLine());  
        Graph graph = new Graph(V, E);          // Initialize !!
        
        StringTokenizer st;
        for (int i = 0; i < E; i++) {
            st = new StringTokenizer(f.readLine());
            int src = Integer.parseInt(st.nextToken()) - 1;     //switch to 0 based for processing
            int end = Integer.parseInt(st.nextToken()) - 1;
            graph.addEdge(src, end);
        }
        
        for (int v = 0; v < graph.V; v++) 
            Collections.sort(graph.adj[v]);
        
        int[] cycle = EulerianCycle(graph); 
        for (int i = cycle.length - 1; i >= 0; i--)
            out.println((cycle[i] + 1)); //back to 1 based for output

        out.close();
    }
    
    public static int[] EulerianCycle(Graph G) {
        int start = 0;
        for (int v = 0; v < G.V; v++) {
            if (G.adj[v].size() % 2 != 0) {
                start = v;
                break;
            }
        }

        // Edge & local copy of adjlist is used to avoid exploring both copies of an edge v-w
        LinkedList<Edge>[] adj = new LinkedList[G.V];
        for (int v = 0; v < G.V; v++) {
            adj[v] = new LinkedList<>();
        }
        
        for (int v = 0; v < G.V; v++) {
            int selfLoops = 0;
            for (int w : G.adj[v]) {
                if (v == w) { // self loops
                    if (selfLoops % 2 == 0) {
                        Edge e = new Edge(v, w);
                        adj[v].add(e);
                        adj[w].add(e);
                    }
                    selfLoops++;
                } else if (v < w) {
                    Edge e = new Edge(v, w); 
                    adj[v].add(e);
                    adj[w].add(e);
                }
            }
        }

        int[] cycle = new int[G.E + 1];  
        int pos = 0;
        Stack<Integer> stack = new Stack<>();
        stack.push(start);
        while (!stack.isEmpty()) { // dfs
            int v = stack.pop();
            while (!adj[v].isEmpty()) {
                Edge edge = adj[v].remove(0);
                if (edge.isUsed) {
                    continue;
                }
                edge.isUsed = true;
                stack.push(v);
                v = edge.other(v);
            }
            // push vertex with no more leaving edges to cycle
            cycle[pos++] = v;
        }

        return cycle;
    }
    
    static class Edge {
        private final int v;
        private final int w;
        private boolean isUsed;

        public Edge(int v, int w) {
            this.v = v;
            this.w = w;
            isUsed = false;
        }

        public int other(int vertex) {
            return vertex == v ? w : v;
        }
    }
    
    static class Graph {
        int V, E;
        LinkedList<Integer>[] adj;

        Graph(int V, int E) {
            this.V = V;
            adj = new LinkedList[V];
            for (int v = 0; v < V; v++) {
                adj[v] = new LinkedList<>();
            }
            
            this.E = E;
        }

        void addEdge(int src, int dest) {
            adj[src].addLast(dest);
            adj[dest].addLast(src);
        }
    }
}