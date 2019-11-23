package basic.alg.sort;

public class SelectionSort implements ISortNumber {
    public SelectionSort() {}

    /** 选择排序 */
    public int[] sortASC(int[] iNums) {
        if (iNums == null) {
            return null;
        }
        // 因为Java的参数传递是采用引用传值的方式，再排下的过程中需要改变数组元素的顺，
        // 序，也就是改变了参数数组，所以，为了保证传入参数的值不变，这里采用了数组的
        // clone方法，直接克隆一个数组
        int [] srcDatas = (int[]) iNums.clone();
        int size = srcDatas.length;
        // 从头遍历数组元素
        for (int i=0; i<size; i++) {
            for (int j=i; j<size; j++) {
                // 如果数组前面的值比后面的值大，则交换位置
                if (srcDatas[i] > srcDatas[j]) {
                    swap(srcDatas, i, j);
                }
            }
        }
        return srcDatas;
    }

    /**
     * 交换数组中下标为src和desc的值
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