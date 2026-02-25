package com.zct.frame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;

import com.zct.UI.windowUI_title;
import com.zct.bean.ADMIN;
import com.zct.DAO.attendant_message;
import com.zct.panel.backgroundpanel;
import com.zct.tool.ImageUtils;

public class Register_admin extends JFrame {
    private JPanel jContentPane = null;
    private JTextField nameField;
    private JPasswordField pwdField;
    private JPasswordField okPwdField;
    private JTextField avatarPathField;
    private JButton selectAvatarButton;
    private JButton registerButton;
    private CircularAvatarLabel avatarPreviewLabel=new CircularAvatarLabel();
    private windowUI_title registertitle;//自定义标题栏
    private int mousex, mousey;// 鼠标点击时的坐标
    private JPanel namePanel = null;
    private JPanel pwdPanel = null;
    private JPanel okPwdPanel = null;
    private backgroundpanel panel = null;
    private JPanel avatarPanel = null;
    private boolean isInitialized=false;
    private File selectedImageFile = null; // 存储选中的图片文件
    private void updateregister_positions()
    {
        if (panel == null) return;
        int width = panel.getWidth();
        int panelWidth = 220;
        int x = (width - panelWidth) / 2;
        namePanel.setBounds(x, 240, panelWidth, 30);
        pwdPanel.setBounds(x, 295, panelWidth, 30);
        okPwdPanel.setBounds(x, 350, panelWidth, 30);
        avatarPanel.setBounds(x, 40, panelWidth, 200);
        registerButton.setBounds(x, 400, panelWidth, 40);
        if (registertitle != null && registertitle.titleBar != null) {
            registertitle.titleBar.setBounds(0, 0, width, 30); // 动态调整宽度
        }

    }
private JPanel getNamePanel()
{

    if(namePanel==null)
    {
        namePanel = new JPanel();
        namePanel.setLayout(null);
        namePanel.setBackground(Color.white);
        namePanel.setBorder(BorderFactory.createEtchedBorder());
        JLabel nameLabel=new JLabel();
        // 使用工具类加载并缩放图像
        BufferedImage img = ImageUtils.loadAndResizeImage(Register_admin.class, "/image/user.png", 20, 20);
        if (img != null) {
            nameLabel.setIcon(new ImageIcon(img));
        } else {
            System.err.println("图片加载失败，请检查路径或格式");
        }
        nameLabel.setBounds(10,4,20,22);
        namePanel.add(nameLabel);
        nameField = new JTextField();
        nameField.setText("用户名");
        nameField.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        nameField.setForeground(Color.GRAY);
        nameField.setBounds(40,4,170,22);
        nameField.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));
        // 输入提示逻辑
        nameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (nameField.getText().equals("用户名")) {
                    nameField.setText("");
                    nameField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (nameField.getText().isEmpty()) {
                    nameField.setText("用户名");
                    nameField.setForeground(Color.GRAY);
                }
            }
        });
        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // 按下回车后，将焦点移动到下一个组件
                     pwdField.requestFocusInWindow();
                }
            }
        });
        namePanel.add(nameField);
    }
   return namePanel;
}
private JPanel getPwdPanel()
{

    if(pwdPanel==null){
        pwdPanel = new JPanel();
        pwdPanel.setLayout(null);
        pwdPanel.setBackground(Color.white);
        pwdPanel.setBorder(BorderFactory.createEtchedBorder());
        JLabel pwdLabel=new JLabel();
        BufferedImage img = ImageUtils.loadAndResizeImage(Register_admin.class, "/image/password.png", 20, 20);
        if(img != null)
        {
            pwdLabel.setIcon(new ImageIcon(img));
        }
        else {
            System.err.println("图片加载失败");
        }
        pwdPanel.add(pwdLabel);
        pwdLabel.setBounds(10,4,20,22);
        pwdField = new JPasswordField();
        pwdField.setBounds(40,4,170,22);
        pwdField.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));
        pwdField.setText("......");
        pwdField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(pwdField.getPassword()).equals("......")) {
                    pwdField.setText("");
                    pwdField.setForeground(Color.BLACK); // 恢复正常文字颜色
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (new String(pwdField.getPassword()).isEmpty()) {
                    pwdField.setText("......");
                    pwdField.setForeground(Color.GRAY); // 提示文字颜色
                }
            }
        });
        pwdField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // 按下回车后，将焦点移动到下一个组件
                    okPwdField.requestFocusInWindow();
                }
            }
        });
        pwdPanel.add(pwdField);
    }
    return pwdPanel;
}
private JPanel getOkPwdPanel()
{

    if(okPwdPanel==null)
    {
        okPwdPanel = new JPanel();
        okPwdPanel.setLayout(null);
        okPwdPanel.setBackground(Color.white);
        okPwdPanel.setBorder(BorderFactory.createEtchedBorder());
        JLabel okPwdLabel=new JLabel();
        BufferedImage img = ImageUtils.loadAndResizeImage(Register_admin.class, "/image/querenmima.png", 20, 20);
        if(img != null)
        {
            okPwdLabel.setIcon(new ImageIcon(img));
        }
        else {
            System.err.println("图片加载失败");
        }
        okPwdLabel.setToolTipText("确认密码");
        okPwdPanel.add(okPwdLabel);
        okPwdLabel.setBounds(10,4,20,22);
        okPwdField = new JPasswordField("......");

        okPwdField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(okPwdField.getPassword()).equals("......")) {
                    okPwdField.setText("");
                    okPwdField.setForeground(Color.BLACK); // 恢复正常文字颜色
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (new String(okPwdField.getPassword()).isEmpty()) {
                    okPwdField.setText("......");
                    okPwdField.setForeground(Color.GRAY); // 提示文字颜色
                }
            }
        });
        okPwdField.setBounds(40,4,150,22);
        okPwdField.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));
        okPwdField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    //按下回车后执行注册按钮的点击事件
                    registerButton.doClick();
                }
            }
        });
        okPwdPanel.add(okPwdField);
    }
    return okPwdPanel;
}
private JPanel avatarPanel() {
if(avatarPanel==null)
{
    avatarPanel = new JPanel();
    avatarPanel.setLayout(null);
    selectAvatarButton = new JButton();
    selectAvatarButton.setBounds(170, 130, 30, 30);

    BufferedImage img=ImageUtils.loadAndResizeImage(Register_admin.class, "/image/upload_picture.png", 30, 30);
    if(img!=null)
    {
        selectAvatarButton.setIcon(new ImageIcon(img));
    }
    else {
        System.err.println("图片加载失败");
    }
    // 设置按钮尺寸和边距
    selectAvatarButton.setPreferredSize(new Dimension(30, 30));
    selectAvatarButton.setMargin(new Insets(0, 0, 0, 0)); // 根据需要调整边距
    selectAvatarButton.setFocusPainted(false);
    // 透明度设置（如果需要）
    selectAvatarButton.setContentAreaFilled(false); // 按钮背景透明
    selectAvatarButton.setBorderPainted(false);     // 不绘制边框
    selectAvatarButton.addMouseListener(new MouseListener_cursor());
    selectAvatarButton.setToolTipText("选择头像");
    avatarPathField = new JTextField();
    avatarPathField.setEditable(false);
    avatarPanel.add(selectAvatarButton);
    avatarPreviewLabel.setBounds(45, 5, 150, 150);
    setDefaultAvatar(avatarPreviewLabel);
    avatarPanel.add(avatarPreviewLabel);
    selectAvatarButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            selectAvatar(e);
        }
    });
}
return avatarPanel;
}
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getPanel(), BorderLayout.CENTER);
            jContentPane.revalidate();
            jContentPane.repaint();
            this.revalidate();
            this.repaint();
        }
        return jContentPane;
    }
