package basic.langs.oop;

import basic.langs.oop.ClassInitialization;

public class SubClass extends ClassInitialization {
    // protected static final String version = "1.0.1";
    private String vStr = ClassInitialization.getStaticValue("测试子类普通方法...。");

    public SubClass() {
        super();
        System.out.println("子类构造方法。");
    }
    private static String className = "SubClass";
    {
        final String msg1 = ClassInitialization.getStaticValue("子类普通初始化块。");
        System.out.println(msg1);
        System.out.println(version);
    }
    static {
        final String msg2 = ClassInitialization.getStaticValue("子类静态初始化块。");
        System.out.println(msg2);
        // System.out.println(SubClass.className);
    }
    
    public void print() {
        super.print();
        System.out.println(this.vStr);
        // System.out.println(super.getStaticValue("XX"));
    }
    protected void finalize() {
        System.out.println("子类对象销毁。");
        super.finalize();
    }

    
}