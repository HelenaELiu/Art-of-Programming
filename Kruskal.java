import java.util.*;
import java.io.*;

// O(ElogE)
public class Kruskal {
    public static void main(String[] args)  throws IOException {
        BufferedReader f = new BufferedReader(new FileReader("1.in"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("1.out")));
        StringTokenizer st = new StringTokenizer(f.readLine());
        int V = Integer.parseInt(st.nextToken());   
        int E = Integer.parseInt(st.nextToken());  
        
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < E; i++) {
            st = new StringTokenizer(f.readLine());
            int src = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());
            int weight = Integer.parseInt(st.nextToken());
            edges.add(new Edge(src, end, weight));
        }
        
        Set<Edge> mstEdges = new HashSet<>();
        out.println("MST weight: " + MST(edges, V, mstEdges));
        out.println("MST edges: " + mstEdges);

        out.close();
    }

    static public int MST(List<Edge> edges, int V,  Set<Edge> mstEdges) {
        Collections.sort(edges);
        //Initialize singleton sets for each node in graph. (V+1) to account for arrays indexing from 0
        DisjointSet nodeSet = new DisjointSet(V + 1);		

        //String outputMessage = "";
        for (int i = 0; i < edges.size() && mstEdges.size() < (V - 1); i++) { //loop over all edges. Start @ 1 (ignore 0th as placeholder).
            Edge edge = edges.get(i);
            int root1 = nodeSet.find(edge.vertex1);
            int root2 = nodeSet.find(edge.vertex2);
            //outputMessage += "find(" + edge.vertex1 + ") returns " + root1 + ", find(" + edge.vertex2 + ") returns " + root2;
            //String unionMessage = ",\tNo union performed\n";
            if (root1 != root2) {
                mstEdges.add(edge);
                nodeSet.union(root1, root2);
                //unionMessage = ",\tUnion(" + root1 + ", " + root2 + ") done\n";
            }
            //outputMessage += unionMessage;
        }

        int mstTotalEdgeWeight = 0;
        for (Edge edge : mstEdges)
            mstTotalEdgeWeight += edge.weight;

        return mstTotalEdgeWeight;
    }
}

class Edge implements Comparable<Edge> {
    int vertex1, vertex2, weight;

    public Edge(int vertex1, int vertex2, int weight) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge otherEdge) {
        return this.weight - otherEdge.weight;
    }

    @Override
    public String toString() {
        return "Edge (" + vertex1 + ", " + vertex2 + ") weight=" + weight + "\r\n";
    }
}

class DisjointSet {
    private int[] set;

    public int[] getSet() {	
        return set;
    }

    public DisjointSet(int numElements) {
        set = new int[numElements];
        for (int i = 0; i < set.length; i++) {	//initialize to -1 so the trees have nothing in them
            set[i] = -1;
        }
    }

    public void union(int root1, int root2) {
        if (set[root2] < set[root1]) {
            set[root1] = root2;
        } else {
            if (set[root1] == set[root2]) 
                set[root1]--;
            set[root2] = root1;
        }
    }

    public int find(int x) {
        if (set[x] < 0) 
            return x;

        int next = x;
        while (set[next] > 0) {
            next = set[next];
        }
        
        return next;
    }
}