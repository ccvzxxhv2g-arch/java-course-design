package com.zct.frame;

import com.zct.bean.USER;
import com.zct.DAO.user_message;
import com.zct.tool.ImageUtils;
import com.zct.tool.SaveUserStateTool;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class Edituserpanel extends JPanel {

    private USER currentUser;
    private JTextField nameField, telephoneField, addressField, emailField, numberField, passwordField;
    private JLabel avatarLabel;
    private JButton saveButton, changeAvatarBtn;
    private BufferedImage currentAvatar;

    public Edituserpanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));

        // 从数据库加载完整用户信息
        String currentUserName = SaveUserStateTool.getUser().getName();
        currentUser = user_message.getuserByName(currentUserName);
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "未找到当前用户", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        add(createAvatarPanel());
        add(Box.createRigidArea(new Dimension(0, 20)));
        SaveUserStateTool.setUser(currentUser);

        // 初始化界面字段全部来自 currentUser
        add(createInputPanel("用户名", nameField = createTextField(currentUser.getName(), true)));
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(createInputPanel("密码", passwordField = createTextField(currentUser.getPwd(), true)));
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(createInputPanel("电话", telephoneField = createTextField(currentUser.getPhone(), true)));
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(createInputPanel("地址", addressField = createTextField(currentUser.getAddress(), true)));
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(createInputPanel("邮箱", emailField = createTextField(currentUser.getEmail(), true)));
        add(Box.createRigidArea(new Dimension(0, 30)));

        add(createSaveButtonPanel());
    }

    // 创建头像区域
    private JPanel createAvatarPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBackground(Color.WHITE);

        String avatarUrl = SaveUserStateTool.getUser().getAvatarUrl();
        currentAvatar = ImageUtils.loadAndResizeImage(Edituserpanel.class, avatarUrl, 100, 100);
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

    // 创建输入项（标签 + 输入框）
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
        field.setPreferredSize(new Dimension(200, 30)); // 统一大小
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
        saveButton.addActionListener(this::saveUserInfo);

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
                currentUser.setAvatarUrl(selectedFile.getAbsolutePath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "无法加载所选图片", "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    // 保存用户信息到数据库
    private void saveUserInfo(ActionEvent e) {
        try {
            // 保存原始用户名（修改前）
            String originalName = currentUser.getName();

            // 获取表单数据
            String newName = nameField.getText().trim();
            String newPwd = passwordField.getText().trim();
            String newPhone = telephoneField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newAddress = addressField.getText().trim();

            // 更新用户对象
            currentUser.setName(newName);
            currentUser.setPwd(newPwd);
            currentUser.setPhone(newPhone);
            currentUser.setEmail(newEmail);
            currentUser.setAddress(newAddress);

            // 调用 DAO 更新数据库，传入原始用户名和新数据
            boolean success = user_message.updateUser(
                    originalName,  // 使用原始用户名作为查询条件
                    newName,
                    newPwd,
                    currentUser.getAvatarUrl(),
                    newPhone,
                    newEmail,
                    newAddress
            );

            if (success) {
                JOptionPane.showMessageDialog(this, "用户信息更新成功！");
                SaveUserStateTool.setUser(currentUser); // 更新本地缓存

                // 如果修改了用户名，更新界面显示的当前用户名
                if (!originalName.equals(newName)) {
                    // 这里可能需要更新主界面显示的用户名
                }
            } else {
                JOptionPane.showMessageDialog(this, "更新失败，请检查输入或网络连接", "错误", JOptionPane.ERROR_MESSAGE);

                // 恢复原始用户名（因为更新失败）
                currentUser.setName(originalName);
                nameField.setText(originalName);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "发生异常：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

}
