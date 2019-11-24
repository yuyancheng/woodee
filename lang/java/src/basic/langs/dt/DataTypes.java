package basic.langs.dt;

public class DataTypes {
    private static char c;
    public static void main(String[] args) {

        System.out.printf("char default vlaue:" + String.valueOf(c));
        System.out.println(c);
        new DataTypes().test();
    }

    private void test() {
        short s = 1;
        s = (short)(s + 1);
        short s2 = 1;
        s2 += 1;
        System.out.println(s);
        System.out.println("s: " + String.valueOf(s));
        System.out.println("s2: " + String.valueOf(s2));
    }
}
