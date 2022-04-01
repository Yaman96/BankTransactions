import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class Bank {

    private final Map<Integer, Account> accounts = fillTheBank();
    private final Random random = new Random();

    public synchronized boolean isFraud(int fromAccountNum, int toAccountNum, long amount) throws InterruptedException {
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    public void transfer(int fromAccountNum, int toAccountNum, long amount) throws InterruptedException {
        Account fromAcc = accounts.get(fromAccountNum);
        Account toAcc = accounts.get(toAccountNum);

        if (fromAcc.isBlocked() || toAcc.isBlocked()) {
            return;
        }

        doTransfer(fromAcc, toAcc, amount);

        if (amount > 50000) {
            if (isFraud(fromAccountNum, toAccountNum, amount)) {
                doTransfer(toAcc, fromAcc, amount);
                fromAcc.setBlocked(true);
                toAcc.setBlocked(true);
            }
        }
    }

    private void doTransfer(Account fromAccount, Account toAccount, long amount) {
        try {
            lockAccounts(fromAccount, toAccount);
            if (fromAccount.withdraw(amount)) {
                toAccount.topup(amount);
            }
        } finally {
            unlockAccounts(fromAccount, toAccount);
        }
    }

    private void lockAccounts(Account fromAccount, Account toAccount) {
        while (true) {
            boolean fromAccLocked = fromAccount.getLock().tryLock();
            boolean toAccLocked = toAccount.getLock().tryLock();
            if (fromAccLocked && toAccLocked) {
                return;
            }
            if (fromAccLocked) fromAccount.getLock().unlock();
            if (toAccLocked) toAccount.getLock().unlock();
        }
    }

    private void unlockAccounts(Account fromAccount, Account toAccount) {
        fromAccount.getLock().unlock();
        toAccount.getLock().unlock();
    }

    public long getBalance(int accountNum) {
        if(this.getAccounts().containsKey(accountNum))
        return accounts.get(accountNum).getAccBalance();
        else {
            System.out.println("There is no such account.");
            return -1;
        }
    }

    public long getSumAllAccounts() {
        AtomicLong sum = new AtomicLong();
        accounts.forEach((key, value) -> sum.getAndAdd(value.getAccBalance()));
        return sum.get();
    }

    public Map<Integer, Account> getAccounts() {
        return accounts;
    }

    private Map<Integer, Account> fillTheBank() {
        Map<Integer, Account> accountMap = new HashMap<>();
        for (int i = 0; i <= 100; i++) {
            long amount = (long) (70000 + 30000 * Math.random());
            Account account = new Account(amount, i);
            accountMap.put(i, account);
        }
        return accountMap;
    }
}
