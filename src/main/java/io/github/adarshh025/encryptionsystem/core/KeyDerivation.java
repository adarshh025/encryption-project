/*
 * Copyright (c) 2026 Adarsh Aher
 *
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 */
package io.github.adarshh025.encryptionsystem.core;

/**
 * Handles validation and key derivation for the Encryption System.
 *
 * <p>In the original implementation, the shift key is calculated as the sum
 * of the base-10 digits of a user-entered integer. This utility provides
 * robust parsing, sanitization, and edge-case handling.
 */
public final class KeyDerivation {

    private KeyDerivation() {
        // Utility class
    }

    /**
     * Parses a string representation of an integer and calculates its digit-sum shift key.
     *
     * @param input raw input string from the user
     * @return positive non-zero shift key
     * @throws IllegalArgumentException if the input is null, blank, non-numeric, or yields a zero key
     */
    public static long deriveKey(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Key cannot be empty. Please enter a numeric key.");
        }
        String trimmed = input.trim();
        try {
            long numericValue = Long.parseLong(trimmed);
            return deriveKey(numericValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid key format: '" + trimmed + "'. Please enter a valid number.", e);
        }
    }

    /**
     * Calculates the sum of decimal digits of a given number.
     *
     * @param value numeric value
     * @return sum of the base-10 digits (non-negative)
     * @throws IllegalArgumentException if the resulting key is zero
     */
    public static long deriveKey(long value) {
        long absVal = Math.abs(value);
        if (absVal == 0) {
            throw new IllegalArgumentException("Key cannot be zero. Please enter a non-zero key.");
        }

        long sum = 0;
        long current = absVal;
        while (current > 0) {
            sum += (current % 10);
            current /= 10;
        }

        if (sum == 0) {
            throw new IllegalArgumentException("Key derivation resulted in zero. Please enter a valid non-zero key.");
        }

        return sum;
    }
}
