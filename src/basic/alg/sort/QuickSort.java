package basic.alg.sort;

import ISortNumber;

public class QuickSort implements ISortNumber {
    public QuickSort() {}

    /** 快速排序 */
    public int[] sortASC(int[] iNums) {
        if (iNums == null) {
            return null;
        }
        int[] srcDatas = (int[]) iNums.clone();
        return this.quickSort(srcDatas, 0, srcDatas.length - 1);
    }

    /**
     * 采用分治递归的方法实现快速排序
     * @param srcDatas
     * @param first 起始下标
     * @param last  终止下标
     * @return
     */
    private int[] quickSort(int[] srcDatas, int first, int last) {
        if (first < last) {
            int pos = partition(srcDatas, first, last);
            quickSort(srcDatas, first, pos - 1);
            quickSort(srcDatas, pos + 1, last);
        }
        return srcDatas;
    }

    /**
     * 寻找数组的分治点
     * @param srcDatas
     * @param first
     * @param last
     * @return
     */
    private int partition(int[] srcDatas, int first, int last) {
        int temp = srcDatas[first];
        int pos = first;
        for (int i=first+1; i<=last; i++) {
            if (srcDatas[i] < temp) {
                pos++;
                swap(srcDatas, pos, i);
            }
        }
        swap(srcDatas, first, pos);
        return pos;
    }

    private swap(int[] data, int src, int dest) {
        int temp = data[src];
        data[src] = data[dest];
        data[dest] = temp;
    }
}