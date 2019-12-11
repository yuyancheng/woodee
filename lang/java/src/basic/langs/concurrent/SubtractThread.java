package basic.langs.concurrent;

public class SubtractThread extends Thread {
    private UserAccount ua;

    public SubtractThread(UserAccount ua) {
        this.ua = ua;
    }

    public void run() {
        for (int i=0; i<1000; i++) {
            ua.subtract();
        }
    }
}
