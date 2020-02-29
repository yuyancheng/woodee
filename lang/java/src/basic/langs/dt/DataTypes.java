package basic.langs.dt;

import java.math.BigDecimal;

public class DataTypes {
    private static char c;
    private int vi;

    private void test() {
        short s = 1;
        String s1 = "1";
        String ss = s1 + "times";
        //Integer v = 2;
        System.out.println("1times" == ss);
        s = (short)(s + 1);
        short s2 = 1;
        s2 += 1;
        //System.out.println(v.equals(2));
        //System.out.println("s: " + String.valueOf(s));
        //System.out.println("s2: " + String.valueOf(s2));


    }

    private void charTest() {
        char a = 'a';
        int b = 010;
        int c = 12;

        System.out.printf("char default vlaue:" + String.valueOf(c));
        System.out.println(c);
        System.out.println(new Integer(97).equals(a));
    }

    private void intTest() {
        Integer i = 1000;
        int a = new Integer(1000);
        System.out.println(i.intValue() == a);

        int[] ia = {12,32,34};
        for (int j = ia.length - 1; j >= 0; j--) {

        }
        int[] aa = new int[ia.length];
        System.out.println(aa.length);
    }

    private void floatTest() {
        float a = 10.f;
        double b = 2.;
        float c = (float)(a * b);
        double d = 20;
        BigDecimal bd = BigDecimal.valueOf(b);

        System.out.println(a * b == c);
        System.out.println(a * b == d);
        System.out.println(c == d);

        System.out.println(bd.compareTo((BigDecimal.valueOf(b))));
    }
    int[] ia = new int[]{1,2,3,4,5};
    int[] ib = new int[]{1,2,3,4,5};
    private void setArr(int[] arr) {
        //arr[0] = 2;
        System.out.println(arr);
        arr = new int[]{10,20};
        int[] brr = arr;
        arr = new int[]{30,40};
        //arr[0] = 30;

        System.out.println(arr[0]);
        System.out.println(brr[0]);
    }

    private void arrTest() {
        //System.out.println(ia);
        setArr(ia);
    }

    public static void main(String[] args) {

        DataTypes dt = new DataTypes();
        //dt.test();
        //dt.charTest();
        // dt.intTest();
        //dt.floatTest();
        //dt.intTest();
        //DataTypes dt = new DataTypes();

        dt.arrTest();

    }
}
