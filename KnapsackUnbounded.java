import java.io.*;
import java.util.*;

// Unbound knapsack - items can be repeated. O(nW).
public class KnapsackUnbounded {
    public static void main(String args[]) throws IOException {
        // test 1. note item does not need sorted to begin with
        int val[] = new int[] {10, 30, 20};    
        int wt[] = new int[] {5, 10, 15};
        int W = 100;
        System.out.println(knapsack(W, wt, val, val.length));   // 300
        int[] instances = knapsacked(W, wt, val, val.length);
        for (int i = 0; i < val.length; i++ )
            System.out.println(val[i] + " * " + instances[i]); // [0, 10, 0]

        // test 2
        val = new int[] {10, 40, 50, 70};
        wt = new int[] {1, 3, 4, 5};
        W = 8;
        System.out.println(knapsack(W, wt, val, val.length));   // 110
        instances = knapsacked(W, wt, val, val.length);
        for (int i = 0; i < val.length; i++ )
            System.out.println(val[i] + " * " + instances[i]); // [0, 1, 0, 1]
        
        // chap 3.1 Score Inflation
        test3();        // fully tested with 12 test cases
    }
    
    // return the maximum value that can be put in a knapsack of capacity W 
    public static int knapsack(int W, int wt[], int val[], int n) {
        int dp[] = new int[W + 1];  // dp[i] stores maximum value with capacity i
          
        for(int i = 0; i <= W; i++){ 
            for(int j = 0; j < n; j++){ 
                if(wt[j] <= i){ 
                    dp[i] = Math.max(dp[i], dp[i - wt[j]] +  val[j]); 
                } 
            } 
        }

        return dp[W];
    }
    
    // return the number of instances used in the knapsack: 0-based
    public static int[] knapsacked(int W, int wt[], int val[], int n) {
        int[] items = new int[W + 1];
        knapsackDP(W, wt, val, n, items);

        int w = W;
        int[] instances = new int[n];

        // compute the no. of instances used for each selecnted item(weight)
        while (w >= 0) {
            int x = items[w];
            if (x == -1)
                break;

            instances[x] += 1;
            w -= wt[items[w]];
        }

        return instances;
    }
    
    private static int[] knapsackDP(int W, int wt[], int val[], int n, int[] baggedItems) {
        int dp[] = new int[W + 1];  // dp[i] stores maximum value with capacity i
        baggedItems[0] = -1;

        for (int i = 1; i <= W; i++) {
            int max = dp[i - 1];
            for (int j = 0; j < n; j++) {
                int x = i - wt[j];
                if (x >= 0 && (dp[x] + val[j]) > max) {
                    max = dp[x] + val[j];
                    baggedItems[i] = j;
                }
                dp[i] = max;
            }
        }

        return dp;
    }
    
    // chap 3.1 Score Inflate
    private static void test3() throws IOException  {
        BufferedReader f = new BufferedReader(new FileReader("12.in"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("12.out")));
        StringTokenizer st = new StringTokenizer(f.readLine());
        int M = Integer.parseInt(st.nextToken());   // same as weight
        int N = Integer.parseInt(st.nextToken());   // same as number of knapsack items

        int[]   val = new int[N], wt = new int[N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(f.readLine());
            val[i] = Integer.parseInt(st.nextToken());   
            wt[i] = Integer.parseInt(st.nextToken());   
        }        
        
        System.out.println(knapsack(M, wt, val, N)); 
        out.println(knapsack(M, wt, val, N)); 

        out.close();
    }
} 
