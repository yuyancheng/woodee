package basic.alg.array;

public class ArrayTest {
    public static void main(String[] args) {
        ResizableArray<Integer> ra = new ResizableArray(5);
        ra.add(new Integer(11));
        ra.add(new Integer(22));
        ra.add(new Integer(33));
        ra.add(new Integer(44));
        ra.add(new Integer(55));
        ra.add(new Integer(66));
        ra.remove(1);
        ra.remove(1);
        ra.remove(1);
        ra.remove(1);
        ra.remove(1);
        ra.add(new Integer(22));
        ra.add(new Integer(33));
        System.out.println("数组的capacity是：" + ra.getCapacity());
        ra.add(new Integer(44));
        ra.add(new Integer(55));
        ra.add(new Integer(66));
        System.out.println("数组的size是：" + ra.getSize());
        System.out.println("数组的capacity是：" + ra.getCapacity());
        System.out.println("数组的内容是：" + ra.toString());
    }
}
