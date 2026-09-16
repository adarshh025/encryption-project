/*
 * Copyright (c) 2026 Adarsh Aher
 *
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 */
package io.github.adarshh025.encryptionsystem;

import io.github.adarshh025.encryptionsystem.core.KeyDerivation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class KeyDerivationTest {

    @ParameterizedTest
    @CsvSource({
            "1, 1",
            "7, 7",
            "9, 9",
            "10, 1",
            "15, 6",
            "12345, 15",
            "99999, 45",
            "1000000000, 1",
            "-123, 6",
            "'  42  ', 6"
    })
    void testValidKeyDerivation(String input, long expectedKey) {
        assertEquals(expectedKey, KeyDerivation.deriveKey(input));
    }

    @Test
    void testNumericDirectDerivation() {
        assertEquals(15, KeyDerivation.deriveKey(12345L));
        assertEquals(6, KeyDerivation.deriveKey(-15L));
        assertEquals(27, KeyDerivation.deriveKey(999L));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "abc", "12a34", "12.34", "$#@!"})
    void testInvalidStringInputs(String invalidInput) {
        assertThrows(IllegalArgumentException.class, () -> KeyDerivation.deriveKey(invalidInput));
    }

    @Test
    void testNullInput() {
        assertThrows(IllegalArgumentException.class, () -> KeyDerivation.deriveKey((String) null));
    }

    @Test
    void testZeroInput() {
        assertThrows(IllegalArgumentException.class, () -> KeyDerivation.deriveKey("0"));
        assertThrows(IllegalArgumentException.class, () -> KeyDerivation.deriveKey(0L));
    }
}
