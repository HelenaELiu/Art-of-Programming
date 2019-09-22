import java.io.*;
import java.util.*;

// O(|V|^{2}) 
public class PrimAdjMatrix {
    public static void main(String[] args) throws IOException {
        //test1();
        test2();        // fully tested with 5 test cases
    }
    
    // return the weight of MST, and the mstEdges in List
    public static int MST(int V, int graph[][], Set<Edge> mstEdges) {
        // (parent[i], i) is the "from" vertix to vertix i
        int[] parent = new int[V];
        int[] dist = new int[V];
        Boolean mstSet[] = new Boolean[V];

        for (int i = 0; i < V; i++) {
            dist[i] = Integer.MAX_VALUE;
            mstSet[i] = false;
        }

        dist[0] = 0;     // start with vertex 0 (i.e, 1 from i/o perspective)
        parent[0] = -1;  // 1st node is always the root of MST 

        for (int count = 0; count < V - 1; count++) {
            int u = minDist(V, dist, mstSet);
            mstSet[u] = true;

            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && mstSet[v] == false && graph[u][v] < dist[v]) {
                    parent[v] = u;
                    dist[v] = graph[u][v];
                }
            }
        }

        int weight = 0;
        for (int i = 1; i < V; i++) { // the number of edges in MST is V - 1
            mstEdges.add(new Edge(parent[i], i));
            weight += graph[i][parent[i]];
        }

        return weight;
    }

    // find the vertex with min distance from the set of vertices not yet in MST 
    private static int minDist(int V, int dist[], Boolean mstSet[]) {
        int min = Integer.MAX_VALUE, min_index = -1;

        for (int v = 0; v < V; v++) {
            if (mstSet[v] == false && dist[v] < min) {
                min = dist[v];
                min_index = v;
            }
        }

        return min_index;
    }
    
    public static void test1() {
        int graph[][] = new int[][]{{0, 2, 0, 6, 0},
        {2, 0, 3, 8, 5},
        {0, 3, 0, 0, 7},
        {6, 8, 0, 0, 9},
        {0, 5, 7, 9, 0}};

        // edges: (0, 1), (1, 2), (0, 3), (1, 4)
        Set<Edge> mstEdges = new HashSet<>();
        System.out.println("MST weight: " + MST(5, graph, mstEdges));
        System.out.println("MST edges: " + mstEdges);
    }
    
    // chap 3.1 Agri-Net 
    private static void test2() throws IOException  {
        Scanner sc = new Scanner(new File("3.in"));
        PrintWriter out = new PrintWriter(new File("3.out"));
        int V = sc.nextInt();
        int[][] graph = new int[V][V];

        for (int i = 0; i < V; i++)
            for (int j = 0; j < V; j++)
                graph[i][j] = sc.nextInt();
        
        Set<Edge> mstEdges = new HashSet<>();
        out.println(MST(V, graph, mstEdges));

        out.close();
    }
}

class Edge {
    int start, end;

    public Edge(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "(" + start + ", " + end + ")";
    }
}
