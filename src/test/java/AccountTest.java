import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private static Account account1;
    private static Account account2;

    @BeforeAll
    static void beforeAll() {
        account1 = new Account();
        account2 = new Account();
    }

    @BeforeEach
    void setUp() {
        account1.setAccBalance(100000);
        account2.setAccBalance(100000);
    }

    @ParameterizedTest
    @CsvSource({"10000,110000,-5000"})
    void testWithdraw(long lessThanAccBalance, long moreThanAccBalance, long minusOrZero) {
        assertTrue(account1.withdraw(lessThanAccBalance)); //1-й параметр. На счете денег достаточно.
        assertEquals(90000,account1.getAccBalance());
        assertFalse(account2.withdraw(moreThanAccBalance));//2-й параметр. На счете денег недостаточно.
        assertEquals(100000,account2.getAccBalance());
        assertFalse(account2.withdraw(minusOrZero));//3-й параметр. Попытка перевести отрицательную сумму, либо ноль.
        assertEquals(100000,account2.getAccBalance());
    }

    @ParameterizedTest
    @CsvSource({"10000,-10000"})
    void testTopup(long moreThanZero, long minusOrZero) {
        assertTrue(account1.topup(moreThanZero));
        assertFalse(account2.topup(minusOrZero));
        assertEquals(110000,account1.getAccBalance());
        assertEquals(100000,account2.getAccBalance());
    }

    @Test
    void testSetBlocked() {
    }
}