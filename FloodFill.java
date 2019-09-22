import java.io.*;
import java.util.*;

// Complex flood fill
// @see also Chap 2.1 Castle- a variation of flood fill with Walls between pixels
public class FloodFill {
    public static void main(String[] args) throws IOException {
        test1();      // fully test with 10 test cases
        //test2();        // tested with only y test cases
    }

    // 2019 Jan USACO silver: mooyomooyo
    private static void test1() throws IOException {
        BufferedReader f = new BufferedReader(new FileReader("10.in"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("10-1.out")));
        StringTokenizer st = new StringTokenizer(f.readLine());
        int N = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());

        int[][] graph = new int[N][10];
        for (int i = 0; i < N; i++) {
            String s = f.readLine();
            for (int j = 0; j < 10; j++) {
                graph[i][j] = s.charAt(j) - '0';
            }
        }

        boolean[] stop = new boolean[1];
        do {
            stop[0] = true;
            FillConnectedComponents cc = new FillConnectedComponents(graph, N, 10);
            graph = cc.fill(K, 0, stop, 0);
            gravity(graph, N);
        } while (!stop[0]);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < 10; j++)
                out.print(graph[i][j]);
            out.println();
        }

        out.close();
    }
    
    private static void gravity(int[][] grid, int N) {
        for (int i = 0; i < 10; i++) { // for each column
            int numZeros = 0;
            for (int j = N - 1; j >= 0; j--) { // for each row, starting from bottom
                if (grid[j][i] == 0) {
                    numZeros++;
                } else if (numZeros != 0) { // if it does have some room to drop, then drop
                    grid[j + numZeros][i] = grid[j][i];
                    grid[j][i] = 0;
                }
            }
        }
    }
}

// graph resprented by Matrix. internal row/col from 0
class FillConnectedComponents {
    int M, N;   // num of rows, cols
    int[][] graph;
    boolean[][] visited;
    int[][] id;
    int count = 1;

    public FillConnectedComponents(int[][] graph, int M, int N) {
        this.graph = graph;
        this.M = M;
        this.N = N;
        this.id = new int[M][N];
        visited = new boolean[M][N];
    }

    public int findCC(int[][] id) {
        for (int r = 0; r < M; r++) {
            for (int c = 0; c < N; c++) {
                if (!visited[r][c]) {
                    dfs(r, c, graph[r][c]);
                    count++;
                }
            }
        }

        for (int r = 0; r < M; r++)
            for (int c = 0; c < N; c++)
                id[r][c] = this.id[r][c];

        return count - 1;
    }

    public Component[] findCC() {
        for (int r = 0; r < M; r++) {
            for (int c = 0; c < N; c++) {
                if (!visited[r][c]) {
                    dfs(r, c, graph[r][c]);
                    count++;
                }
            }
        }

        Set<Pixel>[] psets = new HashSet[count - 1];
        Component[] comps = new Component[count - 1];
        for (int i = 0; i < count - 1; i++) {
            psets[i] = new HashSet<>();
            comps[i] = new Component(psets[i]);
        }

        for (int r = 0; r < M; r++)
            for (int c = 0; c < N; c++)
                psets[id[r][c] - 1].add(new Pixel(r, c)); // or Pair(r+1, c+1)
        
        for (Component comp : comps) {
            Set<Pixel> pset = comp.pixels;
            Pixel p = pset.iterator().next();
            comp.color = graph[p.x][p.y];
        }
        
        return comps;
    }

    public int[][] fill(int k, int newColor, boolean[] stop, int xColor) {  //xColor - exclude color
        Component[] comps = findCC();

        for (Component comp : comps) {
            Set<Pixel> pset = comp.pixels;
            if (comp.color != xColor && pset.size() >= k) {
                stop[0] = false;
                for (Pixel p : pset) {
                    graph[p.x][p.y] = newColor;
                }
            }
        }

        return graph;
    }

    private void dfs(int x, int y, int color) {
        if (x < 0 || x >= M || y < 0 || y >= N || visited[x][y] || graph[x][y] != color) {
            return;
        }

        visited[x][y] = true;
        id[x][y] = count;
        dfs(x + 1, y, color);
        dfs(x - 1, y, color);
        dfs(x, y + 1, color);
        dfs(x, y - 1, color);
    }
}

class Pixel {
    int x, y;

    public Pixel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

class Component {
    Set<Pixel> pixels;
    int color;
    
    public Component(Set<Pixel> pixels) {
        this.pixels = pixels;
    }
}