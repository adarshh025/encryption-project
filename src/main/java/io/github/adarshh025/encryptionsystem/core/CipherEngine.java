/*
 * Copyright (c) 2026 Adarsh Aher
 *
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 */
package io.github.adarshh025.encryptionsystem.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface defining symmetric encryption and decryption operations.
 */
public interface CipherEngine {

    /**
     * Encrypts data from the given input stream and writes the ciphertext to the output stream.
     *
     * @param in  source stream containing plaintext bytes
     * @param out destination stream for encrypted bytes
     * @param key numeric encryption key
     * @throws IOException if an I/O error occurs during stream reading or writing
     */
    void encrypt(InputStream in, OutputStream out, long key) throws IOException;

    /**
     * Decrypts data from the given input stream and writes the plaintext to the output stream.
     *
     * @param in  source stream containing ciphertext bytes
     * @param out destination stream for decrypted bytes
     * @param key numeric decryption key
     * @throws IOException if an I/O error occurs during stream reading or writing
     */
    void decrypt(InputStream in, OutputStream out, long key) throws IOException;

    /**
     * Encrypts a byte array using the provided key.
     *
     * @param data plaintext bytes
     * @param key  numeric encryption key
     * @return encrypted byte array
     */
    byte[] encrypt(byte[] data, long key);

    /**
     * Decrypts a byte array using the provided key.
     *
     * @param data ciphertext bytes
     * @param key  numeric decryption key
     * @return decrypted byte array
     */
    byte[] decrypt(byte[] data, long key);
}
