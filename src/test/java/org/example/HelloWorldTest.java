package org.example;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class HelloWorldTest {
    @Test
    void testAddition() {
        assertEquals(2, 1 + 1);
    }

    @Test
    void testSubtraction() {
        assertEquals(0, 1 - 1);
    }

    @Test
    void testMultiplication() {
        assertEquals(4, 2 * 2);
    }

    @Test
    void testDivision() {
        assertEquals(1, 2 / 2);
    }
}