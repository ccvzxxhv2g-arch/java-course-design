package com.zct.frame;

import com.zct.bean.USER;
import com.zct.DAO.user_message;
import com.zct.tool.CommonUIUtils;
import com.zct.tool.ImageUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AddUserManagePanel extends JPanel {

    private JLabel avatarLabel = new JLabel(); // 头像标签
    private File selectedAvatarFile = null;   // 选中的头像文件

    // 输入字段
    private JTextField nameField;
    private JPasswordField passwordField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField addressField;

    public AddUserManagePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setMinimumSize(new Dimension(580, 600)); // 设置最小尺寸
        setPreferredSize(new Dimension(600, 700));
        setMaximumSize(new Dimension(620, 10000)); // 高度不限制
        setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));

        // 添加标题
        JPanel titleContent = CommonUIUtils.createTitlePanel("添加用户");
        titleContent.setAlignmentX(Component.LEFT_ALIGNMENT);

        Box titleBox = Box.createHorizontalBox();
        titleBox.add(titleContent);
        titleBox.add(Box.createHorizontalGlue()); // 右侧填充实现右对齐效果

        add(titleBox);
        add(Box.createRigidArea(new Dimension(0, 30)));

        // 添加各输入项
        add(createAvatarPanel());
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createNamePanel());
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createPasswordPanel());
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createPhonePanel());
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createEmailPanel());
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createAddressPanel());
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createButtonPanel());
    }

    // ================= 头像上传 ================= //

    private JPanel createAvatarPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("头像上传");
        label.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        label.setForeground(Color.BLACK);

        avatarLabel.setPreferredSize(new Dimension(150, 150));
        avatarLabel.setMaximumSize(new Dimension(150, 150));
        avatarLabel.setMinimumSize(new Dimension(150, 150));
        avatarLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JButton selectButton = new JButton();
        selectButton.setFocusPainted(false);
        selectButton.setBackground(Color.WHITE);
        selectButton.setBorder(null);

        BufferedImage defaultImage = ImageUtils.loadAndResizeImage(AddUserManagePanel.class, "/image/upload_picture.png", 30, 30);
        if (defaultImage != null) {
            selectButton.setIcon(new ImageIcon(defaultImage));
        } else {
            System.out.println("加载默认图片失败");
        }

        selectButton.addActionListener(this::selectAvatar);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 120)));
        buttonPanel.add(selectButton);

        panel.add(Box.createRigidArea(new Dimension(50, 0)));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(30, 0)));
        panel.add(avatarLabel);
        panel.add(buttonPanel);

        return panel;
    }

    private void selectAvatar(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("图片文件", "jpg", "png", "gif"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedAvatarFile = fileChooser.getSelectedFile();

            try {
                BufferedImage originalImage = ImageIO.read(selectedAvatarFile);
                if (originalImage == null) {
                    JOptionPane.showMessageDialog(this, "请选择有效的图片文件");
                    return;
                }

                BufferedImage resizedImage = ImageUtils.resizeImage(originalImage, 150, 150);
                avatarLabel.setIcon(new ImageIcon(resizedImage));
                avatarLabel.revalidate();
                avatarLabel.repaint();

            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "读取图片失败: " + ex.getMessage());
            }
        }
    }

    // ================= 输入框创建方法 ================= //

    private JPanel createNamePanel() {
        return createInputPanel("昵称", nameField = createTextField("", true));
    }

    private JPanel createPasswordPanel() {
        passwordField = createPasswordField("");
        return createInputPanel("密码", passwordField);
    }


    private JPanel createPhonePanel() {
        return createInputPanel("电话", phoneField = createTextField("", true));
    }

    private JPanel createEmailPanel() {
        return createInputPanel("邮箱", emailField = createTextField("", true));
    }

    private JPanel createAddressPanel() {
        return createInputPanel("地址", addressField = createTextField("", true));
    }

    // 创建带提示文本的输入框
    private JTextField createTextField(String hint, boolean editable) {
        JTextField field = new JTextField(hint);
        field.setEditable(editable);
        field.setMaximumSize(new Dimension(600, field.getPreferredSize().height + 10));
        field.setPreferredSize(new Dimension(600, field.getPreferredSize().height + 10));
        setupPlaceholder(field, hint);
        return field;
    }

    // 创建密码输入框
    private JPasswordField createPasswordField(String hint) {
        JPasswordField field = new JPasswordField(hint);
        field.setMaximumSize(new Dimension(600, field.getPreferredSize().height + 10));
        field.setPreferredSize(new Dimension(600, field.getPreferredSize().height + 10));
        setupPlaceholder(field, hint);
        return field;
    }

    // 带 placeholder 的输入框设置
    private void setupPlaceholder(JTextComponent field, String hint) {
        field.setText(hint);
        field.setForeground(Color.GRAY);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(hint)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(hint);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    // 创建通用输入行
    private JPanel createInputPanel(String labelText, JComponent inputComponent) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel(labelText + "：");
        label.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        label.setForeground(Color.BLACK);

        panel.add(Box.createRigidArea(new Dimension(50, 0)));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(20, 0)));
        panel.add(inputComponent);

        return panel;
    }

    // ================= 提交按钮 ================= //

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBackground(Color.WHITE);


        JButton saveButton = new JButton("保存");

        // 按钮样式

        saveButton.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        saveButton.setBackground(new Color(50, 125, 170));
        saveButton.setForeground(Color.WHITE);


        // 保存按钮
        saveButton.addActionListener(this::saveUserInfo);

        panel.add(saveButton);

        return panel;
    }

    // ================= 提交逻辑 ================= //

    private void saveUserInfo(ActionEvent e) {
        try {
            String name = nameField.getText().trim();
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars);
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String address = addressField.getText().trim();
            String avatarUrl = selectedAvatarFile != null ? selectedAvatarFile.getAbsolutePath() : "";

            // 校验必填字段
            if (name.isEmpty() || password.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写所有必填字段", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 构建 USER 对象
            USER user = new USER();
            user.setName(name);
            user.setPwd(password);
            user.setPhone(phone);
            user.setEmail(email);
            user.setAddress(address);
            user.setAvatarUrl(avatarUrl);

            // 调用 DAO 插入数据
            boolean success = user_message.insertUser2(user);
            if (success) {
                JOptionPane.showMessageDialog(this, "用户添加成功！");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "用户添加失败，请检查输入或数据库连接", "错误", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace(); // <<< 这里打印完整的异常堆栈
            JOptionPane.showMessageDialog(this, "发生异常：" + (ex.getMessage() != null ? ex.getMessage() : "未知错误"), "系统异常", JOptionPane.ERROR_MESSAGE);
        }

    }

    // 清空表单
    private void clearForm() {
        nameField.setText("");
        passwordField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");
        avatarLabel.setIcon(null);
        selectedAvatarFile = null;
    }
}
