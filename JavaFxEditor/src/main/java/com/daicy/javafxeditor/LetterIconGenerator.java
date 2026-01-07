package com.daicy.javafxeditor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class LetterIconGenerator {

    // 生成随机颜色
    private static Color generateRandomColor() {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return new Color(r, g, b);
    }

    public static void generateLetterIcon(char letter, String outputPath, int width, int height, double textRatio) {
        // 创建一个 BufferedImage 对象
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // 设置抗锯齿，让文字和图形边缘更平滑
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 设置背景颜色
        g2d.setColor(generateRandomColor());
        g2d.fillRect(0, 0, width, height);

        // 尝试不同的字体大小，直到找到合适的大小
        int fontSize = 1;
        Font font;
        FontMetrics fontMetrics;
        int stringWidth;
        int stringHeight;
        do {
            fontSize++;
            font = new Font("Arial", Font.BOLD, fontSize);
            System.out.println("字体大小 " + fontSize);
            g2d.setFont(font);
            fontMetrics = g2d.getFontMetrics();
            stringWidth = fontMetrics.stringWidth(String.valueOf(letter));
            stringHeight = fontMetrics.getHeight();
        } while (stringWidth < width * textRatio && stringHeight < height * textRatio);

        // 调整字体大小以确保不超出范围
        if (stringWidth > width * textRatio || stringHeight > height * textRatio) {
            fontSize--;
            font = new Font("Arial", Font.BOLD, fontSize);
            System.out.println("字体大小调整到 " + fontSize);
            g2d.setFont(font);
            fontMetrics = g2d.getFontMetrics();
            stringWidth = fontMetrics.stringWidth(String.valueOf(letter));
            stringHeight = fontMetrics.getHeight();
        }

        // 设置字体颜色
        g2d.setColor(Color.WHITE);

        // 计算文本的位置，使其居中显示
        int x = (width - stringWidth) / 2;
        int y = (height - stringHeight) / 2 + fontMetrics.getAscent();

        // 绘制文本
        g2d.drawString(String.valueOf(letter), x, y);

        g2d.dispose();

        // 保存图像为 PNG 文件
        try {
            File outputFile = new File(outputPath);
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        char letter = 'B';
        String outputPath = "letter_icon.png";
        int width = 100;
        int height = 100;
        double textRatio = 0.9; // 文字占图片空间的比例，这里设置为 90%
        generateLetterIcon(letter, outputPath, width, height, textRatio);
    }
}