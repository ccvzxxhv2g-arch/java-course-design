package com.zct.frame;

import com.zct.DAO.announcement_dao;
import com.zct.bean.Announcement;
import com.zct.tool.CommonUIUtils;
import com.zct.tool.ImageUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class home_panel extends JPanel {
    public home_panel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));

        // 标题部分
        JPanel titleContent = CommonUIUtils.createTitlePanel("首页");
        titleContent.setAlignmentX(Component.LEFT_ALIGNMENT);
        Box titleBox = Box.createHorizontalBox();
        titleBox.add(titleContent);
        titleBox.add(Box.createHorizontalGlue());

        // 标题面板
        JPanel titleContent2 = CommonUIUtils.createTitlePanel("公告列表");
        titleContent2.setAlignmentX(Component.LEFT_ALIGNMENT);

// 创建刷新按钮
        JButton refreshButton = new JButton();
        refreshButton.setBorder(BorderFactory.createEmptyBorder()); // 去边框
        refreshButton.setBackground(Color.WHITE); // 白色背景

        BufferedImage refreshIcon = ImageUtils.loadAndResizeImage(getClass(), "/image/sx.png", 20, 20);
        if (refreshIcon != null) {
            refreshButton.setIcon(new ImageIcon(refreshIcon));
        } else {
            System.err.println("无法加载刷新图标");
        }

// 设置按钮动作：刷新公告列表
        refreshButton.addActionListener(e -> refresh());

