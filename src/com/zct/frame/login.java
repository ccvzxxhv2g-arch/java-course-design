package com.zct.frame;

import com.zct.DAO.attendant_message;
import com.zct.DAO.user_message;
import com.zct.UI.windowUI_title;
import com.zct.bean.ADMIN;
import com.zct.bean.USER;
import com.zct.panel.backgroundpanel;
import com.zct.tool.ImageUtils;
import com.zct.tool.SaveUserStateTool;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.util.Base64;

public class login extends  javax.swing.JFrame {
    private JPanel jContentPane = null;
    private URL url = null;// 声明图片的URL
    private Image image = null;// 声明图像对象
    private backgroundpanel jPanel = null;// 声明自定义背景面板对象
    private JPanel userPanel = new JPanel();// 用户名面板
    private JPanel passwordPanel = new JPanel();//  密码面板
    private JPanel rolePanel = new JPanel();// 角色面板
    private JTextField userField = new JTextField();
    private JButton RegisterBtn = null;// 注册按钮
    private JButton loginBtn = null;//  登录按钮
    private windowUI_title windowtitle;//自定义标题栏
    private boolean isInitialized = false;// 是否已经初始化
    private JLabel noAccountLabel = null;// 没有账号
    private CircularAvatarLabel avatarLabel = new CircularAvatarLabel();//  圆形头像标签
    private int mousex, mousey;// 鼠标点击时的坐标
    private JPasswordField passwordField = new JPasswordField();//  密码输入框

    /**
     * 主窗口
     * @param args
     */
    public static void main(String[] args) {
        login login = new login();
        Toolkit tookit = login.getToolkit();
        Dimension dm = tookit.getScreenSize();
        login.setLocation((dm.width - login.getWidth()) / 2,
                (dm.height - login.getHeight()) / 2);//使窗口居中显示
        login.setVisible(true);
    }

