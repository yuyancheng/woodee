package basic.lang.oop;

public class ComplexNumber {

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
     */

}