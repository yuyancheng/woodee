package basic.langs.concurrent;

public class AddThread extends Thread {
    UserAccount ua;

    public AddThread(UserAccount ua) {
        this.ua = ua;
    }

    public void run() {
        for (int i=0; i<1000; i++) {
            ua.add();
        }
    }
}
