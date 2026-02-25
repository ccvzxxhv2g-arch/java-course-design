package com.zct.tool;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageUtils {

    /**
     * 高质量缩放图像
     *
     * @param original 原始图像
     * @param width    目标宽度
     * @param height   目标高度
     * @return         缩放后的高质量图像
     */
    public static BufferedImage resizeImage(BufferedImage original, int width, int height) {
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resized.createGraphics();

        // 设置高质量渲染参数
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 绘制图像
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();

        return resized;
    }

    /**
     * 从 URL 加载图像并高质量缩放
     *
     * @param imageUrl 图像资源路径（如 "/image/user.png"）
     * @param width    目标宽度
     * @param height   目标高度
     * @return         缩放后的高质量图像，失败返回 null
     */
    public static BufferedImage loadAndResizeImage(Class<?> clazz, String imagePath, int width, int height) {
        try {
            BufferedImage original;

            // 尝试作为本地文件加载
            File file = new File(imagePath);
            if (file.exists()) {
                original = ImageIO.read(file);
            } else {
                // 否则尝试作为 classpath 资源加载
                URL url = clazz.getResource(imagePath);
                if (url == null) return null;
                original = ImageIO.read(url);
            }

            if (original == null) return null;

            return resizeImage(original, width, height);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
