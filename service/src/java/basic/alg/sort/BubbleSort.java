package alg.sort;// package basic.alg.sort;

public class BubbleSort implements ISortNumber {
    public BubbleSort() {}

    /** 冒泡排序 */
    public int[] sortASC(int[] iNums) {
        if (iNums == null) {
            return null;
        }
        // 克隆数组
        int[] srcDatas = (int[]) iNums.clone();
        boolean changedPosition = true; // 标识是否交换了元素位置
        int comparedTimes = 0;  // 标识比较次数
        int maxComparedTimes = srcDatas.length - 1; // 标识排序过程中最多可能交换的次数

        // 如果已经发生的比较次数还没有达到最大次数，而且此前交换过元素位置，则继续
        while ((comparedTimes < maxComparedTimes) && (changedPosition)) {
            for (int i=0; i<(maxComparedTimes-comparedTimes); i++) {
                changedPosition = false;
                if (srcDatas[i] > srcDatas[i+1]) {
                    swap(srcDatas, i, i+1);
                    changedPosition = true;
                }
            }
            comparedTimes++;
        }
        return srcDatas;
    }

    /**
     * 交换元素
     * @param data
     * @param src
     * @param dest
     */
    private void swap(int[] data, int src, int dest) {
        int temp = data[src];
        data[src] = data[dest];
        data[dest] = temp;
    }

}