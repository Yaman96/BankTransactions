import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BankApp {

    private static int operationsCount = 5;
    private static final CountDownLatch ctl = new CountDownLatch(operationsCount);

    public static void main(String[] args) throws InterruptedException {
        Bank bank = new Bank();
        System.out.println("Total money in the bank at the start of the program: " + bank.getSumAllAccounts() + " rub.");
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        long startTime = System.currentTimeMillis();

        for(int i = 0; i < operationsCount; i++) {
            threadPool.execute(() -> {
                try{
                    for (int j = 0; j < 100 ; j++) {
                        long amount = (long) (10000 + 45000 * Math.random());
                        bank.transfer(j,j+1,amount);
                        if(j == 99) {
                            break;
                        }
                    }
                    ctl.countDown();
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        ctl.await();
        threadPool.shutdown();
        System.out.println("duration of the program: " + ((System.currentTimeMillis() - startTime) / 1000) + " s.");
        System.out.println("Total money in the bank at the finish of the program: " + bank.getSumAllAccounts() + " rub.");
    }

    public int getOperationsCount() {
        return operationsCount;
    }

    public void setOperationsCount(int operationsCount) {
        BankApp.operationsCount = operationsCount;
    }
}
