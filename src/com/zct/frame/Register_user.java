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
import com.zct.bean.USER;
import com.zct.DAO.user_message;
import com.zct.panel.backgroundpanel;
import com.zct.tool.ImageUtils;

public class Register_user extends JFrame {
    private JPanel jContentPane = null;
    private JTextField nameField;//用户名输入框
    private JPasswordField pwdField;//密码输入框
    private JPasswordField okPwdField;// 确认密码输入框
    private JButton selectAvatarButton;//选择头像按钮
    private JButton registerButton;//注册按钮
    private CircularAvatarLabel avatarPreviewLabel = new CircularAvatarLabel();//自定义圆形头像标签
    private windowUI_title registertitle; // 自定义标题栏
    private int mousex, mousey; // 鼠标点击时的坐标
    private JPanel namePanel = null;
    private JPanel pwdPanel = null;
    private JPanel okPwdPanel = null;
    private backgroundpanel panel = null;
    private JPanel avatarPanel = null;
    private boolean isInitialized = false;
    private File selectedImageFile = null; // 存储选中的图片文件
/**
 * 更新各组件位置
 */
    private void updateregister_positions() {
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

    /**
     * 用户名面板
     * @return
     */
    private JPanel getNamePanel() {
        if (namePanel == null) {
            namePanel = new JPanel();
            namePanel.setLayout(null);
            namePanel.setBackground(Color.white);
            namePanel.setBorder(BorderFactory.createEtchedBorder());
            JLabel nameLabel = new JLabel();
            BufferedImage img = ImageUtils.loadAndResizeImage(Register_user.class, "/image/user.png", 20, 20);
            if (img != null) {
                nameLabel.setIcon(new ImageIcon(img));
            } else {
                System.err.println("图片加载失败，请检查路径或格式");
            }
            nameLabel.setBounds(10, 4, 20, 22);
            namePanel.add(nameLabel);
            nameField = new JTextField();
            nameField.setText("用户名");
            nameField.setFont(new Font("微软雅黑", Font.PLAIN, 15));
            nameField.setForeground(Color.GRAY);
            nameField.setBounds(40, 4, 170, 22);
            nameField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
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
                        pwdField.requestFocusInWindow();
                    }
                }
            });
            namePanel.add(nameField);
        }
        return namePanel;
    }

    /**
     * 密码输入面板
     * @return
     */
    private JPanel getPwdPanel() {
        if (pwdPanel == null) {
            pwdPanel = new JPanel();
            pwdPanel.setLayout(null);
            pwdPanel.setBackground(Color.white);
            pwdPanel.setBorder(BorderFactory.createEtchedBorder());
            JLabel pwdLabel = new JLabel();
            BufferedImage img = ImageUtils.loadAndResizeImage(Register_user.class, "/image/password.png", 20, 20);
            if (img != null) {
                pwdLabel.setIcon(new ImageIcon(img));
            } else {
                System.err.println("图片加载失败");
            }
            pwdPanel.add(pwdLabel);
            pwdLabel.setBounds(10, 4, 20, 22);
            pwdField = new JPasswordField();
            pwdField.setBounds(40, 4, 170, 22);
            pwdField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            pwdField.setText("......");
            pwdField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (new String(pwdField.getPassword()).equals("......")) {
                        pwdField.setText("");
                        pwdField.setForeground(Color.BLACK);
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (new String(pwdField.getPassword()).isEmpty()) {
                        pwdField.setText("......");
                        pwdField.setForeground(Color.GRAY);
                    }
                }
            });
            pwdField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        okPwdField.requestFocusInWindow();
                    }
                }
            });
            pwdPanel.add(pwdField);
        }
        return pwdPanel;
    }

    /**
     * 确认密码面板
     * @return
     */
    private JPanel getOkPwdPanel() {
        if (okPwdPanel == null) {
            okPwdPanel = new JPanel();
            okPwdPanel.setLayout(null);
            okPwdPanel.setBackground(Color.white);
            okPwdPanel.setBorder(BorderFactory.createEtchedBorder());
            JLabel okPwdLabel = new JLabel();
            BufferedImage img = ImageUtils.loadAndResizeImage(Register_user.class, "/image/querenmima.png", 20, 20);
            if (img != null) {
                okPwdLabel.setIcon(new ImageIcon(img));
            } else {
                System.err.println("图片加载失败");
            }
            okPwdLabel.setToolTipText("确认密码");
            okPwdPanel.add(okPwdLabel);
            okPwdLabel.setBounds(10, 4, 20, 22);
            okPwdField = new JPasswordField("......");

            okPwdField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (new String(okPwdField.getPassword()).equals("......")) {
                        okPwdField.setText("");
                        okPwdField.setForeground(Color.BLACK);
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (new String(okPwdField.getPassword()).isEmpty()) {
                        okPwdField.setText("......");
                        okPwdField.setForeground(Color.GRAY);
                    }
                }
            });

            okPwdField.setBounds(40, 4, 150, 22);
            okPwdField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            okPwdField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        registerButton.doClick();
                    }
                }
            });
            okPwdPanel.add(okPwdField);
        }
        return okPwdPanel;
    }

    /**
     * 头像面板
     * @return
     */
    private JPanel avatarPanel() {
        if (avatarPanel == null) {
            avatarPanel = new JPanel();
            avatarPanel.setLayout(null);
            selectAvatarButton = new JButton();
            selectAvatarButton.setBounds(170, 130, 30, 30);

            BufferedImage img = ImageUtils.loadAndResizeImage(Register_user.class, "/image/upload_picture.png", 30, 30);
            if (img != null) {
                selectAvatarButton.setIcon(new ImageIcon(img));
            } else {
                System.err.println("图片加载失败");
            }

            selectAvatarButton.setPreferredSize(new Dimension(30, 30));
            selectAvatarButton.setMargin(new Insets(0, 0, 0, 0));
            selectAvatarButton.setFocusPainted(false);
            selectAvatarButton.setContentAreaFilled(false);
            selectAvatarButton.setBorderPainted(false);
            selectAvatarButton.addMouseListener(new MouseListener_cursor());
            selectAvatarButton.setToolTipText("选择头像");
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
        }
        return jContentPane;
    }

    private JPanel getPanel() {
        if (panel == null) {
            panel = new backgroundpanel(null);
            panel.setLayout(null);
            panel.add(getNamePanel());
            panel.add(getPwdPanel());
            panel.add(getOkPwdPanel());
            panel.add(avatarPanel());
            panel.add(getregisterButton());
            if (registertitle != null && registertitle.titleBar != null) {
                panel.add(registertitle.titleBar);
                registertitle.titleBar.setBounds(0, 0, 400, 30);
            }
        }
        return panel;
    }

    /**
     * 注册按钮
     * @return
     */
    private JButton getregisterButton() {
        registerButton = new RoundedButton();
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
     * 自定义圆形按钮
     */
    public class RoundedButton extends JButton {
        private Color defaultBackgroundColor = new Color(50, 120, 170);
        private Color hoverBackgroundColor = new Color(70, 140, 200);
        private Color backgroundColor = defaultBackgroundColor;

        public RoundedButton() {
            setContentAreaFilled(false);
            setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackgroundColor(hoverBackgroundColor);
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackgroundColor(defaultBackgroundColor);
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            });
        }

        public void setBackgroundColor(Color color) {
            this.backgroundColor = color;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (g instanceof Graphics2D) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

                g2d.setColor(backgroundColor);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40);

                super.paintComponent(g);
            }
        }
    }

    /**
     * 切换鼠标样式
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

    private void Initialized() {
        this.setSize(400, 500);
        this.setLocationRelativeTo(null);
        registertitle = new windowUI_title(this);
        registertitle.settitle("用户注册");
        registertitle.setclose();
        registertitle.setmin();
        registertitle.setbtnlogin_panel();
        ToolTipManager.sharedInstance().setInitialDelay(0);
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



    private void MOuselistener() {
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
                Point location = Register_user.this.getLocation();
                Register_user.this.setLocation(location.x + e.getX() - mousex, location.y + e.getY() - mousey);
            }
        });
    }

    /**
     * 选择头像
     * @param e
     */
    private void selectAvatar(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("图片文件", "jpg", "png", "gif"));
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = fileChooser.getSelectedFile();

            try {
                BufferedImage originalImage = ImageIO.read(selectedImageFile);
                if (originalImage == null) {
                    System.err.println("无法读取图像文件");
                    JOptionPane.showMessageDialog(this, "请选择有效的图像文件");
                    return;
                }

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


    private void handleRegister(ActionEvent e) {
        String name = nameField.getText();
        String pwd = new String(pwdField.getPassword());
        String okPwd = new String(okPwdField.getPassword());

        USER user = new USER();
        user.setName(name);
        user.setPwd(pwd);
        user.setOkPwd(okPwd);

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

        user.setAvatarUrl(avatarPath); // 存入数据库的路径

       user_message.insertUser(user); // 插入数据
    }

    /**
     * 设置默认头像
     * @return
     */
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

    /**
     * 创建图片保存目录
     * @param sourceFile
     * @return
     */
    public String saveImageToDisk(File sourceFile) {
        try {
            File dir = new File("images/avatars/");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String ext = getFileExtension(sourceFile.getName());
            String newFileName = System.currentTimeMillis() + "." + ext;
            File destFile = new File(dir, newFileName);

            Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // 返回完整路径
            return destFile.getAbsolutePath();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,  ex.getMessage());
            return null;
        }
    }


    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf(".");
        return (dotIndex == -1) ? "png" : filename.substring(dotIndex + 1);
    }

    /**
     * 自定义圆形标签
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

    /**
     * 默认使用默认头像
     * @param avatarLabel
     */
    private void setDefaultAvatar(CircularAvatarLabel avatarLabel) {
        BufferedImage img = ImageUtils.loadAndResizeImage(Register_user.class, "/image/user.png", 150, 150);
        if (img != null) {
            avatarLabel.setImage(img);
        } else {
            System.err.println("图片加载失败，请检查路径或格式");
        }
    }

    public static void main(String[] args) {

            Register_user registerUser = new Register_user();
            registerUser.setVisible(true);
    }
    public Register_user() {
        super();
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Initialized();
    }
}
