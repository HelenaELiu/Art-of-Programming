// O(n + mR) = O(n), R - alphabet size
// KMP finds the first occurrence of a pattern string in a text string
public class KMP {
    private final int R;        // the radix
    private final int[][] dfa;  // the KMP automoton
    private final String pat;   // the pattern

    public KMP(String pat) {
        this.R = 256;
        this.pat = pat;

        // build DFA from pattern
        int m = pat.length();
        dfa = new int[R][m];
        dfa[pat.charAt(0)][0] = 1;
        for (int x = 0, j = 1; j < m; j++) {
            for (int c = 0; c < R; c++)
                dfa[c][j] = dfa[c][x];      // copy mismatch cases 

            dfa[pat.charAt(j)][j] = j + 1;  // set match case
            x = dfa[pat.charAt(j)][x];      // update restart state
        }
    }

    // returns the index of the first occurrrence of the pattern string in the text
    public int search(String txt) {
        // simulate operation of DFA on text
        int m = pat.length();
        int n = txt.length();
        int i, j;
        for (i = 0, j = 0; i < n && j < m; i++)
            j = dfa[txt.charAt(i)][j];

        if (j == m) return i - m;   // found
        return -1;                  // not found
    }

    public static void main(String[] args) {
        //abracadabra abacadabrabracabracadabrabrabracad
        //rab abacadabrabracabracadabrabrabracad
        //bcara abacadabrabracabracadabrabrabracad
        //rabrabracad abacadabrabracabracadabrabrabracad 
        //abacad abacadabrabracabracadabrabrabracad
        String pat = "abracadabra";
        String txt = "abacadabrabracabracadabrabrabracad";

        KMP kmp = new KMP(pat);
        int offset = kmp.search(txt);
        if (offset == -1) offset = txt.length(); // just for print out formatting

        System.out.println("text:    " + txt);
        System.out.print("pattern: ");
        for (int i = 0; i < offset; i++) {
            System.out.print(" ");
        }
        System.out.println(pat);
    }
}