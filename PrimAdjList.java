import java.io.*;
import java.util.*;

// O((V+E)logV) = O(ElogV)
public class PrimAdjList {
    public static void main(String[] args) throws IOException {
        BufferedReader f = new BufferedReader(new FileReader("1.in"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("1.out")));
        StringTokenizer st = new StringTokenizer(f.readLine());
        int V = Integer.parseInt(st.nextToken());   
        int E = Integer.parseInt(st.nextToken());
        
        List<Edge>[] edges = new List[V];
        for (int i = 0; i < V; i++)
            edges[i] = new ArrayList<>();
        
        for (int i = 0; i < E; i++) {
            st = new StringTokenizer(f.readLine());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int weight = Integer.parseInt(st.nextToken());
            edges[from].add(new Edge(to, weight));
            edges[to].add(new Edge(from, weight));
        }
        
        int[] parent = new int[V];            
        out.println(MST(edges, parent));  
        //Set<Line> mstEdges = new HashSet<>();
        //out.println(MST(V, edges, mstEdges));
        //out.println(mstEdges);

        out.close();
    }

    public static long MST(List<Edge>[] edges, int[] parent) {
        int n = edges.length;
        Arrays.fill(parent, -1);
        boolean[] visited = new boolean[n];
        int[] priority = new int[n];
        Arrays.fill(priority, Integer.MAX_VALUE);
        priority[0] = 0;
        PriorityQueue<Long> pq = new PriorityQueue<>();
        pq.add(0L);
        long weight = 0;

        while (!pq.isEmpty()) {
            long cur = pq.poll();
            int u = (int) cur;
            if (visited[u]) continue;
            
            visited[u] = true;
            weight += cur >>> 32;
            for (Edge e : edges[u]) {
                int v = e.to;
                if (!visited[v] && priority[v] > e.weight) {
                    priority[v] = e.weight;
                    parent[v] = u;
                    pq.add(((long) priority[v] << 32) + v);
                }
            }
        }
        
        return weight;
    }

    static class Edge {
        int to, weight;
        public Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }
    
    /*
    static long MST(int V, List<Edge>[] edges, Set<Line> mstEdges) {
        int[] parent = new int[V];
        long weight = MST(edges, parent);
        
        for (int i = 1; i < V; i++) { // the number of edges in MST is V - 1
            mstEdges.add(new Line(parent[i], i));
        }
        
        return weight;
    }

    static class Line {
        int from, to;
        public Line(int from, int to) {
            this.from = from;
            this.to = to;
        }
        
        public String toString() {
            return "(" + from + ", " + to + ")";
        }
    }
    */
}