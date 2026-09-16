/*
 * Copyright (c) 2026 Adarsh Aher
 *
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 */
package io.github.adarshh025.encryptionsystem.ui;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * File filter definitions for the Encryption System GUI.
 */
public final class FileFilters {

    private FileFilters() {
        // Utility class
    }

    /**
     * Filter that accepts directories, .txt files, and .java files.
     */
    public static class OpenFileFilter extends FileFilter {
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String name = f.getName().toLowerCase();
            return name.endsWith(".txt") || name.endsWith(".java");
        }

        @Override
        public String getDescription() {
            return "Supported Files (*.txt, *.java)";
        }
    }

    /**
     * Filter that accepts directories and .java files.
     */
    public static class SaveJavaFileFilter extends FileFilter {
        @Override
        public boolean accept(File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".java");
        }

        @Override
        public String getDescription() {
            return "Java Source File (*.java)";
        }
    }

    /**
     * Filter that accepts directories and .txt files.
     */
    public static class SaveTextFileFilter extends FileFilter {
        @Override
        public boolean accept(File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
        }

        @Override
        public String getDescription() {
            return "Text Document (*.txt)";
        }
    }
}
