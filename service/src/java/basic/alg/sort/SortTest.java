package basic.alg.sort;

public class SortTest {
    /**
     * 打印数组
     * @param iNums
     */
    public static void printIntArray(int[] iNums) {
        if (iNums == null) {
            return;
        }
        for (int i=0; i<iNums.length; i++) {
            System.out.println(iNums[i] + " ");
        }
        System.out.println();
    }
    public static void main(String[] args) {
        int[] iNums = new int[]{6,3,4,2,7,2,-3,3};
        System.out.println("排序前的数组：");
        printIntArray(iNums);
        ISortNumber test = new SelectionSort();
        System.out.println("选择排序后的结果：");
        printIntArray(test.sortASC(iNums));
        
        System.out.println("排序前的数组：");
        printIntArray(iNums);
        test = new BubbleSort();
        System.out.println("冒泡排序后的结果");
        printIntArray(test.sortASC(iNums));

        System.out.println("排序前的数组：");
        printIntArray(iNums);
        test = new LinearInsertSort();
        System.out.println("线性插入排序后的结果");
        printIntArray(test.sortASC(iNums));

        System.out.println("排序前的数组：");
        printIntArray(iNums);
        test = new QuickSort();
        System.out.println("快速排序后的结果");
        printIntArray(test.sortASC(iNums));
    }
}