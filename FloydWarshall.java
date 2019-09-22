import java.util.*;
import java.io.*;

public class FloydWarshall {
    public static void main(String[] args) throws IOException {
        test1();        // fully tested with 9 test cases
    }

    public static int[][] floydWarshall(int graph[][], int V) {  // V is 0 based
        int dist[][] = new int[V][V];

        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (graph[i][j] > 0)
                    dist[i][j] = graph[i][j];
                else if (i != j)
                    dist[i][j] = Integer.MAX_VALUE;
            }
        }

        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (i != j && dist[i][k]!= Integer.MAX_VALUE && dist[k][j] != Integer.MAX_VALUE && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        return dist;
    }

    public static double[][] floydWarshall(double graph[][], int V) {  // V is 0 based
        double dist[][] = new double[V][V];

        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (graph[i][j] > 0)
                    dist[i][j] = graph[i][j];
                else if (i != j)
                    dist[i][j] = Integer.MAX_VALUE;
            }
        }

        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (i != j && dist[i][k]!= Integer.MAX_VALUE && dist[k][j] != Integer.MAX_VALUE && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        return dist;
    }
    
    // chap 2.4 cow tours
    private static void test1() throws IOException {
        BufferedReader f = new BufferedReader(new FileReader("1.in"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("1.out")));
        StringTokenizer st;
        int V = Integer.parseInt(f.readLine());

        int[] x = new int[V], y = new int[V];
        for (int i = 0; i < V; i++) {
            st = new StringTokenizer(f.readLine());
            x[i] = Integer.parseInt(st.nextToken());
            y[i] = Integer.parseInt(st.nextToken());
        }

        int[][] grid = new int[V][V];
        for (int i = 0; i < V; i++) {
            String s = f.readLine();
            for (int j = 0; j < V; j++) {
                grid[i][j] = s.charAt(j) - '0';
            }
        }

        double[][] dist = new double[V][V];
        for (double[] row : dist) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (i == j) {
                    dist[i][j] = 0;
                } else if (grid[i][j] == 1) {
                    dist[i][j] = Math.sqrt((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j]));
                }
            }
        }

        dist = floydWarshall(dist, V);
        
        ConnectedComponentsAdjMatrix cc = new ConnectedComponentsAdjMatrix(grid, V);
        int[] id = new int[V];
        int cCount = cc.findCC(id);

        FieldDiameter fd = new FieldDiameter();
        double diameter = fd.calcDiameter(V, cCount, id, dist, x, y);

        String ans = String.format("%.6f", diameter);
        while (ans.length() - ans.indexOf(".") < 6) {
            ans += "0";
        }
        out.println(ans);
        System.out.println(ans);

        out.close();
    }
}

class ConnectedComponentsAdjMatrix {
    int V;
    int[][] graph;
    boolean[] visited;
    int[] id;
    int count = 1;

    public ConnectedComponentsAdjMatrix(int[][] graph, int V) {
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

class FieldDiameter {
    double calcDiameter(int V, int compCount, int[] id, double[][] dist, int[] x, int[] y) {
        double[] diameters = new double[compCount + 1];
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (id[i] == id[j]) {
                    diameters[id[i]] = Math.max(diameters[id[i]], dist[i][j]);
                }
            }
        }

        double minDiam = Integer.MAX_VALUE;
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (id[i] != id[j]) {
                    double diam1 = 0;
                    double diam2 = 0;
                    for (int k = 0; k < V; k++) {
                        if (id[k] == id[i]) {
                            diam1 = Math.max(diam1, dist[i][k]);
                        }
                    }

                    for (int k = 0; k < V; k++) {
                        if (id[k] == id[j]) {
                            diam2 = Math.max(diam2, dist[j][k]);
                        }
                    }

                    double dist_i_j = Math.sqrt((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j]));
                    double diam = Math.max(diameters[id[i]], Math.max(diameters[id[j]], diam1 + dist_i_j + diam2));
                    minDiam = Math.min(minDiam, diam);
                }
            }
        }
  
        return minDiam;
    }
}
