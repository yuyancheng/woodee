package basic.langs.oop;

import java.io.IOError;
import java.io.IOException;

import basic.langs.oop.SubClass;

public class ClassInitialization {
    protected static final String version = "1.0.0";
    private String vStr = "测试父类普通方法。";

    public ClassInitialization() {
        System.out.println("父类构造方法...。");
    }
    private static String className = "ClassInitialization";
    {
        String msg1 = getStaticValue("父类普通初始化块。");
        System.out.println(msg1);
        // System.out.println(ClassInitialization.version);
    }
    static {
        String msg2 = getStaticValue("父类静态初始化块。");
        System.out.println(msg2);
        // System.out.println(ClassInitialization.className);
    }

    public static String getStaticValue(String str) {
        return str;
    }
    
    public void print() {
        // System.out.println(this.vStr);
        System.out.println("父类print方法。");
    }

    protected void finalize() {
        System.out.println("父类对象销毁。");
    }

    public static void main(String[] args) {
        SubClass sc = new SubClass();
        sc.print();
        // sc = null;
        // throw IOException("异常退出！");
        // System.gc();
    }
}

