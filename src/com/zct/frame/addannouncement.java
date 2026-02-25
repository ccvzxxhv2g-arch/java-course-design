package com.zct.frame;

import com.zct.DAO.announcement_dao;
import com.zct.bean.Announcement;
import com.zct.tool.CommonUIUtils;
import com.zct.tool.SaveUserStateTool;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class addannouncement extends JPanel {
    private JTextField titleField;
    private JTextArea contentArea;

    public addannouncement() {
        // 设置主面板为纵向 BoxLayout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setMinimumSize(new Dimension(600, 800));
        setPreferredSize(new Dimension(600, 800));
        setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));

        // 标题栏
        JPanel titleContent = CommonUIUtils.createTitlePanel("发布公告");
        titleContent.setAlignmentX(Component.LEFT_ALIGNMENT);

        Box titleBox = Box.createHorizontalBox();
        titleBox.add(titleContent);
        titleBox.add(Box.createHorizontalGlue()); // 让右边空白撑开，实现左对齐效果

        add(titleBox);
        add(Box.createRigidArea(new Dimension(0, 40)));

        // 标题输入框
        add(createTitleInput());
        add(Box.createRigidArea(new Dimension(0, 20)));
        // 内容输入框
        add(createContentInput());
        add(Box.createRigidArea(new Dimension(0, 180)));
        // 底部提交按钮
        add(createSubmitButtonPanel());

        // 自动刷新布局
        revalidate();
        repaint();
    }

    /**
     * 创建标题输入组件
     */
    private JPanel createTitleInput() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel("公告标题:");
        label.setPreferredSize(new Dimension(80, 30));

        titleField = new JTextField(20);
        titleField.setMaximumSize(new Dimension(500, 30));
        titleField.setMinimumSize(new Dimension(300, 30));
        titleField.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(20, 0)));
        panel.add(titleField);

        return panel;
    }

    /**
     * 创建内容输入组件
     */
    private JPanel createContentInput() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel("公告内容:");
        label.setPreferredSize(new Dimension(80, 300));
        label.setHorizontalAlignment(SwingConstants.RIGHT);

        contentArea = new JTextArea(5, 40);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setMaximumSize(new Dimension(500, 600));
        scrollPane.setMinimumSize(new Dimension(300, 300));

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(20, 0)));
        panel.add(scrollPane);

        return panel;
    }

    /**
     * 创建底部提交按钮区域
     */
    private JPanel createSubmitButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(Color.WHITE);

        JButton submitButton = new JButton("发布");
        submitButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String content = contentArea.getText().trim();

            if (title.isEmpty() || content.isEmpty()) {
                JOptionPane.showMessageDialog(this, "标题和内容不能为空", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 获取当前管理员名称
            String publisherName =  SaveUserStateTool.getAdmin().getName();

            // 创建公告对象并填充字段
            Announcement announcement = new Announcement();
            announcement.setTitle(title);
            announcement.setContent(content);
            announcement.setPublisherName(publisherName); // 设置发布人
            announcement.setPublishTime(LocalDateTime.now()); // 设置发布时间

            // 调用 DAO 添加到数据库
            boolean success = new announcement_dao().addAnnouncement(announcement);

            if (success) {
                JOptionPane.showMessageDialog(this, "公告已发布！", "成功", JOptionPane.INFORMATION_MESSAGE);

                // 清空输入框
                titleField.setText("");
                contentArea.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "发布公告失败，请检查数据库连接或输入内容", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(submitButton);
        return panel;
    }
}
