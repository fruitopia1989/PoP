import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MiscTests {

    // Q2: Fail if ANY value in the array is less than 20
    @Test
    void testAllValuesAtLeast20() {
        int[] values = {20, 35, 22, 47, 100};  // replace with your actual array
        for (int v : values) {
            assertTrue(v >= 20, "Value " + v + " is less than 20");
        }
    }

    // Q3: Pass only if strOne and strTwo contain the exact same characters (same content)
    @Test
    void testStringsEqualContent() {
        String strOne = "hello";
        String strTwo = "hello";  // replace with your actual strings
        assertEquals(strOne, strTwo, "Strings do not contain the same characters");
    }
}
