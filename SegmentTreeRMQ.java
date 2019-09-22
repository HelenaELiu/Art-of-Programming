// O(logn) for range query and update; O(n) for tree construct
// Change line marked with !! to make it Max, XOR, or SUM segment tree.
public class SegmentTreeRMQ {
    int st[]; 

    public void constructST(int arr[], int n) {
        int x = (int) (Math.ceil(Math.log(n) / Math.log(2)));
        int max_size = 2 * (int) Math.pow(2, x) - 1; //Maximum size of segment tree 
        st = new int[max_size];

        constructSTUtil(arr, 0, n - 1, 0);
    }
    
    public int RMQ(int n, int qs, int qe) {
        if (qs < 0 || qe > n - 1 || qs > qe) {
            System.out.println("Invalid Input");
            return -1;
        }

        return RMQUtil(0, n - 1, qs, qe, 0);
    }

    // si is index of current node in segment tree st 
    private int constructSTUtil(int arr[], int ss, int se, int si) {
        if (ss == se) {
            st[si] = arr[ss];
            return arr[ss];
        }

        int mid = getMid(ss, se);
        //!! change Math.min to Math.max for Range Max Query. Change to ^ for XOR, etc.
        st[si] = Math.min(constructSTUtil(arr, ss, mid, si * 2 + 1), constructSTUtil(arr, mid + 1, se, si * 2 + 2));
        return st[si];
    }

    private int RMQUtil(int ss, int se, int qs, int qe, int index) {
        if (qs <= ss && qe >= se) {
            return st[index];
        }

        if (se < qs || ss > qe) {
            return Integer.MAX_VALUE;                   //!!
        }

        // If a part of this segment overlaps with the given range 
        int mid = getMid(ss, se);
        return Math.min(RMQUtil(ss, mid, qs, qe, 2 * index + 1), RMQUtil(mid + 1, se, qs, qe, 2 * index + 2));      //!!
    }

    private int getMid(int s, int e) {
        return s + (e - s) / 2;
    }
    
    public static void main(String args[]) {
        int arr[] = {1, 3, 2, 7, 9, 11};
        int n = arr.length;
        SegmentTreeRMQ tree = new SegmentTreeRMQ();

        tree.constructST(arr, n);

        int qs = 1;  // Starting index of query range 
        int qe = 5;  // Ending index of query range 
        System.out.println("Minimum of values in range [" + qs + ", " + qe + "] is = " + tree.RMQ(n, qs, qe));
    }
}