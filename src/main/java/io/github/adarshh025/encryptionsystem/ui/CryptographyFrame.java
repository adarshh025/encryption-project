/*
 * Copyright (c) 2026 Adarsh Aher
 *
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 */
package io.github.adarshh025.encryptionsystem.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
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
import java.awt.Image;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
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
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

/**
 * Modern, responsive Swing interface for Encryption System.
 *
 * <p>Features:
 * <ul>
 *   <li>FlatLaf modern light/dark theme with instant live toggle</li>
 *   <li>Drag-and-drop file target zone</li>
 *   <li>Real-time key derivation preview (digit-sum feedback as you type)</li>
 *   <li>Interactive status updates and progress bar</li>
 *   <li>Safe file overwrite prompts and smart extension handling</li>
 * </ul>
 */
public class CryptographyFrame extends JFrame implements ActionListener {

    private final CipherEngine cipherEngine;

    // UI Components
    private JLabel logoLabel;
    private JLabel appTitleLabel;
    private JLabel appSubtitleLabel;
    private JButton themeToggleButton;

    private JPanel dropCard;
    private JLabel dropIconLabel;
    private JLabel dropPrimaryLabel;
    private JLabel dropDetailsLabel;
    private JButton browseButton;
    private JButton clearFileButton;

    private JTextField keyInputField;
    private JLabel keyDerivedBadge;

    private JButton encryptButton;
    private JButton decryptButton;
    private JProgressBar progressBar;
    private JLabel statusMessageLabel;

    private JFileChooser fileChooser;
    private File selectedFile;
    private boolean isDarkMode = false;

    public CryptographyFrame() {
        this(new ShiftCipherEngine());
    }

    public CryptographyFrame(CipherEngine engine) {
        super("Encryption System");
        this.cipherEngine = engine;

        initUI();
        initDropTarget();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(680, 620));
        setPreferredSize(new Dimension(740, 680));
        setLocationRelativeTo(null);

