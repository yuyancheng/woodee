package alg.leetCode;

public class ArrayTest {
    public static void main(String[] args) {
        Solution s = new Solution();
        int[] A = {2, -1, 2, 3, -2};
        int K = 3;
        System.out.print(s.shortestSubarray(A, K));
    }
}

class Solution {
    public int shortestSubarray(int[] A, int K) {
        return getArrLen(A, K);
    }
    
    private int getArrLen(int[] A, int K) {
        int min = -1;
        int lt=0, rt=0;
        int S = 0;
        int LS = S;
        
        while(rt < A.length) {
            S += A[rt];
            if (S >= K) {
                LS = S;
                
                if (rt - lt + 1 < min || min == -1) {
                    min = rt - lt + 1;
                }
                
                while(lt < rt) {
                    S -= A[lt];
                    if (S >= K) {
                        if (rt - lt + 1 < min || min == -1) {
                            min = rt - lt + 1;
                        }
                        lt ++;
                    } else {
                        break;
                    }
                }
                
                if (lt == rt) {
                    S = A[rt];
                    if (S >= K) {
                        return 1;
                    }
                }
                
            } else {
                if (LS > 0 && S <= LS) {
                    rt --;
                    S = LS;
                    
                    while(lt < rt) {
                        S -= A[lt];
                        if (S >= K) {
                            if (rt - lt + 1 < min || min == -1) {
                                min = rt - lt + 1;
                            }
                            lt ++;
                        } else {
                            break;
                        }
                    }
                    
                    if (lt == rt) {
                        S = A[rt];
                        if (S >= K) {
                            return 1;
                        }
                    }
                }
            }
            rt++;
        }
        
        return min;
    }
}