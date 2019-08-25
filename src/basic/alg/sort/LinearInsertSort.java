package basic.alg.sort;

import ISortNumber;

public class LinearInsertSort implements ISortNumber {
    public LinearInsertSort() {}

    /** 线性插入法 */
    public int[] srotASC(int[] iNums) {
        if (iNums == null) {
            return null;
        }
        int[] srcDatas = (int[]) iNums.clone();
        int size = srcDatas.length;
        int temp = 0;
        int index = 0;
        // 假定第一个数字已经拍好了序，所以i是从1开始而不是从0开始
        for (int i=1; i<size; i++) {
            temp = srcDatas[i];
            index = i;
            while ((index > 0) && (temp < srcDatas[index - 1])) {
                // 移动index后面的数字
                srcDatas[index] = srcDatas[index - 1];
                index --;
            }
            srcDatas[index] = temp;
        }
        return srcDatas;
    }
}