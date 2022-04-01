import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {

    private long accBalance;
    private int accNumber;
    private volatile boolean isBlocked;
    private final Lock lock = new ReentrantLock();

    public Account() {}

    public Account(long accBalance, int accNumber) {
        this.accBalance = accBalance;
        this.accNumber = accNumber;
    }

    public boolean withdraw(long amount) {
        if (amount <= accBalance && amount > 0) {
            accBalance -= amount;
            return true;
        }
        else {
            return false;
        }
    }

    public boolean topup(long amount) {
        if(amount > 0) {
            accBalance += amount;
            return true;
        }
        else return false;
    }

    public Lock getLock() {
        return lock;
    }

    public long getAccBalance() {
        return accBalance;
    }

    public void setAccBalance(long accBalance) {
        this.accBalance = accBalance;
    }

    public int getAccNumber() {
        return accNumber;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }
}

