/*
 * Copyright (c) 2026 Adarsh Aher
 *
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 */
package io.github.adarshh025.encryptionsystem.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Educational byte-level Caesar/Shift cipher engine.
 *
 * <p>Preserves the authentic algorithm from the original Encryption System:
 * <ul>
 *   <li>Encryption: Shifts each byte by subtracting the derived key: {@code (byte)(data - key)}</li>
 *   <li>Decryption: Restores each byte by adding the derived key: {@code (byte)(data + key)}</li>
 * </ul>
 *
 * <p><strong>Note on Security:</strong> This is an educational cipher designed for learning
 * basic cryptography concepts in classroom environments. Because the effective key space
 * modulo 256 is at most 256 states, and because there is no cryptographic authentication or
 * initialization vector, this cipher should not be used to protect sensitive production data.
 */
public class ShiftCipherEngine implements CipherEngine {

    private static final int BUFFER_SIZE = 8192;

    @Override
    public void encrypt(InputStream in, OutputStream out, long key) throws IOException {
        transform(in, out, -key);
    }

    @Override
    public void decrypt(InputStream in, OutputStream out, long key) throws IOException {
        transform(in, out, key);
    }

    @Override
    public byte[] encrypt(byte[] data, long key) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length);
        try {
            encrypt(bais, baos, key);
        } catch (IOException e) {
            throw new IllegalStateException("Unexpected in-memory I/O error", e);
        }
        return baos.toByteArray();
    }

    @Override
    public byte[] decrypt(byte[] data, long key) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length);
        try {
            decrypt(bais, baos, key);
        } catch (IOException e) {
            throw new IllegalStateException("Unexpected in-memory I/O error", e);
        }
        return baos.toByteArray();
    }

    /**
     * Core byte-level transformation routine supporting streaming I/O.
     *
     * @param in    source stream
     * @param out   destination stream
     * @param shift signed byte shift value
     * @throws IOException if reading or writing fails
     */
    private void transform(InputStream in, OutputStream out, long shift) throws IOException {
        if (in == null || out == null) {
            throw new IllegalArgumentException("Input and output streams cannot be null");
        }
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            for (int i = 0; i < bytesRead; i++) {
                buffer[i] = (byte) (buffer[i] + shift);
            }
            out.write(buffer, 0, bytesRead);
        }
        out.flush();
    }
}
