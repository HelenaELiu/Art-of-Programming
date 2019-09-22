import java.util.*;

// 0-1 knapsack. O(nW)
public class Knapsack { 
    public static void main(String args[]) {
        // test 1. note item does not need sorted to begin with
        int val[] = new int[]{60, 100, 120};    
        int wt[] = new int[] {10, 20, 30};
        int W = 50;
        System.out.println(knapsack(W, wt, val, val.length));   // 220
        System.out.println(knapsacked(W, wt, val, val.length)); // [2, 3]
        
        // test 2
        val = new int[] {1, 4, 5, 7};
        wt = new int[] {1, 3, 4, 5};
        W = 7;
        System.out.println(knapsack(W, wt, val, val.length));   // 9
        System.out.println(knapsacked(W, wt, val, val.length)); // [2, 3]
    }
    
    // return the maximum value that can be put in a knapsack of capacity W 
    public static int knapsack(int W, int wt[], int val[], int n) {
        int dp[][] = knapsackDP(W, wt, val, n);

        return dp[n][W];
    }
    
    // return the items' indexes (1 based) in the knapsack: i/o switch to 1 based
    public static Set<Integer> knapsacked(int W, int wt[], int val[], int n) {
        int dp[][] = knapsackDP(W, wt, val, n);
        int v = dp[n][W]; 
  
        int w = W; 
        Set<Integer> bag = new HashSet<>();
        for (int i = n; i > 0 && v > 0; i--) { 
            // either the result comes from the top =K[i-1][w]) (not take)  
            // or from (val[i-1] + K[i-1][w-wt[i-1]]) (take) knapsack table 
            if (v != dp[i - 1][w]) { 
                bag.add((i - 1) + 1);   // included i-1 (0 based) but +1 to switch to 1 - based
                v = v - val[i - 1]; 
                w = w - wt[i - 1]; 
            } 
        } 
        
        return bag;
    }
    
    private static int[][] knapsackDP(int W, int wt[], int val[], int n) {
        int dp[][] = new int[n + 1][W + 1];

        for (int i = 0; i <= n; i++) {
            for (int w = 0; w <= W; w++) {
                if (i == 0 || w == 0) {
                    dp[i][w] = 0;
                } else if (wt[i - 1] <= w) {
                    dp[i][w] = Math.max(val[i - 1] + dp[i - 1][w - wt[i - 1]], dp[i - 1][w]);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        return dp;
    }
} 
