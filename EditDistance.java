// O(N^2), DP 
public class EditDistance {
    public static int getEditDistance(String a, String b) {
        int m = a.length();
        int n = b.length();
        int[][] len = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++)
            len[i][0] = i;

        for (int j = 0; j <= n; j++)
            len[0][j] = j;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++)
                if (a.charAt(i) == b.charAt(j))
                    len[i + 1][j + 1] = len[i][j];
                else
                    len[i + 1][j + 1] = 1 + Math.min(len[i][j], Math.min(len[i + 1][j], len[i][j + 1]));
        }
        
        return len[m][n];
    }

    public static void main(String[] args) {
        String a = "sunday";
        String b = "saturday";
        System.out.println(getEditDistance(a, b));  // 3 = replace 'n' with 'r', insert t, insert a
    }
}
