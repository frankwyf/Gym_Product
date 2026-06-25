package com.gym;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Compile-time smoke check for gym-management-system-master.
 * If this test runs, the project compiled successfully.
 */
public class GymApplicationTests {

    @Test
    public void buildSanityCheck() {
        assertEquals(4, 2 + 2, "Project compiled and tests are executing.");
    }
}
