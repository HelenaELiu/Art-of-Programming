// O (col ^ 2 * row). see also
public class MaxSubRectangle {
    public MaxRectangleResult maxSum(int[][] grid) {
        int rows = grid.length, cols = grid[0].length;
        int[] tmp = new int[rows];
        MaxRectangleResult result = new MaxRectangleResult();
        for (int left = 0; left < cols; left++) {
            for (int i = 0; i < rows; i++)
                tmp[i] = 0;

            for (int right = left; right < cols; right++) {
                for (int i = 0; i < rows; i++)
                    tmp[i] += grid[i][right];

                KadaneResult kResult = kadane(tmp);
                if (kResult.maxSum > result.maxSum) {
                    result.maxSum = kResult.maxSum;
                    result.leftBound = left;
                    result.rightBound = right;
                    result.upBound = kResult.start;
                    result.lowBound = kResult.end;
                }
            }
        }
        
        return result;
    }

    private KadaneResult kadane(int arr[]) {
        int max = 0, maxStart = -1, maxEnd = -1;
        int curStart = 0, curMax = 0;
        for (int i = 0; i < arr.length; i++) {
            curMax += arr[i];
            if (curMax < 0) {
                curMax = 0;
                curStart = i + 1;
            }
            if (max < curMax) {
                maxStart = curStart;
                maxEnd = i;
                max = curMax;
            }
        }
        
        return new KadaneResult(max, maxStart, maxEnd);
    }   
    
    class MaxRectangleResult{
        int maxSum, leftBound, rightBound, upBound, lowBound;
        public String toString() {
            return "[maxSum = " + maxSum + ", leftBound = " + leftBound + ", rightBound = " + rightBound + ", upBound = " + upBound + ", lowBound = " + lowBound + "]";
        }
    }
    
    class KadaneResult{
        int maxSum, start, end;
        public KadaneResult(int maxSum, int start, int end) {
            this.maxSum = maxSum;
            this.start = start;
            this.end = end;
        }
    }

    public static void main(String args[]){
        int input[][] = {{ 2,  1, -3, -4,  5},
                         { 0,  6,  3,  4,  1},
                         { 2, -2, -1,  4, -5},
                         {-3,  3,  1,  0,  3}};
        MaxSubRectangle max = new MaxSubRectangle();
        System.out.println(max.maxSum(input));
    }
}
