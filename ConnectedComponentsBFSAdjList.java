import java.io.*;
import java.util.*;

// ignore this class unless ConnectedComponentsAdjList has performance issue
public class ConnectedComponentsBFSAdjList {
    public static void main(String[] args) throws IOException {
        test1();
    }
    
    // chap 2.1 Sample, input as adjacency list
    private static void test1() throws IOException {
        BufferedReader f = new BufferedReader(new FileReader("1.in"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("1.out")));
        StringTokenizer st = new StringTokenizer(f.readLine());
        int V = Integer.parseInt(st.nextToken()); 
        int E = Integer.parseInt(st.nextToken());

        Map<Integer, ArrayList<Integer>> graph = new HashMap<>();
        for (int i = 0; i < E; i++) {
            st = new StringTokenizer(f.readLine());
            int from = Integer.parseInt(st.nextToken()) - 1;    // switch to 0-based Vertix
            int to = Integer.parseInt(st.nextToken()) - 1;
            
            ArrayList<Integer> adjListFrom = graph.get(from);
            if (adjListFrom == null) {
                adjListFrom = new ArrayList<>();
                graph.put(from, adjListFrom);
            }
            adjListFrom.add(to);
            
            ArrayList<Integer> adjListTo = graph.get(to);
            if (adjListTo == null) {
                adjListTo = new ArrayList<>();
                graph.put(to, adjListTo);
            }
            adjListTo.add(from);
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

// graph resprented by Adjacency List. i/o: V from 1 ... V, internal 0 ...V-1
class ConnectedComponents {
    int V;
    Map<Integer, ArrayList<Integer>> graph;
    boolean[] visited;
    int[] id;
    int count = 1;

    public ConnectedComponents(Map<Integer, ArrayList<Integer>> graph, int V) {
        this.graph = graph;
        this.V = V;
        this.id = new int[V];
        visited = new boolean[V];
    }

    public int findCC(int[] id) {
        for (int r = 0; r < V; r++) {
            if (!visited[r]) {
                bfs(r);
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
                bfs(r);
                count++;
            }
        }

        Set<Integer>[] comps = new HashSet[count - 1];
        for (int i = 0; i < count - 1; i++) {
            comps[i] = new HashSet<>();
        }

        for (int i = 0; i < V; i++) {
            comps[id[i] - 1].add(i + 1);    // i/o vertex starts with 1, ... V.
        }

        return comps;
    }

    private void bfs(int v) {
        Queue<Integer> q = new LinkedList<>();
        q.add(v);
        while (!q.isEmpty()) {
            int top = q.poll();
            visited[top] = true;
            id[top] = count;

            List<Integer> adjList = graph.get(top);
            if (adjList != null)
                for (Integer c : adjList) { 
                    if (!visited[c]) {
                        q.add(c);
                    }
                }
        }
    }
}

