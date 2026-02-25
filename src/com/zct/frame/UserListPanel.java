package com.zct.frame;

import com.zct.tool.CommonUIUtils;
import com.zct.tool.ImageUtils;
import com.zct.DAO.DAO;
import com.zct.bean.USER;
import com.zct.DAO.user_message;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserListPanel extends JPanel {
    private BookListPanel.RoundedPanel searchPanel = null;
    private JTextField searchField;

    public UserListPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));
        setBackground(Color.WHITE);
        setMinimumSize(new Dimension(580, 800));   // 最小高度要大于等于数据高度
        setPreferredSize(new Dimension(600, 900));  // 推荐高度 > 最小高度
        setMaximumSize(new Dimension(620, 10000));  // 最大高度可以无限扩展

        // 创建标题内容
        JPanel titleContent = CommonUIUtils.createTitlePanel("用户列表");
        titleContent.setAlignmentX(Component.LEFT_ALIGNMENT);

        Box titleBox = Box.createHorizontalBox();
        titleBox.add(titleContent);
        titleBox.add(Box.createHorizontalGlue()); // 右侧填充实现右对齐效果


        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        wrapper.setBackground(Color.white);
        wrapper.add(getSearchPanel());

        add(titleBox);
        add(Box.createRigidArea(new Dimension(0, 10)));

        add(wrapper);
        add(Box.createRigidArea(new Dimension(0, 30)));
        UserListTablePanel userListTablePanel = new UserListTablePanel();
        add(userListTablePanel);
    }

    private BookListPanel.RoundedPanel getSearchPanel() {
        searchPanel = new BookListPanel.RoundedPanel(30, 30, Color.WHITE);
        searchPanel.setMaximumSize(new Dimension(320, 33));
        searchPanel.setMinimumSize(new Dimension(320, 33));
        searchPanel.setPreferredSize(new Dimension(320, 33));

        this.searchField = new JTextField(20);

        JButton searchButton = new JButton();
        BufferedImage img = ImageUtils.loadAndResizeImage(BookListPanel.class, "/image/search_.png", 20, 20);
        if (img != null) {
            searchButton.setIcon(new ImageIcon(img));
        } else {
            System.err.println("无法加载图片");
        }
        searchButton.setBorder(null);
        searchButton.setBackground(Color.white);

        // 为搜索按钮添加点击事件处理
        searchButton.addActionListener(e -> {
            String searchValue = searchField.getText().trim();
            if (!searchValue.isEmpty()) {
                // 仅查询用户名
                List<USER> searchResult = user_message.queryUsers("name", searchValue);
                UserListTablePanel userListTablePanel = findUserListTablePanel();
                if (userListTablePanel != null) {
                    userListTablePanel.reloadTableData(searchResult);
                }
            } else {
                // 搜索框为空，显示所有用户
                UserListTablePanel userListTablePanel = findUserListTablePanel();
                if (userListTablePanel != null) {
                    userListTablePanel.reloadTableData(user_message.listUsers());
                }
            }
        });

        searchPanel.add(this.searchField);
        searchPanel.add(searchButton);
        return searchPanel;
    }

    private UserListTablePanel findUserListTablePanel() {
        for (Component component : getComponents()) {
            if (component instanceof UserListTablePanel) {
                return (UserListTablePanel) component;
            }
        }
        return null;
    }

    public class User {
        private String avatarPath;      // 头像路径
        private String username;        // 用户名
        private String phone;           // 电话
        private String email;           // 邮箱地址
        private String address;         // 地址
        private int borrow_limit;   // 可借阅数量
        private String registrationDate; // 注册日期
        private String lastLoginDate;   // 最后登录日期

        public User(String avatarPath, String username, String phone, String email,
                    String address, int borrow_limit, String registrationDate, String lastLoginDate) {
            this.avatarPath = avatarPath;
            this.username = username;
            this.phone = phone;
            this.email = email;
            this.address = address;
            this.borrow_limit = borrow_limit;
            this.registrationDate = registrationDate;
            this.lastLoginDate = lastLoginDate;
        }

        // Getter 方法
        public String getAvatarPath() { return avatarPath; }
        public String getUsername() { return username; }
        public String getPhone() { return phone; }
        public String getEmail() { return email; }
        public String getAddress() { return address; }
        public int getBorrow_limit() { return borrow_limit; }
        public String getRegistrationDate() { return registrationDate; }
        public String getLastLoginDate() { return lastLoginDate; }
    }

    public class UserTableModel extends AbstractTableModel {
        private List<User> users;
        private final String[] columnNames = {"头像", "用户名", "电话", "邮箱", "地址", "可借阅数量", "注册日期", "最后登录日期", "操作"};

        public UserTableModel(List<User> users) {
            this.users = users;
        }

        // 添加 setUsers 方法
        public void setUsers(List<User> users) {
            this.users = users;
            fireTableDataChanged(); // 通知表格数据更新
        }

        @Override
        public int getRowCount() {
            return users.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int row, int column) {
            User user = users.get(row);
            switch (column) {
                case 0: return user.getAvatarPath();       // 头像路径
                case 1: return user.getUsername();
                case 2: return user.getPhone();
                case 3: return user.getEmail();
                case 4: return user.getAddress();
                case 5: return user.getBorrow_limit();
                case 6: return user.getRegistrationDate(); // 注册日期
                case 7: return user.getLastLoginDate();    // 最后登录日期
                case 8: return new JButton[]{new JButton("编辑"), new JButton("删除")};
                default: return null;
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) {
                return ImageIcon.class;
            } else if (columnIndex == 8) {
                return JButton[].class;
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 8;
        }

        public User getUserAtRow(int row) {
            return users.get(row);
        }
    }

    public class UserListTablePanel extends JPanel {
        private JTable table;
        private UserTableModel model;

        public UserListTablePanel() {
            setLayout(new BorderLayout());

            // 从数据库获取用户列表
            List<com.zct.bean.USER> dbUsers = user_message.listUsers(); // 调用 DAO 获取用户列表
            List<User> userList = convertToTableModel(dbUsers); // 映射为 UserListTablePanel.User 类型

            model = new UserTableModel(userList);
            table = new JTable(model);

            // 设置表格渲染器
            table.setRowHeight(100);
            table.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());
            table.getColumnModel().getColumn(8).setCellRenderer(new MultiButtonRenderer());
            table.getColumnModel().getColumn(8).setCellEditor(new MultiButtonEditor(model, table, this)); // 传递 this 作为 UserListTablePanel 实例

            JScrollPane scrollPane = new JScrollPane(table);
            configureVerticalScrollBar(scrollPane);
            add(scrollPane, BorderLayout.CENTER);
        }

        // 将 com.zct.bean.USER 映射为 UserListTablePanel 内部类 User 类型
        private List<User> convertToTableModel(List<com.zct.bean.USER> dbUsers) {
            List<User> result = new ArrayList<>();
            for (com.zct.bean.USER dbUser : dbUsers) {
                result.add(new User(
                        dbUser.getAvatarUrl(),                   // 头像路径
                        dbUser.getName(),                        // 用户名
                        dbUser.getPhone(),                       // 手机号码
                        dbUser.getEmail(),                       // 邮箱
                        dbUser.getAddress(),                     // 地址
                        dbUser.getBorrowLimit(),                  // 可借阅数量
                        dbUser.getRegisterDate(),                // 注册时间
                        dbUser.getLastLoginTime()                // 最后登录时间
                ));
            }
            return result;
        }

        // 重新加载表格数据
        public void reloadTableData(List<USER> dbUsers) {
            List<User> userList = convertToTableModel(dbUsers);
            model.setUsers(userList); // 使用修改后的 setUsers 方法
        }
    }

    void configureVerticalScrollBar(JScrollPane scrollPane) {
        // 设置滚动速度
        scrollPane.getVerticalScrollBar().setUnitIncrement(13);

        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setPreferredSize(new Dimension(8, Integer.MAX_VALUE)); // 设置滚动条宽度
        verticalScrollBar.setMinimumSize(new Dimension(8, 20));
        verticalScrollBar.setBackground(Color.LIGHT_GRAY);

        // 自定义滚动条 UI
        verticalScrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.GRAY; // 设置滑块颜色
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return new JButton() {
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(0, 0); // 隐藏上箭头按钮
                    }
                };
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return new JButton() {
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(0, 0); // 隐藏下箭头按钮
                    }
                };
            }
        });
    }

    public class ImageRenderer extends JLabel implements TableCellRenderer {
        public ImageRenderer() {
            setOpaque(true);
            setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            if (value instanceof String) {
                String imagePath = (String) value;

                try {
                    // 使用 new File(...) 加载本地路径图片
                    File file = new File(imagePath);

                    if (file.exists()) {
                        BufferedImage originalImage = ImageIO.read(file);
                        Image scaledImage = originalImage.getScaledInstance(60, 80, Image.SCALE_SMOOTH);
                        setIcon(new ImageIcon(scaledImage));
                        setText("");
                    } else {
                        setIcon(null);
                        setText("??");
                        setForeground(Color.RED);
                        System.err.println("未找到图片文件，请检查路径是否正确。路径: " + imagePath);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    setIcon(null);
                    setText("??");
                    setForeground(Color.RED);
                }
            } else {
                setIcon(null);
                setText("");
            }

            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return this;
        }
    }

    public class MultiButtonRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // 改为垂直布局
            panel.setOpaque(true);
            panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());

            panel.removeAll(); // 清空面板，防止重复添加

            if (value instanceof JButton[]) {
                JButton[] buttons = (JButton[]) value;
                for (JButton button : buttons) {
                    button.setPreferredSize(new Dimension(80, 25));
                    button.setBackground(Color.LIGHT_GRAY);
                    button.setForeground(Color.BLACK);
                    button.setAlignmentX(Component.CENTER_ALIGNMENT); // 居中显示
                    panel.add(Box.createRigidArea(new Dimension(0, 13)));
                    panel.add(button);
                    panel.add(Box.createRigidArea(new Dimension(0, 10))); // 可选间距
                }
            }

            return panel;
        }
    }

    public class MultiButtonEditor extends AbstractCellEditor implements TableCellEditor {

        private JButton[] buttons;
        private JPanel panel;
        private User user;
        private UserTableModel model;
        private JTable table; // 刷新表格
        private UserListTablePanel userListTablePanel; // 保存 UserListTablePanel 实例

        public MultiButtonEditor(UserTableModel model, JTable table, UserListTablePanel userListTablePanel) {
            this.model = model;
            this.table = table;
            this.userListTablePanel = userListTablePanel; // 初始化实例
            this.panel = new JPanel();
            this.panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // 改为垂直布局

            this.buttons = new JButton[]{
                    new JButton("编辑"),
                    new JButton("删除")
            };

            for (JButton btn : buttons) {
                btn.setPreferredSize(new Dimension(80, 25));
                btn.setBackground(Color.LIGHT_GRAY);
                btn.setForeground(Color.BLACK);
                btn.setAlignmentX(Component.CENTER_ALIGNMENT); // 居中显示
                panel.add(Box.createRigidArea(new Dimension(0, 13)));
                panel.add(btn);
                panel.add(Box.createRigidArea(new Dimension(0, 10))); // 可选间距

                // 添加按钮事件
                btn.addActionListener(e -> {
                    if (e.getSource() == buttons[0]) {
                        handleEdit();
                    } else if (e.getSource() == buttons[1]) {
                        handleDelete();
                    }
                });
            }
        }

        // 处理编辑事件
        private void handleEdit() {
            if (user == null) return;

            // 表单构建（不含密码字段）
            Object[] inputs = {
                    "用户名:", new JTextField(user.getUsername(), 20),
                    "手机号:", new JTextField(user.getPhone(), 20),
                    "邮箱:", new JTextField(user.getEmail(), 20),
                    "地址:", new JTextField(user.getAddress(), 20),
                    "可借阅数:", new JTextField(String.valueOf(user.getBorrow_limit()), 10)
            };

            int result = JOptionPane.showConfirmDialog(panel, inputs, "编辑用户信息", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                // 获取表单值（注意顺序要与inputs一致）
                String newName = ((JTextField)inputs[1]).getText().trim();
                String newPhone = ((JTextField)inputs[3]).getText().trim();
                String newEmail = ((JTextField)inputs[5]).getText().trim();
                String newAddress = ((JTextField)inputs[7]).getText().trim();
                int newBorrow_limit;

                try {
                    newBorrow_limit = Integer.parseInt(((JTextField)inputs[9]).getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "可借阅数量必须为整数");
                    return;
                }

                // 调用专门的不修改密码的更新方法
                boolean success = user_message.updateUser2(
                        user.getUsername(), // 原用户名
                        newName,
                        "",
                        user.getAvatarPath(),
                        newPhone,
                        newEmail,
                        newAddress
                );

                if (success) {
                    JOptionPane.showMessageDialog(panel, "更新成功");
                    reloadTableData();
                } else {
                    JOptionPane.showMessageDialog(panel, "更新失败");
                }
            }
        }


        // 处理删除事件
        private void handleDelete() {
            if (user == null) return;
            int confirm = JOptionPane.showConfirmDialog(
                    panel,
                    "确认删除用户 [" + user.getUsername() + "] 吗？",
                    "删除确认",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = user_message.deleteUser(user.getUsername());
                if (success) {
                    JOptionPane.showMessageDialog(panel, "删除成功");
                    reloadTableData(); // 刷新表格
                } else {
                    JOptionPane.showMessageDialog(panel, "删除失败，用户可能不存在");
                }
            }
        }

        // 刷新表格数据
        private void reloadTableData() {
            List<com.zct.bean.USER> dbUsers = user_message.listUsers();
            List<User> userList = userListTablePanel.convertToTableModel(dbUsers);
            model.setUsers(userList); // 使用修改后的 setUsers 方法
        }

        @Override
        public Object getCellEditorValue() {
            return buttons;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.user = model.getUserAtRow(row);
            return panel;
        }
    }
}