import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BankTest {

    Bank bank = new Bank();
    Account account1 = new Account(40000, 1);
    Account account2 = new Account(40000, 2);

    @BeforeEach
    void setUp() {
        bank.getAccounts().clear();
        bank.getAccounts().put(1, account1);
        bank.getAccounts().put(2, account2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testTransferEnoughMoney() {
        try {
            bank.transfer(1,2,40000);
            assertEquals(0,bank.getAccounts().get(1).getAccBalance());
            assertEquals(80000,bank.getAccounts().get(2).getAccBalance());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testTransferNotEnoughMoney() {
        try {
            bank.transfer(1,2,45000);
            assertEquals(40000,bank.getAccounts().get(1).getAccBalance());
            assertEquals(40000,bank.getAccounts().get(2).getAccBalance());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @ValueSource(longs = {-45000,0})
    void testTransferMinusOrZeroAmount(long amount) {
        try {
            bank.transfer(1,2,amount);
            assertEquals(40000,bank.getAccounts().get(1).getAccBalance());
            assertEquals(40000,bank.getAccounts().get(2).getAccBalance());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testTransferFromAccBlocked() {
        bank.getAccounts().get(1).setBlocked(true);
        try {
            bank.transfer(1,2,10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        bank.getAccounts().get(1).setBlocked(false);
        assertEquals(40000,bank.getAccounts().get(1).getAccBalance());
    }

    @Test
    void testTransferToAccBlocked() {
        bank.getAccounts().get(2).setBlocked(true);
        try {
            bank.transfer(1,2,10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        bank.getAccounts().get(2).setBlocked(false);
        assertEquals(40000,bank.getAccounts().get(1).getAccBalance());
    }

    @Test
    void testGetBalance() {
        assertEquals(40000,bank.getBalance(1));
        assertEquals(-1,bank.getBalance(3));
    }

    @Test
    void testGetSumAllAccounts() {
        assertEquals(80000,bank.getSumAllAccounts());
    }
}