import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    private BankAccount account;

    // Runs before each test — starts with a fresh account of $100
    @BeforeEach
    void setUp() {
        account = new BankAccount(100.0);
    }

    // Q1: @AfterEach nulls out the account so it can be garbage collected
    @AfterEach
    void tearDown() {
        account = null;
    }

    // Q2: Deposit $50, expect balance of $150
    @Test
    void testDeposit() {
        account.deposit(50.0);
        assertEquals(150.0, account.getBalance(), "Balance should be 150 after depositing 50");
    }

    // Q3: Withdraw $40 from fresh $100 account, expect $60 remaining
    @Test
    void testWithdraw() {
        account.withdraw(40.0);
        assertEquals(60.0, account.getBalance(), "Balance should be 60 after withdrawing 40");
    }

    // Q4: Depositing a negative amount should throw an exception
    @Test
    void testInvalidDeposit() {
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-50.0));
    }

    // Q5: Withdrawing more than the balance should throw an exception
    @Test
    void testOverdraft() {
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(200.0));
    }

    // Q6: Creating an account with a negative balance should throw an exception
    @Test
    void testNegativeInitialBalance() {
        assertThrows(IllegalArgumentException.class, () -> new BankAccount(-50.0));
    }

    // Q1c: Transfer $30 from account (100) to another account (50); expect 70 and 80
    @Test
    void testTransfer() {
        BankAccount other = new BankAccount(50.0);
        account.transfer(other, 30.0);
        assertEquals(70.0, account.getBalance(), "Sender should have 70 after transfer");
        assertEquals(80.0, other.getBalance(), "Receiver should have 80 after transfer");
    }
}
