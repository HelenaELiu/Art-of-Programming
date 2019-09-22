import java.io.*;
import java.util.*;

// O(E + VlogV)
public class Dijkstra {
    public static void main(String[] args)throws IOException  {
        //test1();      
        //test2();        // fully tested with 10 test cases
        test3();      // fully tested with 10 test cases
    }

    //int[] dist - distance to src from v [ 1.... V]
    //int[] prev - v's prev/parent node in the path
    public static void dijkstra(Graph graph, int src, int[] dist, int[] prev) {
        boolean[] visited = new boolean[graph.V + 1]; 
        Arrays.fill(dist, Integer.MAX_VALUE);
        PriorityQueue<Vertex> q = new PriorityQueue<>(graph.V);
        q.add(new Vertex(src, 0));
        dist[src] = 0;

        while (!q.isEmpty()) {
            int u = q.poll().id;        // u = top of the PQ
            if (!visited[u]) {
                visited[u] = true;

                for (Vertex v : graph.adj[u]) { // for all edges from u -> v
                    if (!visited[v.id]) {
                        // Note: the commented line below: handles multiple paths with equal weight -
                        // to choose the path with smallest lexicall order when the path is expresssed in from dest to src order
                        // e.g., src = 1, dest = 5, the path [1, 3, 5] is choosen over [1, 4, 5] becase 5, 3, 1 is smaller than 5, 4, 1
                        // if (dist[u] != Integer.MAX_VALUE && (dist[u] + v.weight < dist[v.id] || (dist[u] + v.weight == dist[v.id] && u < prev[v.id]))) {
                        if (dist[u] != Integer.MAX_VALUE && dist[u] + v.weight < dist[v.id]) {
                            prev[v.id] = u;
                            dist[v.id] = dist[u] + v.weight;
                            q.add(new Vertex(v.id, dist[v.id]));
                        }
                    }
                }
            }
        }
    }
    
    // path is return from dest to src, e.g., [dest, ....,, src]
    public static List<Integer> getPath(int[] prev, int src, int dest) {
        List<Integer> path = new ArrayList<>();

        int current = dest;
        while (current != src) {
            path.add(current);
            current = prev[current];
        }
        path.add(src);
        
        //Collections.reverse(path);    // this line reserve path to [src ... dest]
        return path;
    }
    
    // chap 2.4 Sample 
    private static void test1() throws IOException  {
        BufferedReader f = new BufferedReader(new FileReader("1.in"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("1.out")));
        StringTokenizer st = new StringTokenizer(f.readLine());
        int V = Integer.parseInt(st.nextToken());
        int E = Integer.parseInt(st.nextToken());
        Graph graph = new Graph(V);
        
        for (int i = 0; i < E; i++) {
            st = new StringTokenizer(f.readLine());
            int src = Integer.parseInt(st.nextToken());
            int dest = Integer.parseInt(st.nextToken());
            int weight = Integer.parseInt(st.nextToken());
            graph.addEdge(src, dest, weight);
        }
        
        int[] dist = new int[V + 1];
        int[] prev = new int[V + 1];
        dijkstra(graph, 1, dist, prev);
        
        for (int i = 1; i <= V; i++)
            System.out.println(dist[i]);
        
        System.out.println(getPath(prev, 1, 6));

        out.close();
    }
    
    // chap 2.4 Bessie come home
    private static void test2() throws IOException  {
        BufferedReader f = new BufferedReader(new FileReader("2-9.in"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("2-0.out")));
        int V = 52;
        int E = Integer.parseInt(f.readLine());
        Graph graph = new Graph(V);
        StringTokenizer st;
        
        for (int i = 0; i < E; i++) {
            st = new StringTokenizer(f.readLine());
            int src = st.nextToken().charAt(0); 
            src = Character.isUpperCase(src) ? src - 'A' + 27 : src - 'a' + 1;
            int dest = st.nextToken().charAt(0); 
            dest = Character.isUpperCase(dest) ? dest - 'A' + 27 : dest - 'a' + 1; 
            int weight = Integer.parseInt(st.nextToken());
            graph.addEdge(src, dest, weight);
        }
        
        int[] dist = new int[V + 1];
        int[] prev = new int[V + 1];
        dijkstra(graph, V, dist, prev);
        
        int min = Integer.MAX_VALUE;
        int src = 0;
        for (int i = 27; i < 52; i++) {  // A to Y
            if (dist[i] < min) {
                min = dist[i];
                src = i;
            }
        }
        
        System.out.println((char) (src + 'A' - 27) + " " + min);

        out.close();
    }
    
    // chap 3.2 sweet butter 
    private static void test3() throws IOException  {
        BufferedReader f = new BufferedReader(new FileReader("3-9.in"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("3-9.out")));
        StringTokenizer st = new StringTokenizer(f.readLine());
        int N = Integer.parseInt(st.nextToken());   // num of cows
        int V = Integer.parseInt(st.nextToken());   // = P    
        int E = Integer.parseInt(st.nextToken());   // = C
        Graph graph = new Graph(V);

        int[] cows = new int[N];
        for (int i = 0; i < N; i++) {
            cows[i] = Integer.parseInt(f.readLine());
        }        
        
        for (int i = 0; i < E; i++) {
            st = new StringTokenizer(f.readLine());
            int src = Integer.parseInt(st.nextToken());
            int dest = Integer.parseInt(st.nextToken());
            int weight = Integer.parseInt(st.nextToken());
            graph.addEdge(src, dest, weight);
        }
        
        int min = Integer.MAX_VALUE;
        int[] dist = new int[V + 1];
        int[] prev = new int[V + 1];
        for (int i = 1; i <= V; i++) {
            dijkstra(graph, i, dist, prev);
            
            int cost = 0;
            for (int j : cows) 
                cost += dist[j];
            
            min = Math.min(min, cost);
        }

        System.out.println(min);

        out.close();
    }
}

class Vertex implements Comparable<Vertex> {
    public int id;
    public int weight;

    public Vertex(int name, int weight) {
        this.id = name;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "id: " + id + ", value: " + weight;
    }

    @Override
    public int compareTo(Vertex v) {
        return Integer.compare(weight, v.weight);
    }
}

class Graph {
    int V;
    ArrayList<Vertex>[] adj;

    Graph(int e) {
        V = e;
        adj = new ArrayList[V + 1];
        for (int v = 1; v <= V; v++)
            adj[v] = new ArrayList<>();
    }

    void addEdge(int src, int dest, int weight) {
        adj[src].add(new Vertex(dest, weight));
        adj[dest].add(new Vertex(src, weight));
    }
}
