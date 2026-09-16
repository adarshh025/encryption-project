/*
 * Copyright (c) 2026 Adarsh Aher
 *
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 */
package io.github.adarshh025.encryptionsystem.ui;

import io.github.adarshh025.encryptionsystem.core.CipherEngine;
import io.github.adarshh025.encryptionsystem.core.KeyDerivation;
import io.github.adarshh025.encryptionsystem.core.ShiftCipherEngine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;

/**
 * Main GUI frame for the Encryption System.
 *
 * <p>Provides user-friendly file selection, password-based key entry,
 * and seamless encryption/decryption using the underlying cipher engine.
 */
public class CryptographyFrame extends JFrame implements ActionListener {

    private final CipherEngine cipherEngine;

    private JLabel statusLabel;
    private JTextField filePathField;
    private JButton browseButton;
    private JButton encryptButton;
    private JButton decryptButton;
    private JButton cancelButton;

    private JFileChooser openFileChooser;
    private File selectedFile;

    public CryptographyFrame() {
        this(new ShiftCipherEngine());
    }

    public CryptographyFrame(CipherEngine engine) {
        super("Encryption System - Secure File Utility");
        this.cipherEngine = engine;

        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = Math.max(700, screenSize.width / 2);
        int height = Math.max(500, screenSize.height / 2);
        setSize(width, height);
        setLocationRelativeTo(null);

        openFileChooser = new JFileChooser();
        openFileChooser.setFileFilter(new FileFilters.OpenFileFilter());

        // Top Panel: File Selection & Status
        JPanel topPanel = createTopPanel();

        // Center Panel: Visual Brand / Logo
        JPanel centerPanel = createCenterPanel();

        // Bottom Panel: Action Buttons
        JPanel bottomPanel = createBottomPanel();

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 20, 10, 20),
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        " Select Target File ",
                        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                        javax.swing.border.TitledBorder.DEFAULT_POSITION,
                        new Font("Segoe UI", Font.BOLD, 13)
                )
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        statusLabel = new JLabel("Choose a text (.txt) or Java (.java) file to encrypt or decrypt:");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(statusLabel, gbc);

        filePathField = new JTextField();
        filePathField.setEditable(false);
        filePathField.setFont(new Font("Consolas", Font.PLAIN, 12));
        filePathField.setText("No file selected");
        filePathField.setPreferredSize(new Dimension(380, 28));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        panel.add(filePathField, gbc);

        browseButton = new JButton("Browse...");
        browseButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        browseButton.setPreferredSize(new Dimension(100, 28));
        browseButton.addActionListener(this);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        panel.add(browseButton, gbc);

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        ImageIcon logoIcon = loadIcon("/io/github/adarshh025/encryptionsystem/logo.GIF");
        JLabel imageLabel;
        if (logoIcon != null && logoIcon.getIconWidth() > 0) {
            imageLabel = new JLabel(logoIcon, SwingConstants.CENTER);
        } else {
            imageLabel = new JLabel("Encryption System", SwingConstants.CENTER);
            imageLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
            imageLabel.setForeground(new Color(60, 90, 150));
        }

        panel.add(imageLabel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 20, 15, 20));

        encryptButton = new JButton("Encrypt File");
        encryptButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        encryptButton.setPreferredSize(new Dimension(130, 36));
        encryptButton.setEnabled(false);
        encryptButton.addActionListener(this);

        decryptButton = new JButton("Decrypt File");
        decryptButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        decryptButton.setPreferredSize(new Dimension(130, 36));
        decryptButton.setEnabled(false);
        decryptButton.addActionListener(this);

        cancelButton = new JButton("Close");
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cancelButton.setPreferredSize(new Dimension(100, 36));
        cancelButton.addActionListener(this);

        panel.add(encryptButton);
        panel.add(decryptButton);
        panel.add(cancelButton);

        return panel;
    }

    private ImageIcon loadIcon(String resourcePath) {
        URL url = getClass().getResource(resourcePath);
        if (url != null) {
            return new ImageIcon(url);
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == browseButton) {
            handleBrowse();
        } else if (src == encryptButton) {
            handleProcess(true);
        } else if (src == decryptButton) {
            handleProcess(false);
        } else if (src == cancelButton) {
            dispose();
        }
    }

    private void handleBrowse() {
        int result = openFileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = openFileChooser.getSelectedFile();
            filePathField.setText(selectedFile.getAbsolutePath());
            statusLabel.setText("Selected: " + selectedFile.getName());
            encryptButton.setEnabled(true);
            decryptButton.setEnabled(true);
        }
    }

    private void handleProcess(boolean isEncrypt) {
        if (selectedFile == null || !selectedFile.exists()) {
            JOptionPane.showMessageDialog(this,
                    "Please select an existing file first.",
                    "File Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String actionName = isEncrypt ? "Encryption" : "Decryption";
        String promptMsg = "Enter numeric key for " + actionName.toLowerCase() + ":";
        String keyInput = JOptionPane.showInputDialog(this,
                promptMsg,
                actionName + " Key",
                JOptionPane.QUESTION_MESSAGE);

        if (keyInput == null) {
            // User cancelled
            return;
        }

        long derivedKey;
        try {
            derivedKey = KeyDerivation.deriveKey(keyInput);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Invalid Key",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        File destinationFile = promptSaveDestination(isEncrypt);
        if (destinationFile == null) {
            // User cancelled save dialog
            return;
        }

        // Execute encryption or decryption with visual feedback
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try (InputStream in = new BufferedInputStream(new FileInputStream(selectedFile));
             OutputStream out = new BufferedOutputStream(new FileOutputStream(destinationFile))) {

            if (isEncrypt) {
                cipherEngine.encrypt(in, out, derivedKey);
            } else {
                cipherEngine.decrypt(in, out, derivedKey);
            }

            JOptionPane.showMessageDialog(this,
                    actionName + " completed successfully!\n\nSaved to: " + destinationFile.getAbsolutePath(),
                    actionName + " Successful",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "An error occurred while processing the file:\n" + ex.getMessage(),
                    "I/O Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private File promptSaveDestination(boolean isEncrypt) {
        JFileChooser saveChooser = new JFileChooser();
        saveChooser.setDialogTitle(isEncrypt ? "Save Encrypted File" : "Save Decrypted File");
        saveChooser.addChoosableFileFilter(new FileFilters.SaveTextFileFilter());
        saveChooser.addChoosableFileFilter(new FileFilters.SaveJavaFileFilter());

        // Smart default filename
        String origName = selectedFile.getName();
        String defaultName;
        if (isEncrypt) {
            defaultName = origName.endsWith(".enc") ? origName : origName + ".enc";
        } else {
            defaultName = origName.endsWith(".enc")
                    ? origName.substring(0, origName.length() - 4)
                    : "decrypted_" + origName;
        }
        saveChooser.setSelectedFile(new File(defaultName));

        int response = saveChooser.showSaveDialog(this);
        if (response != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        File chosen = saveChooser.getSelectedFile();
        FileFilter filter = saveChooser.getFileFilter();
        String desiredExt = null;
        if (filter instanceof FileFilters.SaveJavaFileFilter) {
            desiredExt = ".java";
        } else if (filter instanceof FileFilters.SaveTextFileFilter) {
            desiredExt = ".txt";
        }

        if (desiredExt != null && !chosen.getName().toLowerCase().endsWith(desiredExt)) {
            chosen = new File(chosen.getParentFile(), chosen.getName() + desiredExt);
        }

        if (chosen.exists()) {
            int overwrite = JOptionPane.showConfirmDialog(this,
                    "File '" + chosen.getName() + "' already exists. Overwrite?",
                    "Confirm Overwrite",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (overwrite != JOptionPane.YES_OPTION) {
                return null;
            }
        }

        return chosen;
    }
}
