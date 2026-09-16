/*
 * Copyright (c) 2026 Adarsh Aher
 *
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 */
package io.github.adarshh025.encryptionsystem;

import io.github.adarshh025.encryptionsystem.core.ShiftCipherEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ShiftCipherEngineTest {

    private ShiftCipherEngine engine;

    @BeforeEach
    void setUp() {
        engine = new ShiftCipherEngine();
    }

    @Test
    void testAsciiTextRoundTrip() {
        String plaintext = "The quick brown fox jumps over the lazy dog 1234567890 !@#$%^&*()";
        long key = 15;

        byte[] encrypted = engine.encrypt(plaintext.getBytes(StandardCharsets.UTF_8), key);
        assertNotNull(encrypted);
        assertFalse(Arrays.equals(plaintext.getBytes(StandardCharsets.UTF_8), encrypted));

        byte[] decrypted = engine.decrypt(encrypted, key);
        String restored = new String(decrypted, StandardCharsets.UTF_8);
        assertEquals(plaintext, restored);
    }

    @Test
    void testJavaSourceCodeRoundTrip() {
        String javaCode = "package com.example;\n\npublic class Test {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, World!\");\n    }\n}\n";
        long key = 23;

        byte[] encrypted = engine.encrypt(javaCode.getBytes(StandardCharsets.UTF_8), key);
        byte[] decrypted = engine.decrypt(encrypted, key);

        assertEquals(javaCode, new String(decrypted, StandardCharsets.UTF_8));
    }

    @Test
    void testEmptyData() {
        byte[] empty = new byte[0];
        long key = 10;

        byte[] encrypted = engine.encrypt(empty, key);
        assertEquals(0, encrypted.length);

        byte[] decrypted = engine.decrypt(encrypted, key);
        assertEquals(0, decrypted.length);
    }

    @Test
    void testUnicodeUtf8RoundTrip() {
        String unicodeText = "Cryptographie / 密码学 / التشفير / शिफ्रण / 🔐🛡️🔑";
        long key = 17;

        byte[] encrypted = engine.encrypt(unicodeText.getBytes(StandardCharsets.UTF_8), key);
        byte[] decrypted = engine.decrypt(encrypted, key);

        assertEquals(unicodeText, new String(decrypted, StandardCharsets.UTF_8));
    }

    @Test
    void testAllByteValuesBinaryRoundTrip() {
        byte[] allBytes = new byte[256];
        for (int i = 0; i < 256; i++) {
            allBytes[i] = (byte) i;
        }
        long key = 42;

        byte[] encrypted = engine.encrypt(allBytes, key);
        byte[] decrypted = engine.decrypt(encrypted, key);

        assertArrayEquals(allBytes, decrypted);
    }

    @Test
    void testLargePayloadStreaming() throws IOException {
        int size = 256 * 1024; // 256 KB
        byte[] largeData = new byte[size];
        new Random(12345L).nextBytes(largeData);
        long key = 19;

        ByteArrayInputStream in = new ByteArrayInputStream(largeData);
        ByteArrayOutputStream outEncrypted = new ByteArrayOutputStream(size);
        engine.encrypt(in, outEncrypted, key);

        byte[] encrypted = outEncrypted.toByteArray();
        assertEquals(size, encrypted.length);
        assertFalse(Arrays.equals(largeData, encrypted));

        ByteArrayInputStream encIn = new ByteArrayInputStream(encrypted);
        ByteArrayOutputStream outDecrypted = new ByteArrayOutputStream(size);
        engine.decrypt(encIn, outDecrypted, key);

        assertArrayEquals(largeData, outDecrypted.toByteArray());
    }

    @Test
    void testExactCaesarByteShift() {
        // In the original algorithm:
        // Encryption: (byte)(data - key)
        // Decryption: (byte)(data + key)
        byte[] original = new byte[]{100, 50, 0, (byte) 200};
        long key = 15;

        byte[] encrypted = engine.encrypt(original, key);
        assertEquals((byte) (100 - 15), encrypted[0]);
        assertEquals((byte) (50 - 15), encrypted[1]);
        assertEquals((byte) (0 - 15), encrypted[2]);
        assertEquals((byte) (200 - 15), encrypted[3]);

        byte[] decrypted = engine.decrypt(encrypted, key);
        assertArrayEquals(original, decrypted);
    }

    @Test
    void testWrongKeyFailsDecryption() {
        String plaintext = "Confidential Security Data";
        long correctKey = 15;
        long wrongKey = 16;

        byte[] encrypted = engine.encrypt(plaintext.getBytes(StandardCharsets.UTF_8), correctKey);
        byte[] decryptedWithWrongKey = engine.decrypt(encrypted, wrongKey);

        assertFalse(Arrays.equals(plaintext.getBytes(StandardCharsets.UTF_8), decryptedWithWrongKey));
    }

    @Test
    void testNullChecks() {
        assertThrows(IllegalArgumentException.class, () -> engine.encrypt((byte[]) null, 10));
        assertThrows(IllegalArgumentException.class, () -> engine.decrypt((byte[]) null, 10));
        assertThrows(IllegalArgumentException.class, () -> engine.encrypt(null, new ByteArrayOutputStream(), 10));
        assertThrows(IllegalArgumentException.class, () -> engine.decrypt(new ByteArrayInputStream(new byte[0]), null, 10));
    }
}