    /**
     * 创建输入用户名面板（包括头像框以及用户名输入框）
     * @return
     */
    private JPanel setUserPanel() {
        userPanel.setLayout(null);
        userPanel.setBackground(Color.WHITE);
        userPanel.setBorder(BorderFactory.createEtchedBorder());
        //  新增头像 JLabel
        // 使用自定义的圆形头像标签
        avatarLabel.setBounds(6, 4, 22, 22); // 设置大小
        avatarLabel.setBorder(BorderFactory.createEmptyBorder());
        avatarLabel.setBounds(6, 3, 25, 25);
        avatarLabel.setOpaque(false);
        avatarLabel.setBorder(BorderFactory.createEmptyBorder());
        userPanel.add(avatarLabel);
        userPanel.revalidate(); // 刷新布局
        userPanel.repaint();
        // 用户输入框

        userField.setBounds(40, 2, 100, 25);
        userField.setText("用户名");
        userField.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        userField.setForeground(Color.GRAY);
        // 初始化时加载默认头像
        updateAvatar(userField.getText(), avatarLabel);
        // 输入提示逻辑
        userField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (userField.getText().equals("用户名")) {
                    userField.setText("");
                    userField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (userField.getText().isEmpty()) {
                    userField.setText("用户名");
                    userField.setForeground(Color.GRAY);
                }
            }
        });
        userField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // 按下回车后，将焦点移动到下一个组件
                    passwordField.requestFocusInWindow();
                }
            }
        });

        // 添加 DocumentListener 监听输入变化
        userField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateAvatar(userField.getText(), avatarLabel);
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateAvatar(userField.getText(), avatarLabel);
            }


            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateAvatar(userField.getText(), avatarLabel);
            }
        });
        userField.setOpaque(false);
        userField.setBackground(new Color(0, 0, 0, 0));
        userField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        // 添加组件
        userPanel.add(avatarLabel);
        userPanel.add(userField);
        return userPanel;
    }

    /**
     * 绘制圆形头像框
     */
    public static class CircularAvatarLabel extends JLabel {
        private Image image;

        public CircularAvatarLabel() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder());
        }

        public void setImage(Image image) {
            this.image = image;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (image == null) {
                super.paintComponent(g);
                return;
            }

            int width = getWidth();
            int height = getHeight();

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 创建圆形剪裁区域
            Shape clip = new Ellipse2D.Double(0, 0, width, height);
            g2.setClip(clip);
            // 绘制图像（自动缩放）
            g2.drawImage(image, 0, 0, width, height, null);
            g2.dispose();
        }
    }

    /**
     *设置默认头像
     * @param avatarLabel
     */

    private void setDefaultAvatar(login.CircularAvatarLabel avatarLabel) {
        // 使用工具类加载并缩放图像
        BufferedImage img = ImageUtils.loadAndResizeImage(Register_admin.class, "/image/user.png", 150, 150);

        if (img != null) {
            avatarLabel.setImage(img);
        } else {
            System.err.println("图片加载失败，请检查路径或格式");
        }
    }

    /**
     * 实时更新头像
     * @param username
     * @param avatarLabel
     */
    private void updateAvatar(String username, CircularAvatarLabel avatarLabel) {
        if (username == null || username.trim().isEmpty() || username.equals("请输入用户名")) {
            setDefaultAvatar(avatarLabel);
            return;
        }

        try {
            ADMIN admin = attendant_message.getAdminByName(username);
            USER user = user_message.getuserByName(username);
            if (admin != null && admin.getAvatarUrl() != null && !admin.getAvatarUrl().isEmpty()) {
                String avatarPath = admin.getAvatarUrl();
                Image img = null;
                // 判断是否是 Base64 编码
                if (avatarPath.trim().startsWith("data:image")) {
                    // Base64 格式（带前缀）
                    String base64Data = avatarPath.substring(avatarPath.indexOf(",") + 1);
                    byte[] imageBytes = Base64.getDecoder().decode(base64Data);
                    img = ImageIO.read(new ByteArrayInputStream(imageBytes));
                } else if (new File(avatarPath).exists()) {
                    // 本地文件路径
                    img = ImageIO.read(new File(avatarPath));
                }
                else {
                    // URL 或网络图片（可扩展）
                    URL url = new URL(avatarPath);
                    img = ImageIO.read(url);
                }

                if (img != null) {
                    img = img.getScaledInstance(28, 28, Image.SCALE_SMOOTH);
                    avatarLabel.setImage(img);
                } else {
                    System.err.println("图片对象为空");
                    setDefaultAvatar(avatarLabel);
                }
            }
            else if (user != null && user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
                String avatarPath = user.getAvatarUrl();
                Image img = null;
                // 判断是否是 Base64 编码
                if (avatarPath.trim().startsWith("data:image")) {
                    // Base64 格式（带前缀）
                    String base64Data = avatarPath.substring(avatarPath.indexOf(",") + 1);
                    byte[] imageBytes = Base64.getDecoder().decode(base64Data);
                    img = ImageIO.read(new ByteArrayInputStream(imageBytes));
                } else if (new File(avatarPath).exists()) {
                    // 本地文件路径
                    img = ImageIO.read(new File(avatarPath));
                }
                else { 
                    // URL 或网络图片（可扩展）
                    URL url = new URL(avatarPath);
                    img = ImageIO.read(url);
                }

                if (img != null) {
                    img = img.getScaledInstance(28, 28, Image.SCALE_SMOOTH);
                    avatarLabel.setImage(img);
                } else {
                    System.err.println("图片对象为空");
                    setDefaultAvatar(avatarLabel);
                }
            }
            else {
                setDefaultAvatar(avatarLabel);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            setDefaultAvatar(avatarLabel);
        }
    }

    /**
     * 创建密码面板（包括密码图片标签和密码输入框）
     * @return
     */
    private JPanel setPasswordPanel() {
        // 创建用于存放标签和密码框的面板
        passwordPanel.setLayout(null); // 使用绝对布局
        passwordPanel.setBounds(350, 270, 200, 30); // 设置面板位置和宽度高度
        passwordPanel.setBackground(Color.WHITE);
        passwordPanel.setBorder(BorderFactory.createEtchedBorder());

// 密码标签
        JLabel passwordLabel = new JLabel();
        BufferedImage img = ImageUtils.loadAndResizeImage(login.class, "/image/password.png", 20, 20);
        if (img != null) {
            passwordLabel.setIcon(new ImageIcon(img));
        } else {
            System.err.println("图片加载失败，请检查路径或格式");
        }
        passwordLabel.setForeground(Color.black);
        passwordLabel.setBounds(8, 2, 30, 25); // 相对于 passwordPanel 的位置

// 密码输入框

        passwordField.setBounds(40, 3, 120, 25); // 紧随标签之后
        passwordField.setText("********");
        passwordField.setForeground(Color.GRAY);
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(passwordField.getPassword()).equals("********")) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK); // 恢复正常文字颜色
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (new String(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText("********");
                    passwordField.setForeground(Color.GRAY); // 提示文字颜色
                }
            }
        });
        passwordField.setOpaque(false);
        passwordField.setBackground(new Color(0, 0, 0, 0));
        passwordField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        passwordPanel.add(passwordLabel);

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // 按下回车后，将焦点移动到下一个组件
                    loginBtn.doClick();
                }
            }
        });
        passwordPanel.add(passwordField);
        return passwordPanel;

    }

    /**
     * 自定义按钮类，用于绘制圆角按钮
     */
    public class RoundedButton extends JButton {


        private Color defaultBackgroundColor = new Color(50, 120, 170); // 默认背景颜色
        private Color hoverBackgroundColor = new Color(70, 140, 200); // 悬停背景颜色
        private Color backgroundColor = defaultBackgroundColor;

        public RoundedButton() {
            setContentAreaFilled(false); // 不填充内容区域，以便自定义绘制
            setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // 设置边距

            // 添加鼠标事件监听器
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    // 鼠标悬停时，更改背景颜色为高亮颜色
                    setBackgroundColor(hoverBackgroundColor);
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // 鼠标离开时，恢复默认背景颜色
                    setBackgroundColor(defaultBackgroundColor);
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            });
        }

        public void setBackgroundColor(Color color) {
            this.backgroundColor = color;
            repaint(); // 触发重绘
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (g instanceof Graphics2D) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // 启用抗锯齿
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); // 启用文本抗锯齿
                g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON); // 启用分数度量

                // 绘制背景
                g2d.setColor(backgroundColor);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40); // 圆角矩形

                // 绘制文本
                super.paintComponent(g);
            }
        }
    }

    /**
     * 创建登录按钮
     * @return
     */

    private JButton getButton_login() {
        if (loginBtn == null) {
            loginBtn = new RoundedButton(); // 使用自定义按钮
            loginBtn.setText("登 录"); // 设置按钮文本
            loginBtn.setFont(loginBtn.getFont().deriveFont(18f)); // 设置字体大小
            loginBtn.setForeground(Color.WHITE); // 设置文本颜色
            loginBtn.setFocusPainted(false);
            // 添加点击事件监听器
            loginBtn.addActionListener(e -> {

                JComboBox roleComboBox = null;
                // 遍历 panel 找到 JComboBox
                for (Component c : rolePanel.getComponents()) {
                    if (c instanceof JComboBox) {
                        roleComboBox = (JComboBox) c;
                        break;
                    }
                }

                if (roleComboBox == null) {
                    JOptionPane.showMessageDialog(login.this, "角色选择异常！");
                    return;
                }

                String selectedRole = (String) roleComboBox.getSelectedItem();

                if ("管  理  员".equals(selectedRole)) {
                    String username = userField.getText();
                    String password = new String(passwordField.getPassword());
                    // 从数据库查询完整用户信息（包括头像路径）
                   ADMIN admin1=new ADMIN();
                   admin1.setName(username);
                   admin1.setPwd(password);
                    if (attendant_message.adminLogin(admin1)) {
                        ADMIN admin = attendant_message.getAdminByName(username);
                        SaveUserStateTool.setAdmin(admin); // ? 设置完整用户信息（含头像路径）
                        new admin_frame(); // 创建新窗口
                        dispose(); // 关闭当前窗口
                    }
                    else {
                        userField.setText("");
                        passwordField.setText("");
                        userField.requestFocus();
                    }
                }

                else if ("用      户".equals(selectedRole)) {
                    String username = userField.getText();
                    String password = new String(passwordField.getPassword());
                    USER user1=new USER();
                    user1.setName(username);
                    user1.setPwd(password);
                    if(user_message.userLogin(user1))
                    {
                       USER user=user_message.getuserByName(username);
                        SaveUserStateTool.setUser(user);
                        new user_frame();
                        dispose();
                    }

                }
                else {
                    userField.setText("");
                    passwordField.setText("");
                    userField.requestFocus();
                }
            });
        }
        return loginBtn;
    }

    /**
     * 创建注册按钮
     * @return
     */
    private JButton getButton_Register() {
        if (RegisterBtn == null) {
            RegisterBtn = new JButton();
            BufferedImage img=ImageUtils.loadAndResizeImage(login.class, "/image/register.png", 30, 30);
            if(img!=null)
            {
                RegisterBtn.setIcon(new ImageIcon(img));
            }
            else {
                System.err.println("图片加载失败，请检查路径或格式");
            }
            RegisterBtn.setToolTipText("前往注册");
            // 设置按钮尺寸和边距
            RegisterBtn.setPreferredSize(new Dimension(30, 30));
            RegisterBtn.setMargin(new Insets(0, 0, 0, 0)); // 根据需要调整边距
            RegisterBtn.setFocusPainted(false);
            // 透明度设置（如果需要）
            RegisterBtn.setContentAreaFilled(false); // 按钮背景透明
            RegisterBtn.setBorderPainted(false);     // 不绘制边框
            // 添加鼠标事件监听器
            RegisterBtn.addMouseListener(new MouseListener_cursor());
        // 添加点击事件
        RegisterBtn.addActionListener(e -> {
            JComboBox roleComboBox = null;

            // 遍历 panel 找到 JComboBox
            for (Component c : rolePanel.getComponents()) {
                if (c instanceof JComboBox) {
                    roleComboBox = (JComboBox) c;
                    break;
                }
            }

            if (roleComboBox == null) {
                JOptionPane.showMessageDialog(login.this, "角色选择异常！");
                return;
            }

            String selectedRole = (String) roleComboBox.getSelectedItem();

            if ("管  理  员".equals(selectedRole)) {
                // 跳转到管理员注册界面
                new Register_admin(); // 假设你已经定义了 Register_admin 类
            } else if ("用      户".equals(selectedRole)) {
                new Register_user();
            } else {
                JOptionPane.showMessageDialog(login.this, "请选择有效身份");
            }
        });
    }
       return RegisterBtn;

}

    /**
     * 创建位于注册按钮前的提示面板
     * @return
     */
    private JLabel getNoAccountLabel() {
        if (noAccountLabel == null) {
            noAccountLabel = new JLabel("还没有账号？");
            noAccountLabel.setFont(new Font("宋体", Font.PLAIN, 14)); // 设置字体
            noAccountLabel.setForeground(Color.GRAY); // 设置颜色
        }
        return noAccountLabel;
    }


    /**
     * 创建身份选择面板
     * @return
     */
    private JPanel setRoleComboBoxPanel() {
        rolePanel.setLayout(null);
        rolePanel.setBounds(311, 310, 200, 30); // 放在密码面板下方
        rolePanel.setBackground(Color.WHITE);
        String[] roles = {       "--请选择身份--",
                                 "管  理  员",
                                 "用      户"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        roleComboBox.setBounds(0, 0, 200, 30);
        roleComboBox.setSelectedIndex(0); // 默认选中提示项
        rolePanel.add(roleComboBox);
        // 添加鼠标事件监听器
        roleComboBox.addMouseListener(new MouseListener_cursor());
        for (Component component : roleComboBox.getComponents()) {
            if (component instanceof JButton) { // 下拉箭头通常是一个 JButton
                JButton arrowButton = (JButton) component;
                arrowButton.addMouseListener(new MouseListener_cursor());

            }
        }
        return rolePanel;
    }
    /**
     * 创建鼠标事件监听器(当移动到可交互按钮时更改鼠标样式)
     */
    private class MouseListener_cursor extends MouseAdapter {
        @Override
        public void mouseEntered(MouseEvent e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * 创建背景主面板
     * @return
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            url = login.class.getResource("/image/background_1.png"); // 获得图片的URL
            image = new ImageIcon(url).getImage(); // 创建图像对象
            jPanel = new backgroundpanel(image);
            jPanel.setLayout(null);
            jPanel.add(setUserPanel());
            jPanel.add(setPasswordPanel());
            jPanel.add(setRoleComboBoxPanel());
            jPanel.add(getButton_login());
            jPanel.add(getButton_Register());
            jPanel.add(getNoAccountLabel());
            // 标题栏
            if (windowtitle != null && windowtitle.titleBar != null) {
                jPanel.add(windowtitle.titleBar);
                windowtitle.titleBar.setBounds(0, 0, 825, 30);
            }

        }
        return jPanel;
    }

    /**
     *初始化并返回带有登录界面主面板的内容面板（JPanel）
     * @return
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getJPanel(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
     * 定义构造方法
     */
    public login()
    {
    super();
        setUndecorated(true);
        initialize();
    }
    /*
    /*
    * 设置鼠标拖动
     */

    /**
     * 添加鼠标监听器（用于更改窗口位置）
     */
    private void MOuselistener()
    {

        windowtitle.titleBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousex = e.getX();
                mousey = e.getY();
            }
        });

        windowtitle.titleBar.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point location = login.this.getLocation();
                login.this.setLocation(location.x + e.getX() - mousex, location.y + e.getY() - mousey);
            }
        });
    }

    /**
     * 各个组件的位置
     */
    private void updateComponentPositions() {
        if (jPanel == null) return;
        int width = jPanel.getWidth();
        int panelWidth = 200;
        int x = (width - panelWidth) / 2;
        // 更新 userPanel 和 passwordPanel 的位置
        userPanel.setBounds(x, 220, panelWidth, 30);
        passwordPanel.setBounds(x, 265, panelWidth, 30);
        rolePanel.setBounds(x, 310, panelWidth, 30);
        // 更新按钮的位置
        JButton loginBtn = getButton_login();
        JButton zhuceBtn = getButton_Register();
        if (loginBtn != null) {
            loginBtn.setBounds(x, 350, panelWidth, 40);
        }

        if (zhuceBtn != null) {
            zhuceBtn.setBounds(x+145, 400, 90, 30);
        }

        // 获取“还没有账号？”标签
        JLabel noAccountLabel = getNoAccountLabel();
        if (noAccountLabel != null) {
            noAccountLabel.setBounds(x+90 , 405, 90, 20); // 根据需要调整位置
        }

        if (windowtitle != null && windowtitle.titleBar != null) {
            windowtitle.titleBar.setBounds(0, 0, width, 30); // 动态调整宽度
        }
    }

    /**
     * 初始化方法
     */
    private void initialize() {
        this.setSize(825, 520);
        windowtitle=new windowUI_title(this);
        windowtitle.settitle("");//设置标题
        windowtitle.setclose();//设置关闭按钮
        windowtitle.setmin();//设置最小化按钮
        windowtitle.setbtnlogin_panel();//添加按钮面板
        ToolTipManager.sharedInstance().setInitialDelay(0);//设置提示框显示时间
        UIManager.put("ToolTip.font", new Font("宋体", Font.PLAIN, 14));
        UIManager.put("ToolTip.background", Color.white);
        UIManager.put("ToolTip.foreground", Color.BLACK);
        UIManager.put("ToolTip.border", BorderFactory.createLineBorder(Color.GRAY));
        MOuselistener();//设置鼠标拖动
        // 添加窗口大小变化监听器
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (isInitialized) {
                    updateComponentPositions();
                }
            }
        });
        this.validate();
        this.repaint();
        isInitialized = true;
        getJPanel().add(windowtitle.titleBar, BorderLayout.NORTH); //  添加到这里
        // 设置内容面板为带背景图的面板
        this.setContentPane(getJContentPane());
    }
    }