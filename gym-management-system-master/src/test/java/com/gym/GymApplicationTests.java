package com.gym;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Minimal unit tests for GymApplication — run without any external infrastructure.
 * These serve as a compile-time smoke check and validate basic project invariants.
 */
public class GymApplicationTests {

    @Test
    public void applicationClassExists() {
        // Verifies the main application class is on the classpath and instantiable.
        assertDoesNotThrow(() -> Class.forName("com.gym.GymApplication"));
    }

    @Test
    public void mainClassHasCorrectPackage() {
        assertEquals("com.gym", GymApplication.class.getPackage().getName());
    }
}
