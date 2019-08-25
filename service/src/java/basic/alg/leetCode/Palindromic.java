package alg.leetCode;

public class Palindromic {
    public static void main(String[] args) {
        //System.out.println(new Solution().nearestPalindromic("123456"));
        //System.out.println(new Solution().nearestPalindromic("12122121"));
        //System.out.println(new Solution().nearestPalindromic("54321"));
        System.out.println(new Solution2().nearestPalindromic("121"));
    }
}

class Solution2 {
    public String nearestPalindromic(String n) {
        return getPalindromic(n);
    }

    private String getPalindromic(String n) {
        char a = 'a';
        char c = 'c';
        System.out.println(Character.hashCode(a));
        System.out.println(Character.hashCode(c));

        String str = "";
        char[] strArr = n.toCharArray();
        int len = strArr.length;

        for(int i=0; i <= Math.floor(len / 2); i++) {
            strArr[len - 1 - i] = strArr[i];
        }

        str = strArr.toString();
        if (str.equals(n)) {
            int idx = (int)Math.ceil(len / 2);
            int mid = Integer.parseInt(String.valueOf(strArr[idx]));
            if (mid != 0) {
                mid --;
                strArr[idx] = (char)mid;
                getPalindromic(strArr.toString());
            }
        }

        return String.valueOf(strArr);
    }
}