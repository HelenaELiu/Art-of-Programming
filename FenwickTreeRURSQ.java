import java.io.*;
import java.util.*;

// O(logn) for range sum query and update; O(n) for tree construct
// Use two Fenwick trees to track range updates and range sum queries. 1-BASED INDEX
public class FenwickTreeRURSQ {
    private FenwickTree bit1;
    private FenwickTree bit2;
    private int size;

    public FenwickTreeRURSQ(int size) {
        this.size = size;
        // size + 1 - use 1 based index so the bit1 and bit2 will be (size+2)
        // (1 extra for 1-based indexing and another for storing cumulative sums)
        bit1 = new FenwickTree(size + 1);
        bit2 = new FenwickTree(size + 1);
    }

    public void updateRange(int leftIndex, int rightIndex, long delta) {
        bit1.update(leftIndex, delta);
        bit1.update(rightIndex + 1, -delta);
        bit2.update(leftIndex, delta * (leftIndex - 1));
        bit2.update(rightIndex + 1, -delta * rightIndex);
    }

    // return the sum of elements in range [0, rightIndex] in original array
    public long getPrefixSum(int rightIndex) {
        return bit1.getPrefixSum(rightIndex) * rightIndex - bit2.getPrefixSum(rightIndex);
    }

    public long getRangeSum(int leftIndex, int rightIndex) {
        return getPrefixSum(rightIndex) - getPrefixSum(leftIndex - 1);
    }

    public long getPointValue(int index) {
        return getPrefixSum(index);
    }

    public long[] getUpdatedArray() {
        long[] array = new long[size];
        for (int i = 1; i <= size; i++)
            array[i - 1] = getPointValue(i) - getPointValue(i - 1);

        return array;
    }
    
    // -------------------------- FenwickTreeRURSQ test START ----------------------
    static List<Integer>[] adj;
    static int[] subSize, subrootStart, subrootEnd, base;
    static int baseIdx = 0;
    
    public static void main(String[] args) throws IOException {
        BufferedReader f = new BufferedReader(new FileReader("1.in"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("1.out")));
        StringTokenizer token = new StringTokenizer(f.readLine());
        int N = Integer.parseInt(token.nextToken());
        int Q = Integer.parseInt(token.nextToken());
        int root = Integer.parseInt(token.nextToken()) - 1;    // C

        adj = new List[N];
        for (int i = 0; i < N; i++)
            adj[i] = new ArrayList<>();

        for (int i = 0; i < N - 1; i++) {
            token = new StringTokenizer(f.readLine());
            int src = Integer.parseInt(token.nextToken()) - 1;
            int dest = Integer.parseInt(token.nextToken()) - 1;
            adj[src].add(dest);
            adj[dest].add(src);
        }

        subSize = new int[N];
        subrootStart = new int[N];
        subrootEnd = new int[N];
        base = new int[N];
        dfs(root, -1);
        
        FenwickTreeRURSQ fen = new FenwickTreeRURSQ(N); 

        token = new StringTokenizer(f.readLine());
        long[] nodeVal = new long[N];
        for (int i = 0; i < N; i++) {
            nodeVal[i] = Long.parseLong(token.nextToken());
            fen.updateRange(subrootEnd[i], subrootEnd[i], nodeVal[i]); 
        }
        
        for (int i = 0; i < Q; i++) {
            token = new StringTokenizer(f.readLine());
            int oper = Integer.parseInt(token.nextToken());
            int node = Integer.parseInt(token.nextToken()) - 1;
                
            if (oper == 1) {
                long ans = fen.getRangeSum(subrootStart[node], subrootEnd[node]);
                out.println(ans);
            } else if (oper == 2) {
                long delta = Long.parseLong(token.nextToken());
                fen.updateRange(subrootStart[node], subrootEnd[node], delta);
            }
        }

        out.close();
    }
    
    private static void dfs(int u, int par) {
        subSize[u] = 1;
        subrootStart[u] = baseIdx + 1;
        for (int v : adj[u]) {
            if (v != par) {
                dfs(v, u);
                subSize[u] += subSize[v];
            }
        }
                       
        base[baseIdx] = u;
        baseIdx++;
        subrootEnd[u] = baseIdx;
    }
    // -------------------------- FenwickTreeRURSQ test END ----------------------
}

class FenwickTree {
    long[] fenwickTree;
    int treeSize;

    public FenwickTree(int size) {
        this.treeSize = size + 1;
        this.fenwickTree = new long[treeSize];
    }

    public FenwickTree(long[] array) {
        this.treeSize = array.length + 1;
        this.fenwickTree = new long[treeSize];

        for (int i = 1; i < treeSize; i++)
            update(i, array[i - 1]);
    }

    // if want to update from 2 to 8, use the delta (8 - 2) as 'delta'
    public void update(int index, long delta) {
        while (index < treeSize) {
            fenwickTree[index] += delta;
            index += index & (-index);  // find the next node
        }
    }

    // return the sum of the range [0, rightIndex]
    public long getPrefixSum(int rightIndex) {
        long sum = 0;
        while (rightIndex > 0) {
            sum += fenwickTree[rightIndex];
            rightIndex -= rightIndex & (-rightIndex);   // find the parent node
        }

        return sum;
    }

    public long getRangeSum(int leftIndex, int rightIndex) {
        return getPrefixSum(rightIndex) - getPrefixSum(leftIndex - 1);
    }

    public int size() {
        return fenwickTree.length - 1;
    }
}

// Range Update & Point Query. 1-BASED INDEX. Example:
// FenwickTreeRUPQ bit = new FenwickTreeRUPQ(10);
// bit.updateRange(3, 7, 1);    // [0, 0, 1, 1, 1, 1, 1, 0, 0, 0]
// bit.updateRange(1, 10, -1);  // [-1, -1, 0, 0, 0, 0, 0, -1, -1, -1]
// bit.updateRange(4, 8, 5);    // [-1, -1, 0, 5, 5, 5, 5, 4, -1, -1]
// bit.getPointValue(3);        // 0
// bit.getPointValue(7);        // 5
// bit.getPointValue(10);       // -1
class FenwickTreeRUPQ extends FenwickTree {
    private int size;

    public FenwickTreeRUPQ(int size) {
        // we are updating (rightIndex + 1), so treeSize needs to be more than that. 
        // When initializing the FenwickTree, pass size as (original size + 1)
        super(size + 1);
        this.size = size;
    }

    public void updateRange(int leftIndex, int rightIndex, long delta) {
        update(leftIndex, delta);
        update(rightIndex + 1, -delta);
    }

    // return the value at index, i.e., array[index], not prefix sum although calls getPrefixSum
    public long getPointValue(int index) {
        return getPrefixSum(index);
    }

    // return the updated array by calculating cumulative sums
    public long[] getUpdatedArray() {
        long[] array = new long[size];
        for (int i = 1; i <= size; i++)
            array[i - 1] = getPointValue(i);

        return array;
    }
}
