import java.util.*;

// Suffix Array in O(N * logN) and LCP in O(N)
public class SuffixArray {
    // sort suffixes of S in O(n*log(n))
    public static int[] suffixArray(CharSequence S) {
        int n = S.length();
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++)
            order[i] = n - 1 - i;

        // stable sort of characters
        Arrays.sort(order, (a, b) -> Character.compare(S.charAt(a), S.charAt(b)));

        int[] sa = new int[n];
        int[] classes = new int[n];
        for (int i = 0; i < n; i++) {
            sa[i] = order[i];           // suffix on i'th position after sorting by first len characters
            classes[i] = S.charAt(i);   // equivalence class of the i'th suffix after sorting by first len characters
        }
       
        for (int len = 1; len < n; len *= 2) {
            int[] c = classes.clone();
            for (int i = 0; i < n; i++) {
                // condition sa[i - 1] + len < n simulates 0-symbol at the end of the string
                // a separate class is created for each suffix followed by simulated 0-symbol
                classes[sa[i]] = i > 0 && c[sa[i - 1]] == c[sa[i]] && sa[i - 1] + len < n && c[sa[i - 1] + len / 2] == c[sa[i] + len / 2] ? classes[sa[i - 1]] : i;
            }
            
            // Suffixes already sorted by first len characters, now sort suffixes by first len * 2 characters
            int[] cnt = new int[n];
            for (int i = 0; i < n; i++)
                cnt[i] = i;

            int[] s = sa.clone();
            for (int i = 0; i < n; i++) {
                // text[i] - order of suffixes sorted by first len characters
                // (text[i] - len) - order of suffixes sorted only by second len characters
                int s1 = s[i] - len;
                if (s1 >= 0)    // sort only suffixes of length > len, others are already sorted
                    sa[cnt[classes[s1]]++] = s1;
            }
        }
        
        return sa;
    }

    // longest common prefixes array in O(n)
    public static int[] lcp(int[] sa, CharSequence s) {
        int n = sa.length;
        int[] rank = new int[n];
        for (int i = 0; i < n; i++)
            rank[sa[i]] = i;
        
        int[] lcp = new int[n - 1];
        for (int i = 0, h = 0; i < n; i++) {
            if (rank[i] < n - 1) {
                for (int j = sa[rank[i] + 1]; Math.max(i, j) + h < s.length() && s.charAt(i + h) == s.charAt(j + h); ++h);
                lcp[rank[i]] = h;
                if (h > 0) --h;
            }
        }
        
        return lcp;
    }

    public static void main(String[] args) {
        String text = "abcab";
        int[] sa = suffixArray(text);
        print(text, sa, lcp(sa, text));
        System.out.println();
        
        // another test
        text = "abcbbccaabcaabc";
        sa = suffixArray(text);
        print(text, sa, lcp(sa, text));
    }
    
    public static void print(String text, int[] sa, int[] lcp) {
        int n = sa.length - 1;
        System.out.println("------i------SA------LCP---Suffix");
        for (int i = 0; i < n; i++)
            System.out.printf("%7d %7d %7d    %s\n", i, sa[i], lcp[i], text.substring(sa[i]));
        System.out.printf("%7d %7d %7d    %s\n", n, sa[n], 0, text.substring(sa[n]));
    }
}
