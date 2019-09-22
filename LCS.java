// O(m * n) - m, n are the length of the two strings respectively. Longest commmon subsequence
public class LCS { 
    public static void main(String[] args) {
        // should return GTAB
        System.out.println(lcsLength("AGGTAB", "GXTXAYB"));
        System.out.println(lcs("AGGTAB", "GXTXAYB"));
        
        // should return "1234"
        System.out.println(lcs("1234", "1224533324"));

        // should return "tsitest":
        System.out.println(lcs("thisisatest", "testing123testing"));
    }
    
    // Returns length of LCS for X[0..m-1], Y[0..n-1] 
    public static int lcsLength(String X, String Y) {
        int m = X.length(), n = Y.length();
        int[][] L = lcsDP(X, Y, m, n);
        
        return L[m][n];
    }
    
    // Returns LCS for X[0..m-1], Y[0..n-1] 
    public static String lcs(String X, String Y) {
        int m = X.length(), n = Y.length();
        int[][] L = lcsDP(X, Y, m, n);   
        int index = L[m][n];
        char[] lcs = new char[index + 1];

        // Start from the right-most-bottom-most corner
        int i = m, j = n;
        while (i > 0 && j > 0) {
            if (X.charAt(i - 1) == Y.charAt(j - 1)) {
                lcs[index - 1] = X.charAt(i - 1);

                i--;
                j--;
                index--;
            } else if (L[i - 1][j] > L[i][j - 1]) { 
                i--;  //If not same,  go in the direction of larger value 
            } else {
                j--;
            }
        }
        
        return new String(lcs);
    }
    
    private static int[][] lcsDP(String X, String Y, int m, int n) {
        int[][] L = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0 || j == 0) {
                    L[i][j] = 0;
                } else if (X.charAt(i - 1) == Y.charAt(j - 1)) {
                    L[i][j] = L[i - 1][j - 1] + 1;
                } else {
                    L[i][j] = Math.max(L[i - 1][j], L[i][j - 1]);
                }
            }
        }
        
        return L;
    }
}
