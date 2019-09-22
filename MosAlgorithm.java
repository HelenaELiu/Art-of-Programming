import java.util.Arrays;

// Queries: given L and R, find the number of distinct values in [L, R]
public class MosAlgorithm {
    public static class Query {
        int index;
        int a, b;

        public Query(int a, int b) {
            this.a = a;
            this.b = b;
        }
    }

    static int add(int[] input, int[] cnt, int i) {
        return ++cnt[input[i]] == 1 ? 1 : 0;    // == 1: new instance of val?
    }

    static int remove(int[] input, int[] cnt, int i) {
        return --cnt[input[i]] == 0 ? -1 : 0;   // == 0: last instance of val?
    }

    public static int[] processQueries(int[] input, Query[] queries) {
        for (int i = 0; i < queries.length; i++)
            queries[i].index = i;

        int sqrtn = (int) Math.sqrt(input.length);  // = length of input block
        Arrays.sort(queries, (q1, q2) -> {
            int cmp = Integer.compare(q1.a / sqrtn, q2.a / sqrtn);
            return cmp != 0 ? cmp : Integer.compare(q1.b, q2.b);
        });
        
        int[] cnt = new int[1000002];  // = frequency of value i in the current range
        int[] res = new int[queries.length];
        int L = 1, R = 0;
        int ans = 0;        // answer to the current query(input, b)
        for (Query query : queries) {
            while (L < query.a) ans += remove(input, cnt, L++);
            while (L > query.a) ans += add(input, cnt, --L);
            while (R < query.b) ans += add(input, cnt, ++R);
            while (R > query.b) ans += remove(input, cnt, R--);

            res[query.index] = ans;
        }
        
        return res;
    }

    public static void main(String[] args) {
        test1();
        test2();
    }

    public static void test1() {
        int[] input = {1, 3, 3, 4};
        Query[] queries = {new Query(0, 3), new Query(1, 3), new Query(2, 3), new Query(3, 3)};
        int[] res = processQueries(input, queries);
        System.out.println(Arrays.toString(res));
    }

    public static void test2() {
        int[] input = {1, 1, 2, 1, 3};
        Query[] queries = {new Query(0, 4), new Query(1, 3), new Query(2, 4)};
        int[] res = processQueries(input, queries);
        System.out.println(Arrays.toString(res));
    }
}