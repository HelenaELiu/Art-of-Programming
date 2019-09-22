import java.io.*;
import java.util.StringTokenizer;

public class Exponent {
    //   (x^y)%p in O(log y) 
    static long power(int b, int e, int p) {
        long x = 1;
        long y = b;
        while (e > 0) {
            if (e % 2 == 1) {
                x = (x * y) % p;
            }
            y = (y * y) % p; 
            e /= 2;
        }

        return x % p;
    }

    public static void main(String args[]) throws IOException {
        BufferedReader f = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(f.readLine());
        StringTokenizer st;
        int p = 1000000007, b, e;
        long sum = 0;
                 
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(f.readLine());
            b = Integer.parseInt(st.nextToken());
            e = Integer.parseInt(st.nextToken());

            sum = sum + power(b, e, p);
            sum = sum % p;
        }

        System.out.println(sum);
    }
}
