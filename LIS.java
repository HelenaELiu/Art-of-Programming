import java.util.*;

// O(N^2)
// @see also usaco\other\LISTest\Stacking - another variation of LIS
public class LIS {
    public static void main(String[] args) {
        int[] a = {3, 2, 6, 4, 5, 1};

        // should return [2, 4, 5]
        System.out.println(lisLength(a, a.length));
        System.out.println(lis(a, a.length));
    }

    public static List<Integer> lis(int[] V, int N) {
        List<Integer>[] L = new List[N];
        int longest = 0;
        int longestIndex = 0;

        for (int i = 0; i < N; i++)
            L[i] = new ArrayList();

        L[0].add(V[0]);

        for (int i = 1; i < N; i++) {
            for (int j = 0; j < i; j++) {
                //if (V[j] < V[i] && L[j].size() > L[i].size()) {
                if (V[j] < V[i] && compare(L[j], L[i])) {
                    L[i].clear();
                    L[i].addAll(L[j]);
                }
            }

            L[i].add(V[i]);

            if (L[i].size() > longest) {
                longest = L[i].size();
                longestIndex = i;
            }
        }

        return L[longestIndex];
    }

    // if asking for LCS length only, lisLength() is faster than lis()
    public static int lisLength(int[] V, int n) {
        int len[] = new int[n];
        int i, j, max = 0;

        for (i = 0; i < n; i++)
            len[i] = 1;

        for (i = 1; i < n; i++) {
            for (j = 0; j < i; j++) {
                if (V[i] > V[j] && len[i] < len[j] + 1)
                    len[i] = len[j] + 1;
            }
        }

        for (i = 0; i < n; i++)
            if (max < len[i]) max = len[i];

        return max;
    }

    private static boolean compare(List<Integer> a, List<Integer> b) {
        int size = a.size();
        if (size > b.size()) {
            return true;
        } else if (size < b.size()) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (a.get(i) < b.get(i)) {
                return true;
            }
        }

        return false;
    }
}
