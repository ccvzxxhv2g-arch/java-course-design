package com.zct.frame;

import com.zct.bean.USER;
import com.zct.DAO.user_message;
import com.zct.tool.CommonUIUtils;
import com.zct.tool.ImageUtils;
import com.zct.tool.SaveUserStateTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class userInfoPanel extends JPanel {
    private static final boolean DEBUG = true;
    private JPanel avatarPanel;
    private JPanel avatar_Panel;
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField addressField;
    private JTextField borrowLimitField;
    private JTextField emailField;
    private JTextField registerDateField;
    private JTextField lastLoginTimeField;

    public userInfoPanel() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));
        setBackground(Color.WHITE);
        setMinimumSize(new Dimension(580, 800));
        setPreferredSize(new Dimension(600, 900));
        setMaximumSize(new Dimension(620, 10000));

        JPanel titleContent = CommonUIUtils.createTitlePanel("用户信息");
        titleContent.setAlignmentX(Component.RIGHT_ALIGNMENT);

        Box titleBox = Box.createHorizontalBox();
        titleBox.add(titleContent);
        titleBox.add(Box.createRigidArea(new Dimension(10, 0)));
        titleBox.add(createRefreshButton());
        titleBox.add(Box.createHorizontalGlue()); // 右侧填充实现右对齐效果

        add(titleBox);
        add(Box.createRigidArea(new Dimension(0, 30)));
        String currentUserName = SaveUserStateTool.getUser().getName();
        USER user = user_message.getuserByName(currentUserName);

        if (user == null) {
            JOptionPane.showMessageDialog(this, "未找到当前用户信息", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        add(getAvatarPanel(user));
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(createFieldPanel("姓名:", nameField = new JTextField(user.getName())));
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createFieldPanel("状态:", new JTextField("用户")));
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createFieldPanel("地址:", addressField = new JTextField(user.getAddress())));
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createFieldPanel("手机号:", phoneField = new JTextField(user.getPhone())));
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createFieldPanel("电子邮箱:", emailField = new JTextField(user.getEmail())));
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createFieldPanel("可借阅数:", borrowLimitField = new JTextField(String.valueOf(user.getBorrowLimit()))));
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createFieldPanel("注册时间:", registerDateField = new JTextField(user.getRegisterDate())));
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createFieldPanel("最后登录:", lastLoginTimeField = new JTextField(user.getLastLoginTime())));
    }

    private JPanel createFieldPanel(String labelText, JTextField textField) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 40));
        panel.setMaximumSize(new Dimension(300, 40));
        panel.setMinimumSize(new Dimension(300, 40));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        panel.setBackground(Color.WHITE);

        // 设置固定大小的标签
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(80, 30)); // 增加标签宽度
        label.setFont(new Font("微软雅黑", Font.PLAIN, 14)); // 设置字体

        textField.setPreferredSize(new Dimension(100, 30)); // 增加字段宽度
        textField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        textField.setEditable(false);
        textField.setBackground(Color.WHITE);
        textField.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220))); // 添加边框

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(15, 0))); // 调整标签与字段间距
        panel.add(textField);

        panel.setMaximumSize(new Dimension(600, 40)); // 限制面板最大宽度

        return panel;
    }
    private JButton createRefreshButton() {
        JButton refreshBtn = new JButton();
        try {
            BufferedImage refreshIcon = ImageUtils.loadAndResizeImage(
                    getClass(),
                    "/image/sx.png",
                    20, 20
            );
            refreshBtn.setIcon(new ImageIcon(refreshIcon));
        } catch (Exception e) {
            refreshBtn.setText("刷新");
        }

        refreshBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        refreshBtn.setContentAreaFilled(false);
        refreshBtn.addActionListener(e -> refreshUserInfo());

        refreshBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                refreshBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                refreshBtn.setBackground(new Color(240, 240, 240));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                refreshBtn.setCursor(Cursor.getDefaultCursor());
                refreshBtn.setBackground(null);
            }
        });

        return refreshBtn;
    }

    private void refreshUserInfo() {
        SwingUtilities.invokeLater(() -> {
            try {
                USER currentUser = SaveUserStateTool.getUser();
                if (currentUser == null) {
                    showError("当前无用户登录状态");
                    return;
                }

                USER freshUser = user_message.getuserByName(currentUser.getName());
                if (freshUser == null) {
                    showError("获取最新用户信息失败");
                    return;
                }

                // 更新全局状态
                SaveUserStateTool.setUser(freshUser);

                // 直接更新各个字段
                nameField.setText(freshUser.getName());
                phoneField.setText(freshUser.getPhone());
                addressField.setText(freshUser.getAddress());
                borrowLimitField.setText(String.valueOf(freshUser.getBorrowLimit()));
                emailField.setText(freshUser.getEmail());
                registerDateField.setText(freshUser.getRegisterDate());
                lastLoginTimeField.setText(freshUser.getLastLoginTime());

                // 更新头像
                getAvatarPanel(freshUser);

                revalidate();
                repaint();

            } catch (Exception e) {
                log("刷新数据时出错: " + e.getMessage());
                e.printStackTrace();
                showError("刷新数据时出错: " + e.getMessage());
            }
        });
    }

    private JPanel getAvatarPanel(USER user) {
        avatarPanel = new JPanel();
        avatarPanel.setLayout(new BoxLayout(avatarPanel, BoxLayout.X_AXIS));
        avatarPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        avatarPanel.setMaximumSize(new Dimension(600, 110));
        avatarPanel.setPreferredSize(new Dimension(600, 110));
        avatarPanel.setMinimumSize(new Dimension(600, 110));
        avatarPanel.setBackground(Color.WHITE);
        avatarPanel.add(Box.createRigidArea(new Dimension(170, 0)));
        avatarPanel.add(getAvatarPanelContent(user));
        return avatarPanel;
    }

    private JPanel getAvatarPanelContent(USER user) {
        avatar_Panel = new JPanel();
        avatar_Panel.setLayout(null);
        avatar_Panel.setBounds(10, 30, 100, 100);
        avatar_Panel.setBackground(new Color(0, 0, 0, 0));

        admin_frame.CircularAvatarLabel avatarLabel = new admin_frame.CircularAvatarLabel();
        avatarLabel.setBounds(0, 0, 100, 100);

        try {
            BufferedImage img = ImageUtils.loadAndResizeImage(
                    admin_frame.class,
                    user.getAvatarUrl(),
                    100, 100
            );
            avatarLabel.setImage(img);
        } catch (Exception e) {
            log("头像加载失败: " + e.getMessage());
            avatarLabel.setText("头像");
        }

        avatar_Panel.add(avatarLabel);
        return avatar_Panel;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "错误", JOptionPane.ERROR_MESSAGE);
    }

    private void log(String message) {
        if (DEBUG) {
            System.out.println("[UserInfoPanel] " + message);
        }
    }
}
