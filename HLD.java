import java.io.*;
import java.util.*;

public class HLD {
    static int N, nodeVal[];
    static List<Integer>[] adj;
    
    public static void main(String[] args) throws IOException {
        BufferedReader f = new BufferedReader(new FileReader("2.in"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("2.out")));
        StringTokenizer token = new StringTokenizer(f.readLine());
        N = Integer.parseInt(token.nextToken());
        int Q = Integer.parseInt(token.nextToken());
        int root = Integer.parseInt(token.nextToken()) - 1;

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
        
        token = new StringTokenizer(f.readLine());
        nodeVal = new int[N];
        for (int i = 0; i < N; i++)
            nodeVal[i] = Integer.parseInt(token.nextToken());
        
        executeHLD(root);
        
        for (int i = 0; i < Q; i++) {
            token = new StringTokenizer(f.readLine());
            int oper = Integer.parseInt(token.nextToken());

            if (oper == 1) {
                int nodeA = Integer.parseInt(token.nextToken()) - 1;
                int nodeB = Integer.parseInt(token.nextToken()) - 1;
                out.println(query(nodeA, nodeB));
            } else if (oper == 2) { // assume + delta, not replace with new value
                int node = Integer.parseInt(token.nextToken()) - 1;
                int val = Integer.parseInt(token.nextToken());
                updateDelta(node, val);      // use update() for replace with value
            }
        }
        
        out.close();
    }

    static void update(int node, int val) {
        //st.updatePoint(segIdx[node], nodeVal[node] + val);        
        st.updatePoint(segIdx[node], val);
        nodeVal[node] = val;
    }
    
    static void updateDelta(int node, int val) {
        //st.updatePoint(segIdx[node], nodeVal[node] + val);        
        st.updatePointDelta(segIdx[node], val);
        nodeVal[node] = val;
    }

    static int query(int u, int v) {
        int lca = lca_query(u, v);
        return query_up(u, lca) + query_up(v, lca) - nodeVal[lca];
    }

    static int query_up(int v, int u) {
        int uChain = chainIdx[u], vChain = chainIdx[v], ans = 0;

        while (uChain != vChain) {
            ans += st.query(segIdx[chainHead[vChain]], segIdx[v]);
            v = ancestor[chainHead[vChain]][0];
            vChain = chainIdx[v];
        }
        ans += st.query(segIdx[u], segIdx[v]);
        return ans;
    }

    static SegmentTree st;

    static void executeHLD(int root) {
        int k = (int) (Math.floor(Math.log(N) / Math.log(2))) + 1;
        ancestor = new int[N][k];
        for (int i = 0; i < N; i++)
            Arrays.fill(ancestor[i], -1);

        subSize = new int[N];
        level = new int[N];
        ancestor[root][0] = -1;
        dfs(root, -1, 0);

        //prepare for LCA
        for (int j = 1; j < k; j++) {
            for (int i = 1; i < N; i++)
                if (ancestor[i][j - 1] != -1)
                    ancestor[i][j] = ancestor[ancestor[i][j - 1]][j - 1];
        }

        //HL decompose
        chainNo = 0;
        sIdx = 0;
        chainHead = new int[N];
        chainPos = new int[N];
        chainIdx = new int[N];
        chainSize = new int[N];
        segIdx = new int[N];
        Arrays.fill(chainHead, -1);
        hld(root);

        //create segment tree
        int n = (int) Math.pow(2, Math.ceil(Math.log(N) / Math.log(2)));
        int[] in = new int[n + 1];
        for (int i = 0; i < N; i++) {
            in[segIdx[i]] = nodeVal[i];
        }

        st = new SegmentTree(in);
    }

    static int[][] ancestor;
    static int[] subSize, level;

    static void dfs(int u, int parent, int depth) {
        level[u] = depth;
        subSize[u] = 1;
        for (int v : adj[u]) {
            if (v != parent) {
                ancestor[v][0] = u;
                dfs(v, u, depth + 1);
                subSize[u] += subSize[v];
            }
        }
    }

    static int chainNo, sIdx;
    static int[] chainHead, chainPos, chainIdx, chainSize, segIdx;

    static void hld(int cur) {
        if (chainHead[chainNo] == -1)
            chainHead[chainNo] = cur;

        chainIdx[cur] = chainNo;
        chainPos[cur] = chainSize[chainNo]++;
        segIdx[cur] = ++sIdx;

        int nxt = -1, maxSize = -1;
        for (int v : adj[cur]) {
            if (ancestor[cur][0] != v && subSize[v] > maxSize) {
                maxSize = subSize[v];
                nxt = v;
            }
        }
        
        if (nxt != -1) hld(nxt);

        for (int v : adj[cur]) {
            if (ancestor[cur][0] != v && v != nxt) {
                chainNo++;
                hld(v);
            }
        }
    }

    static int lca_query(int p, int q) {
        int tmp, log, i;

        // if p is situated on a higher level than q then swap them
        if (level[p] < level[q]) {
            tmp = p;
            p = q;
            q = tmp;
        }

        for (log = 1; 1 << log <= level[p]; log++); // compute [log(level[p)]
        log--;

        for (i = log; i >= 0; i--) { // find the ancestor of node p at the same level
            if (level[p] - (1 << i) >= level[q])
                p = ancestor[p][i];
        }
        if (p == q) return p;

        for (i = log; i >= 0; i--) { // compute LCA(p, q) using the values in ancestor
            if (ancestor[p][i] != -1 && ancestor[p][i] != ancestor[q][i]) {
                p = ancestor[p][i];
                q = ancestor[q][i];
            }
        }

        return ancestor[p][0];
    }

    static class SegmentTree {
        int N;
        int[] array, sTree;

        SegmentTree(int[] in) {
            array = in;
            N = in.length - 1;
            sTree = new int[N << 1];
            build(1, 1, N);
        }

        void build(int node, int b, int e) {
            if (b == e) {
                sTree[node] = array[b];
            } else {
                build(node << 1, b, (b + e) / 2);
                build((node << 1) + 1, (b + e) / 2 + 1, e);
                sTree[node] = sTree[node << 1] + sTree[(node << 1) + 1];
            }
        }

        void updatePoint(int index, int val) {
            index += N - 1;
            sTree[index] = val;
            while (index > 1) {
                index >>= 1;
                sTree[index] = sTree[index << 1] + sTree[(index << 1) + 1];
            }
        }
        
        void updatePointDelta(int index, int val) {
            index += N - 1;
            sTree[index] = sTree[index] + val;
            while (index > 1) {
                index >>= 1;
                sTree[index] = sTree[index << 1] + sTree[(index << 1) + 1];
            }
        }

        int query(int i, int j) {
            return query(1, 1, N, i, j);
        }

        int query(int node, int b, int e, int i, int j) {
            if (i > e || j < b) {
                return 0;
            }
            if (b >= i && e <= j) {
                return sTree[node];
            }
            return query(node << 1, b, (b + e) / 2, i, j) + query((node << 1) + 1, (b + e) / 2 + 1, e, i, j);
        }
    }
}