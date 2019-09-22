import java.io.*;
import java.util.*;

// this class may not performa as good as ConnectedComponentsAdjMatrix. 
// if performance is a concern, try convert AdjMatrix to AdjList and use the other class.
public class ConnectedComponentsAdjMatrix {
    public static void main(String[] args) throws IOException {
        test1();      // fully test with 10 test cases (same as FloydWarshall)
    }

    // chap 2.1 Sample, input as adjacency matrix
    private static void test1() throws IOException {
        BufferedReader f = new BufferedReader(new FileReader("1.in"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("1.out")));
        int V = Integer.parseInt(f.readLine());

        int[][] graph = new int[V][V];
        for (int i = 0; i < V; i++) {
            String s = f.readLine();
            for (int j = 0; j < V; j++)
                graph[i][j] = s.charAt(j) - '0';
        }

        ConnectedComponents cc = new ConnectedComponents(graph, V);
        
        Set<Integer>[] comps = cc.findCC();
        System.out.println(comps.length);
        for (Set<Integer> comp : comps) {
            System.out.println(comp);
        }
        /*
        int[] id = new int[V];
        int cCount = cc.findCC(id);
        System.out.println("num of components: " + cCount);
        for (int i = 0; i < V; i++)
            System.out.println("node " + (i + 1) + " in component " + id[i]);
        */

        out.close();
    }
}

// graph resprented by Adjacency Matrix. i/o: V from 1 ... V, internal: 0..V - 1 
class ConnectedComponents {
    int V;
    int[][] graph;
    boolean[] visited;
    int[] id;
    int count = 1;

    public ConnectedComponents(int[][] graph, int V) {
        this.graph = graph;
        this.V = V;
        this.id = new int[V];
        visited = new boolean[V];
    }

    public int findCC(int[] id) {
        for (int r = 0; r < V; r++) {
            if (!visited[r]) {
                dfs(r);
                count++;
            }
        }
        System.arraycopy(this.id, 0, id, 0, V);

        return count - 1;
    }

    // return array of sets. caller can use .length to find the number of components 
    public Set<Integer>[] findCC() { 
        for (int r = 0; r < V; r++) {
            if (!visited[r]) {
                dfs(r);
                count++;
            }
        }

        Set<Integer>[] comps = new HashSet[count - 1];
        for (int i = 0; i < count - 1; i++) {
            comps[i] = new HashSet<>();
        }

        for (int i = 0; i < V; i++) {
            comps[id[i] - 1].add(i + 1); // i/o vertex starts with 1, ... V.
        }

        return comps;
    }

    private void dfs(int v) {
        visited[v] = true;
        id[v] = count;
        for (int c = 0; c < V; c++) {
            if (c != v && !visited[c] && graph[v][c] > 0) {
                dfs(c);
            }
        }
    }
}