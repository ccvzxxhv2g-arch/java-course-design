package com.zct.frame;

import com.zct.bean.ADMIN;
import com.zct.DAO.attendant_message;
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
public class adminInfoPanel extends JPanel {
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

    public adminInfoPanel() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));
        setBackground(Color.WHITE);

        JPanel titleContent = CommonUIUtils.createTitlePanel("管理员信息");
        titleContent.setAlignmentX(Component.RIGHT_ALIGNMENT);

        Box titleBox = Box.createHorizontalBox();
        titleBox.add(titleContent);
        titleBox.add(Box.createRigidArea(new Dimension(10, 0)));
        titleBox.add(createRefreshButton());
        titleBox.add(Box.createHorizontalGlue()); // 右侧填充实现右对齐效果

         add(titleBox);
        add(Box.createRigidArea(new Dimension(0, 30)));
        String currentAdminName = SaveUserStateTool.getAdmin().getName();
        ADMIN admin = attendant_message.getAdminByName(currentAdminName);

        if (admin == null) {
            JOptionPane.showMessageDialog(this, "未找到当前管理员信息", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        add(getAvatarPanel(admin));
         add(Box.createRigidArea(new Dimension(0, 10)));
        add(createFieldPanel("姓名:", nameField = new JTextField(admin.getName())));
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createFieldPanel("状态:", new JTextField("管理员")));
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createFieldPanel("地址:", addressField = new JTextField(admin.getAddress())));
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createFieldPanel("手机号:", phoneField = new JTextField(admin.getPhone())));
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createFieldPanel("电子邮箱:", emailField = new JTextField(admin.getEmail())));
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createFieldPanel("可借阅数:", borrowLimitField = new JTextField(String.valueOf(admin.getBorrowLimit()))));
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createFieldPanel("注册时间:", registerDateField = new JTextField(admin.getRegisterDate())));
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createFieldPanel("最后登录:", lastLoginTimeField = new JTextField(admin.getLastLoginTime())));
    }



    private JPanel createFieldPanel(String labelText, JTextField textField) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(70, 30));
        label.setMaximumSize(label.getPreferredSize());
        label.setMinimumSize(label.getPreferredSize());

        textField.setPreferredSize(new Dimension(100, 30));
        textField.setEditable(false);
        textField.setBackground(Color.WHITE);

        panel.add(Box.createRigidArea( new Dimension(240, 0)));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(20, 0)));
        panel.add(textField);

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
        refreshBtn.addActionListener(e -> refreshAdminInfo());

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

    private void refreshAdminInfo() {
        SwingUtilities.invokeLater(() -> {
            try {
                ADMIN currentAdmin = SaveUserStateTool.getAdmin();
                if (currentAdmin == null) {
                    showError("当前无管理员登录状态");
                    return;
                }

                ADMIN freshAdmin = attendant_message.getAdminByName(currentAdmin.getName());
                if (freshAdmin == null) {
                    showError("获取最新管理员信息失败");
                    return;
                }

                // 更新全局状态
                SaveUserStateTool.setAdmin(freshAdmin);

                // 直接更新各个字段
                nameField.setText(freshAdmin.getName());
                phoneField.setText(freshAdmin.getPhone());
                addressField.setText(freshAdmin.getAddress());
                borrowLimitField.setText(String.valueOf(freshAdmin.getBorrowLimit()));
                emailField.setText(freshAdmin.getEmail());
                registerDateField.setText(freshAdmin.getRegisterDate());
                lastLoginTimeField.setText(freshAdmin.getLastLoginTime());

                // 更新头像
                getAvatarPanel(freshAdmin);

                revalidate();
                repaint();

            } catch (Exception e) {
                log("刷新数据时出错: " + e.getMessage());
                e.printStackTrace();
                showError("刷新数据时出错: " + e.getMessage());
            }
        });
    }

    private JPanel getAvatarPanel(ADMIN admin) {
        avatarPanel = new JPanel();
        avatarPanel.setLayout(new BoxLayout(avatarPanel, BoxLayout.X_AXIS));
        avatarPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        avatarPanel.setMaximumSize(new Dimension(600, 110));
        avatarPanel.setPreferredSize(new Dimension(600, 110));
        avatarPanel.setMinimumSize(new Dimension(600, 110));
        avatarPanel.setBackground(Color.WHITE);
        avatarPanel.add(Box.createRigidArea(new Dimension(170, 0)));
        avatarPanel.add(getAvatarPanelContent(admin));
        return avatarPanel;
    }

    private JPanel getAvatarPanelContent(ADMIN admin) {
        avatar_Panel = new JPanel();
        avatar_Panel.setLayout(null);
        avatar_Panel.setBounds(10, 30, 100, 100);
        avatar_Panel.setBackground(new Color(0, 0, 0, 0));

        admin_frame.CircularAvatarLabel avatarLabel = new admin_frame.CircularAvatarLabel();
        avatarLabel.setBounds(0, 0, 100, 100);

        try {
            BufferedImage img = ImageUtils.loadAndResizeImage(
                    admin_frame.class,
                    admin.getAvatarUrl(),
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
            System.out.println("[AdminInfoPanel] " + message);
        }
    }
}