private JPanel getPanel()
{
    if (panel == null) {
        panel  = new backgroundpanel(null);
        panel.setLayout(null);
        panel.add(getNamePanel());
        panel.add(getPwdPanel());
        panel.add(getOkPwdPanel());
        panel.add(avatarPanel());
        panel.add(getregisterButton());
        // 标题栏
        if (registertitle != null && registertitle.titleBar != null) {
            panel.add(registertitle.titleBar);
            registertitle.titleBar.setBounds(0, 0, 400, 30);
        }
        panel.revalidate();
        panel.repaint();
        this.revalidate();
        this.repaint();
    }
    return panel;
}

    private JButton getregisterButton()
    {
       registerButton  = new RoundedButton();
       registerButton.setText("注册");

        registerButton.setFont(registerButton.getFont().deriveFont(18f)); // 设置字体大小
        registerButton.setForeground(Color.WHITE); // 设置文本颜色
        registerButton.setFocusPainted(false);
       registerButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleRegister(e);
        }
    });

        return registerButton;
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

private void Initialized()
    {
        this.setSize(400, 500);
        this.setLocationRelativeTo(null);
        registertitle=new windowUI_title(this);
        registertitle.settitle("管理员注册");
        registertitle.setclose();
        registertitle.setmin();
        registertitle.setbtnlogin_panel();
        ToolTipManager.sharedInstance().setInitialDelay(0);//设置提示框显示时间
        UIManager.put("ToolTip.font", new Font("宋体", Font.PLAIN, 14));
        UIManager.put("ToolTip.background", Color.white);
        UIManager.put("ToolTip.foreground", Color.BLACK);
        UIManager.put("ToolTip.border", BorderFactory.createLineBorder(Color.GRAY));
        MOuselistener();
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

                if (isInitialized) {
                    updateregister_positions();
                }
            }
        });
        this.validate();
        this.repaint();
        isInitialized = true;
        getPanel().add(registertitle.titleBar, BorderLayout.NORTH);
        this.setContentPane(getJContentPane());
        setVisible(true);
    }

    public Register_admin() {
        super();
        setUndecorated(true);
        Initialized();
    }

    /**
     * 添加鼠标监听器（用于更改窗口位置）
     */
    private void MOuselistener()
    {

        registertitle.titleBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousex = e.getX();
                mousey = e.getY();
            }
        });

        registertitle.titleBar.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point location = Register_admin.this.getLocation();
                Register_admin.this.setLocation(location.x + e.getX() - mousex, location.y + e.getY() - mousey);
            }
        });
    }
    private void selectAvatar(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("图片文件", "jpg", "png", "gif"));
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = fileChooser.getSelectedFile();
            avatarPathField.setText(selectedImageFile.getAbsolutePath());

            try {
                BufferedImage originalImage = ImageIO.read(selectedImageFile);
                if (originalImage == null) {
                    System.err.println("无法读取图像文件");
                    JOptionPane.showMessageDialog(this, "请选择有效的图像文件");
                    return;
                }

                //  使用工具类进行高质量缩放
                BufferedImage resizedImage = ImageUtils.resizeImage(originalImage, 150, 150);

                avatarPreviewLabel.setImage(resizedImage);
                avatarPreviewLabel.revalidate();
                avatarPreviewLabel.repaint();

            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "读取图像失败：" + ex.getMessage());
            }

        }
    }


    // 注册按钮点击事件
    private void handleRegister(ActionEvent e) {
        String name = nameField.getText();
        String pwd = new String(pwdField.getPassword());
        String okPwd = new String(okPwdField.getPassword());

        ADMIN admin = new ADMIN();
        admin.setName(name);
        admin.setPwd(pwd);
        admin.setOkPwd(okPwd);

        // 设置头像路径
        String avatarPath = null;

        if (selectedImageFile != null && selectedImageFile.exists()) {
            // 用户选择了头像
            avatarPath = saveImageToDisk(selectedImageFile);
        } else {
            // 用户未选择头像，使用默认头像
            avatarPath = saveDefaultAvatarToDisk();
        }

        if (avatarPath == null) {
            JOptionPane.showMessageDialog(this, "设置头像失败，请重试");
            return;
        }

        admin.setAvatarUrl(avatarPath); // 存入数据库的路径

        attendant_message.insertUser(admin); // 插入数据
    }

    // 使用默认头像
    private String saveDefaultAvatarToDisk() {
        try {
            // 加载默认头像资源
            BufferedImage defaultImage = ImageUtils.loadAndResizeImage(Register_admin.class, "/image/user.png", 150, 150);
            if (defaultImage == null) {
                System.err.println("默认头像加载失败");
                return null;
            }

            // 创建缓存图像文件
            File dir = new File("images/avatars");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String newFileName = "default_avatar_" + System.currentTimeMillis() + ".png";
            File destFile = new File(dir, newFileName);

            ImageIO.write(defaultImage, "png", destFile);

            return "images/avatars/" + newFileName;
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "无法设置默认头像：" + ex.getMessage());
            return null;
        }
    }

    // 将图片保存到本地并返回路径
    public String saveImageToDisk(File sourceFile) {
        try {
            // 定义目标文件夹
            File dir = new File("images/avatars");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 使用时间戳命名文件，避免重复
            String ext = getFileExtension(sourceFile.getName());
            String newFileName = System.currentTimeMillis() + "." + ext;
            File destFile = new File(dir, newFileName);

            // 复制文件到目标位置
            Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // 返回相对路径（可自定义）
            return "images/avatars/" + newFileName;
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "保存头像出错：" + ex.getMessage());
            return null;
        }
    }

    // 获取文件扩展名
    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf(".");
        return (dotIndex == -1) ? "png" : filename.substring(dotIndex + 1);
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

            Shape clipShape = new Ellipse2D.Float(0, 0, width, height);
            g2.setClip(clipShape);

            g2.drawImage(image, 0, 0, width, height, null);
            g2.dispose();
        }

    }
    private void setDefaultAvatar(CircularAvatarLabel avatarLabel) {
        // 使用工具类加载并缩放图像
        BufferedImage img = ImageUtils.loadAndResizeImage(Register_admin.class, "/image/user.png", 150, 150);

        if (img != null) {
            avatarLabel.setImage(img);
        } else {
            System.err.println("图片加载失败，请检查路径或格式");
        }
    }

    public static void main(String[] args) {
        Register_admin register_admin=new Register_admin();
        register_admin.setVisible(true);
    }
}

