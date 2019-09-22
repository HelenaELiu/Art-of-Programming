import java.io.*;
import java.util.*;

// O(V + E). DFS Kosaraju's Strongly connected components
public class SCCKosaraju {
    public static List<List<Integer>> scc(List<Integer>[] graph) {
        int n = graph.length;
        boolean[] used = new boolean[n];
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!used[i])
                dfs(graph, used, order, i);
        }

        List<Integer>[] reverseGraph = new List[n];
        for (int i = 0; i < n; i++)
            reverseGraph[i] = new ArrayList<>();
        
        for (int i = 0; i < n; i++) {
            for (int j : graph[i])
                reverseGraph[j].add(i);
        }

        List<List<Integer>> components = new ArrayList<>();
        Arrays.fill(used, false);
        Collections.reverse(order);

        for (int u : order) {
            if (!used[u]) {
                List<Integer> component = new ArrayList<>();
                dfs(reverseGraph, used, component, u);
                components.add(component);
            }
        }

        return components;
    }

    private static void dfs(List<Integer>[] graph, boolean[] used, List<Integer> res, int u) {
        used[u] = true;
        for (int v : graph[u]) {
            if (!used[v])
                dfs(graph, used, res, v);
        }
        res.add(u);
    }

    public static List<Integer>[] createGraph(int V) {
        List<Integer>[] graph = new List[V];
        for (int i = 0; i < V; i++) {
            graph[i] = new ArrayList<>();
        }

        return graph;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader f = new BufferedReader(new FileReader("6.in"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("6.out")));
        StringTokenizer st = new StringTokenizer(f.readLine());
        int V = Integer.parseInt(st.nextToken());
        int E = Integer.parseInt(st.nextToken());
        List<Integer>[] graph = createGraph(V);

        for (int i = 0; i < E; i++) {
            st = new StringTokenizer(f.readLine());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            graph[from].add(to);
        }

        out.println(scc(graph));
        out.close();
    }
}