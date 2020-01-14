package basic.langs.dt;

import java.math.BigDecimal;

public class DataTypes {
    private static char c;
    private int vi;
    public static void main(String[] args) {

        DataTypes dt = new DataTypes();
        //dt.test();

        //dt.charTest();
        dt.floatTest();
    }

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
}
