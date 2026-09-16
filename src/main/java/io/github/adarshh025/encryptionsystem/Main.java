/*
 * Copyright (c) 2026 Adarsh Aher
 *
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 */
package io.github.adarshh025.encryptionsystem;

import io.github.adarshh025.encryptionsystem.ui.CryptographyFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Entry point for the Encryption System application.
 */
public class Main {

    public static final String APP_NAME = "Encryption System";
    public static final String APP_VERSION = "1.0.0";
    public static final String APP_AUTHOR = "Adarsh Aher";

    public static void main(String[] args) {
        if (args.length > 0) {
            String flag = args[0].toLowerCase();
            if (flag.equals("--version") || flag.equals("-v")) {
                System.out.println(APP_NAME + " v" + APP_VERSION + " by " + APP_AUTHOR);
                return;
            } else if (flag.equals("--help") || flag.equals("-h")) {
                System.out.println(APP_NAME + " v" + APP_VERSION);
                System.out.println("Author: " + APP_AUTHOR);
                System.out.println("Usage: java -jar encryption-system.jar [options]");
                System.out.println("Options:");
                System.out.println("  -v, --version    Display application version");
                System.out.println("  -h, --help       Display this help message");
                return;
            }
        }

        // Apply native platform look-and-feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Fallback gracefully to default Swing Look and Feel
        }

        SwingUtilities.invokeLater(() -> {
            CryptographyFrame frame = new CryptographyFrame();
            frame.setVisible(true);
        });
    }
}
