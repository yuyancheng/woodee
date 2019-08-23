//package basic.lang.oop;

public class ComplexNumber implements Cloneable {

    private double realPart;    // 实部
    private double imaginaryPart; // 虚部

    // 默认构造函数
    public ComplexNumber() {
        this.realPart = 0.0;
        this.imaginaryPart = 0.0;
    }

    public ComplexNumber(double a, double b) {
        this.realPart = a;
        this.imaginaryPart = b;
    }

    /**
     * 复数的加法
     * c = a + b的运算法则是：
     * c.realPart = a.realPart + b.realPart
     * c.imaginary.Part = a.imaginaryPart + b.imaginaryPart
     * @param aComNum 加数
     * @return 加法运算的结果，为一个复数对象
     */
    public ComplexNumber add(ComplexNumber aComNum) {
        if (aComNum == null) {
            System.err.println("对象不能够为null !");
            return new ComplexNumber();
        }
        return new ComplexNumber(
            this.realPart + aComNum.getRealPart(),
            this.imaginaryPart + aComNum.getImaginaryPart()
        );
    }

    /**
     * 复数的减法运算
     * c = a - b的运算法则是：
     * c.realPart = a.realPart - b.realPart
     * c.imaginaryPart = a.imaginaryPart -  b.imaginaryPart
     * @param aComNum 减数
     * @return 减法的运算结果，为一个复数对象
     */
    public ComplexNumber subtract(ComplexNumber aComNum) {
        if (aComNum == null) {
            System.err.println("对象不能够为null ！");
            return new ComplexNumber();
        }
        return new ComplexNumber(
            this.realPart - aComNum.getRealPart(),
            this.imaginaryPart - aComNum.getImaginaryPart()
        );
    }

    /**
     * 复数的乘法运算
     * c = a * b的运算法则是：
     * c.realPart = a.realPart * b.realPart - a.imaginaryPart * b.imaginaryPart
     * c.imaginaryPart = a.imaginaryPart * b.realPart + a.realPart * b.imaginaryPart
     * @param aComNum 乘数
     * @return 乘法运算的结果，为一个复数对象
     */
    public ComplexNumber multiply(ComplexNumber aComNum) {
        if (aComNum == null) {
            System.err.println("对象不能够为null ！");
            return new ComplexNumber();
        }
        double newReal = this.realPart * aComNum.realPart - this.imaginaryPart * aComNum.imaginaryPart;
        double newImaginary = this.realPart * aComNum.imaginaryPart + this.imaginaryPart * aComNum.realPart;
        return new ComplexNumber(newReal, newImaginary);
    }

    /**
     * 复数的除法运算
     * c = a / b的运算法则是：
     * c.实部 = (a.实部 * b.实部 + a.虚部 * b.虚部) / (b.实部 * b.实部 + b.虚部 * b.虚部);
     * c.虚部 = (a.虚部 * b.实部 - a.实部 * b.虚部) / (b.实部 * b.实部 + b.虚部 * b.虚部);
     * @param aComNum 除数
     * @return 除数运算的结果，为一个复数对象
     */
    public ComplexNumber divide(ComplexNumber aComNum) {
        if (aComNum == null) {
            System.err.println("对象不能够为null ！");
            return new ComplexNumber();
        }
        if ((aComNum.getRealPart() == 0) && (aComNum.getImaginaryPart() == 0)) {
            System.err.println("除数不能够为0 ！");
            return new ComplexNumber();
        }

        double temp = aComNum.getRealPart() * aComNum.getRealPart() +
                aComNum.getImaginaryPart() * aComNum.getImaginaryPart();
        double crealPart = (this.realPart * aComNum.getRealPart() + 
                this.imaginaryPart * aComNum.getImaginaryPart()) / temp;
        double cimaginaryPart = (this.imaginaryPart * aComNum.getRealPart() -
                this.realPart * aComNum.getImaginaryPart()) / temp;
        return new ComplexNumber(crealPart, cimaginaryPart);
    }

    /** 将一个复数显示为字符串 */
    public String toString() {
        return this.realPart + " + " + this.imaginaryPart + "i";
    }

    /** @return 返回 imaginaryPart */
    public double getImaginaryPart() {
        return imaginaryPart;
    }

    /** @param imaginaryPart 要设置的 imaginaryPart */
    public void setImaginaryPart(double imaginaryPart) {
        this.imaginaryPart = imaginaryPart;
    }

    /** @return 返回 realPart */
    public double getRealPart() {
        return realPart;
    }

    /** @param realPart 要设置的 realPart */
    public void setRealPart(double realPart) {
        this.realPart = realPart;
    }

    /** 比较一个对象是否和这个复数对象的值相等 */
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        // 先判断a是不是一个复数对象，instanceof关键字用来判断对象的类型
        if (obj instanceof ComplexNumber) {
            // 如果a是复数对象，则将它强制类型转换成复数对象，以调用复数类提供的方法
            ComplexNumber b = (ComplexNumber) obj;
            if ((this.realPart == b.getRealPart()) && (this.imaginaryPart == b.getImaginaryPart())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /** 获得该复数对象的hashCode */
    public int hashCode() {
        // 如果两个复数对象是equals的，那么它们的hashCode也必须相同
        // 两个值相等的复数对象通过toString()方法得到的输出字符串是一样的
        // 于是，可以把得到的字符串的hashCode当做复数对象的hashCode
        return this.toString().hashCode();
    }

    /** 根据现有对象克隆一个新对象，新对象和现有对象的值一样 */
    public Object clone() {
        try{
            // 克隆一个对象时，应该先调用父类的clone方法
            ComplexNumber newObject = (ComplexNumber) super.clone();
            // 将现有对象的值赋给新对象
            newObject.setRealPart(this.realPart);
            newObject.setImaginaryPart(this.imaginaryPart);
            return newObject;
        } catch (CloneNotSupportedException e) {
            // 如果没有实现Cloneable接口，抛出异常
            e.printStackTrace();
            return null;
        }
    }

    /** 测试main方法 */ 
    public static void main(String[] args) {
        ComplexNumber a = new ComplexNumber(2, 4);
        ComplexNumber b = new ComplexNumber(2, 4);
        System.out.println("ComplexNumber a:" + a.toString());
        System.out.println("ComplexNumber b:" + b.toString());
        // System.out.println("(a + b) = " + a.add(b).toString());
        // System.out.println("(a - b) = " + a.subtract(b).toString());
        // System.out.println("(a * b) = " + a.multiply(b).toString());
        // System.out.println("(a / b) = " + a.divide(b).toString());

        // 用自定义的equals方法比较两个对象是否相等
        System.out.println("a.equals(b) = " + a.equals(b));
        // 用自定义的hashCode方法获取对象的hashCode
        System.out.println("a.hashCode = " + a.hashCode());
        System.out.println("b.hashCode = " + b.hashCode());
        // 用自定义的clone方法克隆对象
        Object c = a.clone();
        System.out.println("a.equals(c) = " + a.equals(c));
        System.out.println("a.clone = " + a.clone().toString());
    }

}