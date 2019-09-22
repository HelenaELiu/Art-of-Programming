import java.io.*;
import java.util.*;

// O((N + Q) LongN). 2 power n Jump algorithm
public class LCA {
    static int TREE_HEIGHT = 16;    // = 2 power 16
    static int[][] ancestor;
    static int[] parent, depth;
    static LinkedList<Integer>[] edges;

    public static void main(String[] args) throws IOException { // 2019 Feb Gold P3 Paint Barn
        BufferedReader f = new BufferedReader(new FileReader("cowland.in"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("cowland.out")));
        StringTokenizer st = new StringTokenizer(f.readLine());
        int N = Integer.parseInt(st.nextToken());
        int Q = Integer.parseInt(st.nextToken());

        parent = new int[N + 1];
        Arrays.fill(parent, -1);
        parent[0] = 0;
        parent[1] = 0;
        depth = new int[N + 1];
        ancestor = new int[N + 1][TREE_HEIGHT + 1];
        edges = new LinkedList[N + 1];

        for (int i = 0; i < edges.length; i++) {
            edges[i] = new LinkedList<>();
        }

        int[] e = new int[N + 1];
        st = new StringTokenizer(f.readLine());
        for (int i = 1; i <= N; i++) {
            e[i] = Integer.parseInt(st.nextToken());
        }

        for (int j = 1; j < N; j++) {
            st = new StringTokenizer(f.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            edges[a].add(b);
            edges[b].add(a);
        }

        bfs();
        genLCA();

        for (int i = 0; i < Q; i++) {
            st = new StringTokenizer(f.readLine());
            int oper = Integer.parseInt(st.nextToken());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());

            if (oper == 1) {
                e[x] = y;
            } else {
                int lca = lca(x, y);
                int ans = e[x];
                while (x != lca) {
                    x = getParent(x, depth[x] - 1);
                    ans = ans ^ e[x];
                }
                ans = ans ^ e[lca];

                ans = ans ^ e[y];
                while (y != lca) {
                    y = getParent(y, depth[y] - 1);
                    ans = ans ^ e[y];
                }
                
                out.println(ans);
            }
        }

        out.close();
    }
    
    public static int lca(int a, int b) {
        if (depth[a] > depth[b]) {
            return lca(b, a);
        } else {
            if (depth[a] < depth[b]) {
                b = getParent(b, depth[a]);
            }

            for (int k = TREE_HEIGHT; k > 0; k--) {
                while (ancestor[a][k] != ancestor[b][k]) {
                    a = ancestor[a][k];
                    b = ancestor[b][k];
                }
            }

            while (a != b) {
                a = parent[a];
                b = parent[b];
            }

            return a;
        }
    }

    public static void bfs() {
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(1);

        while (!queue.isEmpty()) {
            int at = queue.removeFirst();
            for (int child : edges[at]) {
                if (parent[child] == -1) {
                    parent[child] = at;
                    depth[child] = 1 + depth[at];
                    queue.add(child);
                }
            }
        }
    }

    public static void genLCA() {
        for (int i = 1; i < parent.length; i++) {
            ancestor[i][0] = parent[i];
        }

        for (int j = 1; j < ancestor[0].length; j++) {
            for (int i = 1; i < parent.length; i++) {
                ancestor[i][j] = ancestor[ancestor[i][j - 1]][j - 1];
            }
        }
    }

    public static int getParent(int node, int depthLvL) {
        for (int k = TREE_HEIGHT; depth[node] != depthLvL; k--) {
            while (depth[node] - (1 << k) >= depthLvL) {
                node = ancestor[node][k];
            }
        }

        return node;
    }
}