        // App Icon
        ImageIcon appIcon = loadIcon("/io/github/adarshh025/encryptionsystem/logo.png", 32, 32);
        if (appIcon != null) {
            setIconImage(appIcon.getImage());
        }

        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilters.OpenFileFilter());

        // Root Container with padding
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // 1. Header Bar
        JPanel headerPanel = createHeaderPanel();

        // 2. File Selection Card (Drop Zone)
        dropCard = createFileCard();

        // 3. Key Configuration Card
        JPanel keyCard = createKeyCard();

        // 4. Action Buttons & Progress Bar
        JPanel actionPanel = createActionPanel();

        root.add(headerPanel);
        root.add(Box.createVerticalStrut(15));
        root.add(dropCard);
        root.add(Box.createVerticalStrut(15));
        root.add(keyCard);
        root.add(Box.createVerticalStrut(15));
        root.add(actionPanel);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(root, BorderLayout.CENTER);
        pack();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setOpaque(false);

        // Logo + Title left aligned
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);

        ImageIcon logoIcon = loadIcon("/io/github/adarshh025/encryptionsystem/logo.png", 52, 52);
        if (logoIcon != null) {
            logoLabel = new JLabel(logoIcon);
            left.add(logoLabel);
        }

        JPanel titleGroup = new JPanel();
        titleGroup.setLayout(new BoxLayout(titleGroup, BoxLayout.Y_AXIS));
        titleGroup.setOpaque(false);

        appTitleLabel = new JLabel("Encryption System");
        appTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));

        appSubtitleLabel = new JLabel("Educational Byte-Shift Cipher • Adarsh Aher");
        appSubtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        appSubtitleLabel.setForeground(new Color(120, 130, 145));

        titleGroup.add(appTitleLabel);
        titleGroup.add(Box.createVerticalStrut(2));
        titleGroup.add(appSubtitleLabel);
        left.add(titleGroup);

        // Theme Toggle Button on right
        themeToggleButton = new JButton(isDarkMode ? "☀️ Light Mode" : "🌙 Dark Mode");
        themeToggleButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        themeToggleButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        themeToggleButton.addActionListener(e -> toggleTheme());

        panel.add(left, BorderLayout.WEST);
        panel.add(themeToggleButton, BorderLayout.EAST);
        return panel;
    }

    private JPanel createFileCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 16; background: lighten($Panel.background, 3%)");
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 220), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        dropIconLabel = new JLabel("📁");
        dropIconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        card.add(dropIconLabel, gbc);

        gbc.gridy = 1;
        dropPrimaryLabel = new JLabel("Drag and drop a file here, or click Browse");
        dropPrimaryLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        card.add(dropPrimaryLabel, gbc);

        gbc.gridy = 2;
        dropDetailsLabel = new JLabel("Supports plain text (*.txt) and Java source files (*.java)");
        dropDetailsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dropDetailsLabel.setForeground(new Color(130, 140, 155));
        card.add(dropDetailsLabel, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(12, 8, 4, 8);
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonRow.setOpaque(false);

        browseButton = new JButton("Browse File...");
        browseButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        browseButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        browseButton.addActionListener(this);
        buttonRow.add(browseButton);

        clearFileButton = new JButton("Clear");
        clearFileButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        clearFileButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        clearFileButton.setVisible(false);
        clearFileButton.addActionListener(e -> clearSelectedFile());
        buttonRow.add(clearFileButton);

        card.add(buttonRow, gbc);
        return card;
    }

    private JPanel createKeyCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 16; background: lighten($Panel.background, 3%)");
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 225), 1, true),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel keyLabel = new JLabel("Numeric Encryption Key:");
        keyLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        card.add(keyLabel, gbc);

        keyInputField = new JTextField("12345");
        keyInputField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        keyInputField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter numbers e.g. 12345");
        keyInputField.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        keyInputField.putClientProperty(FlatClientProperties.STYLE, "arc: 10; margin: 4,8,4,8");
        keyInputField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateKeyBadge();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateKeyBadge();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateKeyBadge();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        card.add(keyInputField, gbc);

        keyDerivedBadge = new JLabel("Shift: 15 (1+2+3+4+5)");
        keyDerivedBadge.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        keyDerivedBadge.setForeground(new Color(0, 130, 200));
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        card.add(keyDerivedBadge, gbc);

        JLabel hintLabel = new JLabel("💡 Shift cipher uses base-10 digit sum: key '12345' shifts bytes by -15 (encrypt) and +15 (decrypt).");
        hintLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hintLabel.setForeground(new Color(130, 140, 155));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        card.add(hintLabel, gbc);

        return card;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        // Buttons
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonRow.setOpaque(false);

        encryptButton = new JButton("🔒 Encrypt File");
        encryptButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        encryptButton.setPreferredSize(new Dimension(170, 42));
        encryptButton.setEnabled(false);
        encryptButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        encryptButton.putClientProperty(FlatClientProperties.STYLE, "background: #0078D4; foreground: #FFFFFF");
        encryptButton.addActionListener(this);
        buttonRow.add(encryptButton);

        decryptButton = new JButton("🔓 Decrypt File");
        decryptButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        decryptButton.setPreferredSize(new Dimension(170, 42));
        decryptButton.setEnabled(false);
        decryptButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        decryptButton.putClientProperty(FlatClientProperties.STYLE, "background: #107C41; foreground: #FFFFFF");
        decryptButton.addActionListener(this);
        buttonRow.add(decryptButton);

        panel.add(buttonRow);
        panel.add(Box.createVerticalStrut(15));

        // Progress bar
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(false);
        progressBar.setVisible(false);
        progressBar.setPreferredSize(new Dimension(300, 6));
        panel.add(progressBar);

        // Status Label
        statusMessageLabel = new JLabel("Ready. Select a file to begin.", SwingConstants.CENTER);
        statusMessageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusMessageLabel.setForeground(new Color(120, 130, 145));
        statusMessageLabel.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(6));
        panel.add(statusMessageLabel);

        return panel;
    }

    private void initDropTarget() {
        new DropTarget(dropCard, DnDConstants.ACTION_COPY, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    Object transferable = dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    if (transferable instanceof List<?>) {
                        List<?> files = (List<?>) transferable;
                        if (!files.isEmpty() && files.get(0) instanceof File) {
                            File dropped = (File) files.get(0);
                            setSelectedFile(dropped);
                            dtde.dropComplete(true);
                            return;
                        }
                    }
                    dtde.dropComplete(false);
                } catch (Exception ex) {
                    dtde.dropComplete(false);
                }
            }
        });
    }

    private void updateKeyBadge() {
        String text = keyInputField.getText().trim();
        try {
            long key = KeyDerivation.deriveKey(text);
            keyDerivedBadge.setText("Shift: " + key);
            keyDerivedBadge.setForeground(new Color(0, 140, 70));
        } catch (Exception ex) {
            keyDerivedBadge.setText("Invalid key");
            keyDerivedBadge.setForeground(Color.RED);
        }
    }

    private void toggleTheme() {
        isDarkMode = !isDarkMode;
        if (isDarkMode) {
            FlatDarkLaf.setup();
            themeToggleButton.setText("☀️ Light Mode");
        } else {
            FlatLightLaf.setup();
            themeToggleButton.setText("🌙 Dark Mode");
        }
        FlatLaf.updateUI();
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void setSelectedFile(File file) {
        if (file == null || !file.exists()) {
            clearSelectedFile();
            return;
        }

        String name = file.getName().toLowerCase();
        if (!name.endsWith(".txt") && !name.endsWith(".java") && !name.endsWith(".enc")) {
            JOptionPane.showMessageDialog(this,
                    "Unsupported file format. Please select a .txt, .java, or .enc file.",
                    "Format Notice",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        this.selectedFile = file;
        dropIconLabel.setText("📄");
        dropPrimaryLabel.setText(file.getName());

        long bytes = file.length();
        String sizeStr = bytes < 1024 ? bytes + " B" : (bytes / 1024) + " KB";
        String typeStr = name.endsWith(".java") ? "Java Source" : (name.endsWith(".enc") ? "Encrypted File" : "Text Document");
        dropDetailsLabel.setText(typeStr + " • " + sizeStr + " • " + file.getAbsolutePath());

        browseButton.setText("Change File...");
        clearFileButton.setVisible(true);

        encryptButton.setEnabled(true);
        decryptButton.setEnabled(true);
        statusMessageLabel.setText("File loaded: " + file.getName());
    }

    public void clearSelectedFile() {
        this.selectedFile = null;
        dropIconLabel.setText("📁");
        dropPrimaryLabel.setText("Drag and drop a file here, or click Browse");
        dropDetailsLabel.setText("Supports plain text (*.txt) and Java source files (*.java)");

        browseButton.setText("Browse File...");
        clearFileButton.setVisible(false);

        encryptButton.setEnabled(false);
        decryptButton.setEnabled(false);
        statusMessageLabel.setText("Ready. Select a file to begin.");
    }

    private ImageIcon loadIcon(String resourcePath, int width, int height) {
        URL url = getClass().getResource(resourcePath);
        if (url != null) {
            ImageIcon icon = new ImageIcon(url);
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
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
        }
    }

    private void handleBrowse() {
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            setSelectedFile(fileChooser.getSelectedFile());
        }
    }

    private void handleProcess(boolean isEncrypt) {
        if (selectedFile == null || !selectedFile.exists()) {
            JOptionPane.showMessageDialog(this, "Please select an existing file first.", "File Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String keyText = keyInputField.getText().trim();
        long derivedKey;
        try {
            derivedKey = KeyDerivation.deriveKey(keyText);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Key", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File destinationFile = promptSaveDestination(isEncrypt);
        if (destinationFile == null) {
            return;
        }

        String actionName = isEncrypt ? "Encryption" : "Decryption";
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
        statusMessageLabel.setText("Processing " + actionName.toLowerCase() + "...");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        SwingUtilities.invokeLater(() -> {
            try (InputStream in = new BufferedInputStream(new FileInputStream(selectedFile));
                 OutputStream out = new BufferedOutputStream(new FileOutputStream(destinationFile))) {

                if (isEncrypt) {
                    cipherEngine.encrypt(in, out, derivedKey);
                } else {
                    cipherEngine.decrypt(in, out, derivedKey);
                }

                progressBar.setVisible(false);
                statusMessageLabel.setText("✓ " + actionName + " completed: " + destinationFile.getName());
                JOptionPane.showMessageDialog(this,
                        actionName + " completed successfully!\n\nSaved to: " + destinationFile.getAbsolutePath(),
                        actionName + " Successful",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                progressBar.setVisible(false);
                statusMessageLabel.setText("Error during " + actionName.toLowerCase());
                JOptionPane.showMessageDialog(this,
                        "An error occurred while processing the file:\n" + ex.getMessage(),
                        "I/O Error",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                setCursor(Cursor.getDefaultCursor());
            }
        });
    }

    private File promptSaveDestination(boolean isEncrypt) {
        JFileChooser saveChooser = new JFileChooser();
        saveChooser.setDialogTitle(isEncrypt ? "Save Encrypted File" : "Save Decrypted File");
        saveChooser.addChoosableFileFilter(new FileFilters.SaveTextFileFilter());
        saveChooser.addChoosableFileFilter(new FileFilters.SaveJavaFileFilter());

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
