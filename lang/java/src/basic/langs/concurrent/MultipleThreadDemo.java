package basic.langs.concurrent;

public class MultipleThreadDemo extends Thread {
    public static void main(String[] args) {
        int num = 50;
        UserAccount ua = new UserAccount(num);

        AddThread at = new AddThread(ua);
        SubtractThread st = new SubtractThread(ua);

        at.start();
        st.start();
    }
}
