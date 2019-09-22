import java.util.LinkedList;

// for DIRECTED graph only!!
public class GraphDirectedMatrix2Adj {
    public static void main(String[] args) {
        int V = 5;
        int m[][] = new int[][]{
            {0, 2, 0, 6, 0},
            {2, 0, 3, 8, 5},
            {0, 3, 0, 0, 7},
            {5, 0, 0, 0, 1},
            {0, 0, 0, 0, 0}};
        
        Graph g = Matrix2List(m, V);
        
        for (int v = 0; v < V; v++) {
            LinkedList<Vertex> alist = g.adj[v];
            if (!alist.isEmpty()) {
                for (Vertex u : alist) {
                    System.out.println(v + "-" + u.dest + " " + u.weight);
                }
                System.out.println();
            }
        }
        
        int[][] mtx = List2Matrix(g);
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                System.out.print(mtx[i][j] + " ");
                if (m[i][j] != mtx[i][j]) System.out.print("err");
            }
            System.out.println();
        }
    }
    
    public static Graph Matrix2List(int[][] m, int V) {
        Graph g = new Graph(V);

        for (int i = 0; i < V; i++) 
            for (int j = 0; j < V; j++) 
                if (m[i][j] > 0)
                    g.addEdge(i, j, m[i][j]);
        
        return g;
    }
    
    public static int[][] List2Matrix(Graph g) {
        int V = g.V;
        int[][] m = new int[V][V];
        for (int i = 0; i < V; i++) 
            for (int j = 0; j < V; j++) 
                m[i][j] = 0;
        
            for (int v = 0; v < V; v++) {
                LinkedList<Vertex> alist =  g.adj[v];
                if (!alist.isEmpty())
                    for (Vertex u : alist) {
                        m[v][u.dest] = u.weight;
                    }
            }
        
        return m;
    }
}

class Graph {
    int V;
    LinkedList<Vertex>[] adj;

    Graph(int V) {
        this.V = V;
        adj = new LinkedList[V];
        for (int v = 0; v < V; v++)
            adj[v] = new LinkedList<>();
    }

    void addEdge(int src, int dest, int weight) {
        adj[src].addLast(new Vertex(dest, weight));
    }
}

class Vertex {
    int dest, weight;

    Vertex(int a, int b) {
        dest = a;
        weight = b;
    }
}
