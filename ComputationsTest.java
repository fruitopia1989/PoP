import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ComputationsTest {
    // --- fibonacci ---
    // Normal values: check known Fibonacci numbers
    @Test
    void testFibonacciNormal() {
        assertEquals(0, Computations.fibonacci(0));
        assertEquals(1, Computations.fibonacci(1));
        assertEquals(1, Computations.fibonacci(2));
        assertEquals(5, Computations.fibonacci(5));
        assertEquals(55, Computations.fibonacci(10));
    }
    
    // Edge case: negative input should throw
    @Test
    void testFibonacciNegative() {
        assertThrows(IllegalArgumentException.class, () -> Computations.fibonacci(-1));
    }

    // --- isPrime ---

    // Normal: known primes and non-primes
    @Test
    void testIsPrimeNormal() {
        assertTrue(Computations.isPrime(2));
        assertTrue(Computations.isPrime(7));
        assertTrue(Computations.isPrime(13));
        assertFalse(Computations.isPrime(4));
        assertFalse(Computations.isPrime(9));
    }

    // Edge cases: 0, 1, and negative numbers are not prime
    @Test
    void testIsPrimeEdgeCases() {
        assertFalse(Computations.isPrime(0));
        assertFalse(Computations.isPrime(1));
        assertFalse(Computations.isPrime(-5));
    }

    // --- isEven / isOdd ---

    // Normal even and odd values
    @Test
    void testIsEvenAndOdd() {
        assertTrue(Computations.isEven(4));
        assertFalse(Computations.isEven(7));
        assertTrue(Computations.isOdd(3));
        assertFalse(Computations.isOdd(8));
    }

    // Edge case: 0 is even
    @Test
    void testIsEvenZero() {
        assertTrue(Computations.isEven(0));
        assertFalse(Computations.isOdd(0));
    }

    // --- toCelsius / toFahrenheit ---

    // Known conversions: 32F = 0C, 212F = 100C
    @Test
    void testToCelsius() {
        assertEquals(0.0,   Computations.toCelsius(32.0),  0.001);
        assertEquals(100.0, Computations.toCelsius(212.0), 0.001);
        assertEquals(-40.0, Computations.toCelsius(-40.0), 0.001); // -40 is the same in both
    }

    // Known conversions: 0C = 32F, 100C = 212F
    @Test
    void testToFahrenheit() {
        assertEquals(32.0,  Computations.toFahrenheit(0.0),   0.001);
        assertEquals(212.0, Computations.toFahrenheit(100.0), 0.001);
        assertEquals(-40.0, Computations.toFahrenheit(-40.0), 0.001);
    }
}