// 将标题和按钮放入水平盒子中
        Box titleBox2 = Box.createHorizontalBox();
        titleBox2.add(titleContent2);
        titleBox2.add(Box.createHorizontalGlue()); // 占据中间空间
        titleBox2.add(refreshButton); // 刷新按钮靠右显示

        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.X_AXIS));
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setPreferredSize(new Dimension(720, 300));
        imagePanel.setMaximumSize(new Dimension(720, 300));
        imagePanel.setMinimumSize(new Dimension(720, 300));
        ImageCarouselPanel carouselPanel = new ImageCarouselPanel();
        carouselPanel.setBackground(Color.BLUE);
        imagePanel.add(Box.createRigidArea(new Dimension(120, 0)));
        imagePanel.add(carouselPanel);

        add(titleBox);
        add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(imagePanel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(titleBox2);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(createNoticeList());

    }
    private class ImageCarouselPanel extends JPanel {
        private static final int PANEL_WIDTH = 600; // 面板宽度
        private static final int PANEL_HEIGHT = 300; // 面板高度
        private static final int DELAY = 3000; // 切换间隔：3 秒

        private java.util.List<BufferedImage> images = new ArrayList<>();
        private int currentIndex = 0;

        public ImageCarouselPanel() {
            setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
            setDoubleBuffered(true);

            // 加载图片资源
            String[] imagePaths = {
                    "/image/weic.png",
                    "/image/v2-cf2d4df0323f7340b4e56b9f9808696e_r.jpg",
            };

            for (String path : imagePaths) {
                BufferedImage img = loadScaledImage(path, PANEL_WIDTH, PANEL_HEIGHT);
                if (img != null) {
                    images.add(img);
                } else {
                    System.err.println("Failed to load image: " + path);
                }
            }

            // 启动定时器切换图片
            Timer timer = new Timer(DELAY, e -> {
                if (images.size() <= 1) return;
                currentIndex = (currentIndex + 1) % images.size();
                repaint();
            });
            timer.start();
        }

        /**
         * 使用 ImageIO 加载并缩放图片
         */
        private BufferedImage loadScaledImage(String path, int targetWidth, int targetHeight) {
            try (InputStream is = getClass().getResourceAsStream(path)) {
                if (is == null) {
                    System.err.println("Resource not found: " + path);
                    return null;
                }

                BufferedImage original = ImageIO.read(is);
                BufferedImage scaled = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = scaled.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.drawImage(original, 0, 0, targetWidth, targetHeight, null);
                g2d.dispose();

                return scaled;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (images.isEmpty()) return;

            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 绘制当前图片
            if (currentIndex < images.size()) {
                BufferedImage currentImage = images.get(currentIndex);
                g2d.drawImage(currentImage, 0, 0, this);
            }

            g2d.dispose();
        }
    }
    private JScrollPane createNoticeList() {
        // 模拟数据（后续应从数据库加载）
        List<Announcement> notices = getSampleNotices();

        // 创建表格面板
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);

        for (Announcement notice : notices) {
            listPanel.add(createNoticeItem(notice));
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        // 返回带滚动条的面板
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        return scrollPane;
    }

    /**
     * 创建单个公告项
     */
    private JPanel createNoticeItem(Announcement notice) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BorderLayout(10, 10));
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // 左侧信息区
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 1, 5, 5));
        infoPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("标题：" + notice.getTitle() );
        JLabel contentLabel = new JLabel("内容： " + notice.getContent().substring(0, Math.min(50, notice.getContent().length()))+"..." );
        JLabel metaLabel = new JLabel("发布时间：" + notice.getPublishTime() + " | 发布者：" + notice.getPublisherName());

        infoPanel.add(titleLabel);
        infoPanel.add(contentLabel);
        infoPanel.add(metaLabel);

        // 右侧按钮
        JButton detailButton = new JButton();
        detailButton.setBorder(BorderFactory.createEmptyBorder());
        detailButton.setBackground(Color.WHITE);
        BufferedImage img = ImageUtils.loadAndResizeImage(BookListPanel.class, "/image/xq.png", 60, 50);
        if (img != null)
        {
            detailButton.setIcon(new ImageIcon(img));
        }
        else
        {
            System.err.println("无法加载图片");
        }
        detailButton.setPreferredSize(new Dimension(60, 40)); // 设置按钮宽高
        detailButton.setMaximumSize(new Dimension(30, 10));   // 防止被布局拉伸
        detailButton.setMinimumSize(new Dimension(30, 10));
        JPanel buttonWrapper = new JPanel();
        buttonWrapper.setBackground(Color.WHITE);
        buttonWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15)); // 精确控制位置
        buttonWrapper.add(detailButton);
        detailButton.addActionListener((ActionEvent e) -> {
            showDetailDialog(notice);
        });

        // 添加到主面板
        itemPanel.add(infoPanel, BorderLayout.CENTER);
        itemPanel.add(buttonWrapper, BorderLayout.EAST);

        return itemPanel;
    }

    /**
     * 显示公告详情弹窗
     */
    /**
     * 显示公告详情弹窗
     */
    private void showDetailDialog(Announcement notice) {
        // 创建标题标签
        JLabel titleLabel = new JLabel( notice.getTitle());
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));

        // 创建内容文本区域
        JTextArea textArea = new JTextArea(notice.getContent());
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setBackground(Color.WHITE);
        textArea.setForeground(Color.BLACK);
        textArea.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体和大小
        JScrollPane contentScrollPane = new JScrollPane(textArea);

        // 使用 JPanel 包裹标题和内容
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(titleLabel);
        panel.add(titlePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(contentScrollPane);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        contentScrollPane.setPreferredSize(new Dimension(400, 300));

        // 显示弹窗
        JOptionPane.showMessageDialog(this,
                panel,
                "公告详情",
                JOptionPane.PLAIN_MESSAGE);
    }
    public void refresh() {
        // 重新加载并刷新公告列表
        Component scrollPane = getComponent(6);
        if (scrollPane instanceof JScrollPane) {
            JPanel listPanel = (JPanel) ((JScrollPane) scrollPane).getViewport().getView();
            listPanel.removeAll();

            List<Announcement> notices = getSampleNotices(); // 从数据库获取最新数据
            for (Announcement notice : notices) {
                listPanel.add(createNoticeItem(notice));
                listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }

            revalidate();
            repaint();
        }
    }


    /**
     * 从数据库获取公告列表
     */
    private List<Announcement> getSampleNotices() {
        List<Announcement> list = new ArrayList<>();

        try {
            announcement_dao dao = new announcement_dao();
            list = dao.getAllAnnouncements(); // 调用 DAO 查询所有公告
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "加载公告失败，请检查数据库连接", "错误", JOptionPane.ERROR_MESSAGE);
        }

        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "暂无公告信息", "提示", JOptionPane.INFORMATION_MESSAGE);
        }

        return list;
    }

}
