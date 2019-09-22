// O(logn) for range sum query and updateReplace; O(n) for tree construct
// NOTE: 0-based index
public class FenwickTree {
    int[] nums;
    int[] BIT;
    int n;

    public FenwickTree(int[] nums) {
        this.nums = nums;

        n = nums.length;
        BIT = new int[n + 1];
        for (int i = 0; i < n; i++)
            init(i, nums[i]);
    }

    public void init(int i, int val) {
        i++;
        while (i <= n) {
            BIT[i] += val;
            i += (i & -i);
        }
    }

    public void updateReplace(int i, int val) {   // replace ith element with the new "val"
        int delta = val - nums[i];
        nums[i] = val;
        init(i, delta);
    }

    public void update(int i, int delta) {    // upldate ith element by adding delta
        nums[i] += delta;
        init(i, delta);
    }

    // return the sum of elements in range [0, i] in original array
    public int getSum(int i) {
        int sum = 0;
        i++;
        while (i > 0) {
            sum += BIT[i];
            i -= (i & -i);
        }
        return sum;
    }

    public int sumRange(int i, int j) {
        return getSum(j) - getSum(i - 1);
    }
    
    public static void main(String args[]){
        int input[] = {1,3,4,2,1,6,-1};
        FenwickTree ft = new FenwickTree(input);
        
        System.out.println(ft.sumRange(0, 1));  // 0-based index
        ft.updateReplace(1, 10);     // i.e.: ft.update(1, 7);
        System.out.println(ft.sumRange(1, 2));
    }
}