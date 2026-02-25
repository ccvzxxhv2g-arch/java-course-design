package com.zct.frame;

import com.zct.bean.ADMIN;
import com.zct.DAO.attendant_message;
import com.zct.tool.CommonUIUtils;
import com.zct.tool.ImageUtils;
import com.zct.tool.SaveUserStateTool;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class Editadminpanel extends JPanel {

    private ADMIN currentAdmin;
    private JTextField nameField, telephoneField, addressField, emailField, numberField, passwordField;
    private JLabel avatarLabel;
    private JButton saveButton, changeAvatarBtn;
    private BufferedImage currentAvatar;

    public Editadminpanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));

        JPanel titleContent = CommonUIUtils.createTitlePanel("修改信息");
        titleContent.setAlignmentX(Component.LEFT_ALIGNMENT);

        Box titleBox = Box.createHorizontalBox();
        titleBox.add(titleContent);
        titleBox.add(Box.createHorizontalGlue()); // 右侧填充实现右对齐效果

         add(titleBox);
          add(Box.createRigidArea(new Dimension(0, 30)));
        // 从数据库加载当前管理员信息
        String currentAdminName = SaveUserStateTool.getAdmin().getName();
        currentAdmin = attendant_message.getAdminByName(currentAdminName);
        if (currentAdmin == null) {
            JOptionPane.showMessageDialog(this, "未找到当前管理员", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        add(createAvatarPanel());
        add(Box.createRigidArea(new Dimension(0, 20)));
        SaveUserStateTool.setAdmin(currentAdmin); // 更新缓存

        // 初始化输入框（使用 currentAdmin 数据）
        add(createInputPanel("用户名", nameField = createTextField(currentAdmin.getName(), true)));
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(createInputPanel("密码", passwordField = createTextField(currentAdmin.getPwd(), true)));
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(createInputPanel("电话", telephoneField = createTextField(currentAdmin.getPhone(), true)));
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(createInputPanel("地址", addressField = createTextField(currentAdmin.getAddress(), true)));
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(createInputPanel("邮箱", emailField = createTextField(currentAdmin.getEmail(), true)));
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(createInputPanel("可借阅数", numberField = createTextField(String.valueOf(currentAdmin.getBorrowLimit()), true)));
        add(Box.createRigidArea(new Dimension(0, 30)));

        add(createSaveButtonPanel());
    }

    // 创建头像面板
    private JPanel createAvatarPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBackground(Color.WHITE);

        String avatarUrl = SaveUserStateTool.getAdmin().getAvatarUrl();
        currentAvatar = ImageUtils.loadAndResizeImage(Editadminpanel.class, avatarUrl, 100, 100);
        avatarLabel = new JLabel(new ImageIcon(currentAvatar));
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        changeAvatarBtn = new JButton("选择新头像");
        changeAvatarBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        changeAvatarBtn.addActionListener(this::changeAvatar);

        panel.add(avatarLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(changeAvatarBtn);

        return panel;
    }

    // 创建带标签的输入行
    private JPanel createInputPanel(String labelText, JComponent inputComponent) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel(labelText + ":");
        label.setPreferredSize(new Dimension(80, 30));
        label.setMaximumSize(label.getPreferredSize());
        label.setMinimumSize(label.getPreferredSize());
        label.setForeground(Color.BLACK);

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(20, 0)));
        panel.add(inputComponent);

        return panel;
    }

    // 创建文本框
    private JTextField createTextField(String text, boolean editable) {
        JTextField field = new JTextField(text);
        field.setEditable(editable);
        field.setPreferredSize(new Dimension(200, 30));
        field.setMaximumSize(field.getPreferredSize());
        field.setMinimumSize(field.getPreferredSize());
        field.setBackground(Color.WHITE);
        return field;
    }

    // 创建保存按钮面板
    private JPanel createSaveButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.WHITE);

        saveButton = new JButton("保存");
        saveButton.setBackground(new Color(50, 125, 170));
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(this::saveAdminInfo);

        panel.add(saveButton);
        return panel;
    }

    // 更换头像事件
    private void changeAvatar(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                currentAvatar = ImageIO.read(selectedFile);
                avatarLabel.setIcon(new ImageIcon(currentAvatar.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
                currentAdmin.setAvatarUrl(selectedFile.getAbsolutePath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "无法加载所选图片", "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    // 保存管理员信息
    private void saveAdminInfo(ActionEvent e) {
        try {
            // 获取原始用户名（修改前）
            String originalName = currentAdmin.getName();

            // 获取表单数据
            String newName = nameField.getText().trim();
            String newPwd = passwordField.getText().trim();
            String newPhone = telephoneField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newAddress = addressField.getText().trim();
            int newBorrowLimit;

            try {
                newBorrowLimit = Integer.parseInt(numberField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "请输入有效的数字格式", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 设置新的管理员信息
            currentAdmin.setName(newName);
            currentAdmin.setPwd(newPwd);
            currentAdmin.setPhone(newPhone);
            currentAdmin.setEmail(newEmail);
            currentAdmin.setAddress(newAddress);
            currentAdmin.setBorrowLimit(newBorrowLimit);

            // 调用 DAO 更新数据库，传入原始用户名和新数据
            boolean success = attendant_message.updateUserInfo(currentAdmin, originalName);

            if (success) {
                JOptionPane.showMessageDialog(this, "管理员信息更新成功");
                SaveUserStateTool.setAdmin(currentAdmin); // 刷新缓存


                if (!originalName.equals(newName)) {

                }
            } else {
                JOptionPane.showMessageDialog(this, "更新失败", "错误", JOptionPane.ERROR_MESSAGE);

                // 恢复原始用户名（因为更新失败）
                currentAdmin.setName(originalName);
                nameField.setText(originalName);
            }

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "验证失败", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "发生异常：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

}
