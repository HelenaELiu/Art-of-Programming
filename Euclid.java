// GCD, LCM, mod
public class Euclid {
    long MOD = 1000000007L;

    public static long gcd(long a, long b) {
        return b == 0 ? Math.abs(a) : gcd(b, a % b);
    }

    public static long gcd2(long a, long b) {
        while (b != 0) {
            long t = b;
            b = a % b;
            a = t;
        }
        
        return Math.abs(a);
    }

    public static long lcm(long a, long b) {
        return Math.abs(a / gcd(a, b) * b);
    }

    public long mod(long a) {
        /* if (Math.abs(a) >= m) return a %= m; else return a; */
        a %= MOD;
        return a >= 0 ? a : a + MOD;
    }

    public long plus(long a, long b) {
        // ( a + b ) mod m = ( a mod n + b mod m ) mod m
        return mod(mod(a) + mod(b));
    }

    public long times(long a, long b) {
        // ( a * b ) mod m = ( a mod m * b mod m ) mod m
        return mod((mod(a) * mod(b)));
    }
}