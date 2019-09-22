import java.io.*;
import java.util.*;

// O(V + E). DFS Tarjan's Strongly connected components
public class SCCTarjan {
    List<Integer>[] graph;
    boolean[] visited;
    Stack<Integer> stack;
    int time;
    int[] lowlink;
    List<List<Integer>> components;

    public List<List<Integer>> scc(List<Integer>[] graph) {
        int n = graph.length;
        this.graph = graph;
        visited = new boolean[n];
        stack = new Stack<>();
        time = 0;
        lowlink = new int[n];
        components = new ArrayList<>();

        for (int u = 0; u < n; u++) {
            if (!visited[u])
                dfs(u);
        }

        return components;
    }

    private void dfs(int u) {
        lowlink[u] = time++;
        visited[u] = true;
        stack.add(u);
        boolean isComponentRoot = true;

        for (int v : graph[u]) {
            if (!visited[v])
                dfs(v);

            if (lowlink[u] > lowlink[v]) {
                lowlink[u] = lowlink[v];
                isComponentRoot = false;
            }
        }

        if (isComponentRoot) {
            List<Integer> component = new ArrayList<>();
            while (true) {
                int x = stack.pop();
                component.add(x);
                lowlink[x] = Integer.MAX_VALUE;
                if (x == u)
                    break;
            }
            components.add(component);
        }
    }

    public List<Integer>[] createGraph(int V) {
        List<Integer>[] graph = new List[V];
        for (int i = 0; i < V; i++)
            graph[i] = new ArrayList<>();

        return graph;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader f = new BufferedReader(new FileReader("1.in"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("1.out")));
        StringTokenizer st = new StringTokenizer(f.readLine());
        int V = Integer.parseInt(st.nextToken());
        int E = Integer.parseInt(st.nextToken());
        
        SCCTarjan components = new SCCTarjan();
        List<Integer>[] graph = components.createGraph(V);

        for (int i = 0; i < E; i++) {
            st = new StringTokenizer(f.readLine());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            graph[from].add(to);
        }

        out.println(components.scc(graph));
        out.close();
    }
}