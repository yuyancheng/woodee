package basic.langs.dt;

public class DataTypes {
    private static char c;
    private int vi;
    public static void main(String[] args) {

        System.out.printf("char default vlaue:" + String.valueOf(c));
        System.out.println(c);
        new DataTypes().test();
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
        System.out.println("s: " + String.valueOf(s));
        System.out.println("s2: " + String.valueOf(s2));
    }
}
