/*
 * Copyright (c) 2026 Adarsh Aher
 *
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 */
package io.github.adarshh025.encryptionsystem;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import io.github.adarshh025.encryptionsystem.ui.CryptographyFrame;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

/**
 * Programmatically launches the application and captures pixel-perfect high-resolution screenshots.
 */
public class CaptureScreenshots {

    public static void main(String[] args) throws Exception {
        File outDir = new File("assets/screenshots");
        outDir.mkdirs();

        // 1. Capture Light Mode - Overview
        FlatLightLaf.setup();
        FlatLaf.updateUI();

        CryptographyFrame frame1 = new CryptographyFrame();
        frame1.setVisible(true);
        Thread.sleep(700);
        saveFrame(frame1, new File(outDir, "app_overview.png"));

        // 2. Capture Light Mode - File Selected
        File sampleFile = new File("NetworkSecurityDemo.java");
        try (FileWriter fw = new FileWriter(sampleFile)) {
            fw.write("// Sample Java file for encryption demonstration\n"
                    + "public class NetworkSecurityDemo {\n"
                    + "    public static void main(String[] args) {\n"
                    + "        System.out.println(\"Confidential Network Security Algorithm\");\n"
                    + "    }\n"
                    + "}\n");
        }
        frame1.setSelectedFile(sampleFile);
        Thread.sleep(400);
        saveFrame(frame1, new File(outDir, "file_selected.png"));
        frame1.dispose();
        sampleFile.delete();

        // 3. Capture Dark Mode
        FlatDarkLaf.setup();
        FlatLaf.updateUI();

        CryptographyFrame frame2 = new CryptographyFrame();
        frame2.setVisible(true);
        Thread.sleep(700);
        saveFrame(frame2, new File(outDir, "app_dark_mode.png"));
        frame2.dispose();

        System.out.println("Screenshots generated successfully!");
    }

    private static void saveFrame(CryptographyFrame frame, File target) throws Exception {
        int w = frame.getWidth();
        int h = frame.getHeight();
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        frame.paint(g2);
        g2.dispose();
        ImageIO.write(img, "png", target);
        System.out.println("Saved " + target.getName() + " (" + target.length() + " bytes)");
    }
}
