package basic.langs.concurrent;

public class UserAccount {
    private int count;
    public UserAccount(int count) {
        this.count = count;
    }
    public synchronized void add() {
        if (count < 100) {
            count++;
            System.out.println("Add thread is running: " + count);
        } else {
            subtract();
        }
    }

    public synchronized void subtract() {
        if (count > 0) {
            count--;
            System.out.println("Subtract thread is running: " + count);
        } else {
            add();
        }
    }
}
