package com.zct.frame;

import com.zct.UI.windowUI_title;
import com.zct.tool.CommonUIUtils;
import com.zct.tool.ImageUtils;
import com.zct.tool.SaveUserStateTool;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;


public class admin_frame extends JFrame {
    private windowUI_title windowtitle;
    private int mousex;
    private int mousey;
    private JPanel avatar_panel = null;
    private JPanel navpanel = null;
    private JPanel admin_information_panel = null;
    private CircularAvatarLabel avatar_label = new CircularAvatarLabel();
    private JLabel name_label=new JLabel();
    private JPanel mainPanel = new JPanel();
    private List<JButton> allButtons = new ArrayList<>();
    private JLabel coverImageLabel = new JLabel();
    private File selectedImageFile = null; // 存储选中的图片文件
    private String currentPage = "home";
    private String previousPage = "home";
    private JButton bookListButton;     // 图书列表按钮
    private JButton addBookButton;      // 录入图书按钮
    private JButton userListButton;     // 用户列表按钮
    private JButton addUserManageButton; // 用户添加按钮
    private JButton borrowBookButton;   // 借阅图书按钮
    private JButton returnBookButton;   // 归还图书按钮
    private JButton infoPageButton;     // 查看信息按钮
    private JButton editPageButton;     // 修改信息按钮
    private JButton addannouncementButton;//发布公告按钮
    private JButton announcement_listButton;//公告列表
    private JButton homeButton;

    public admin_frame() {
        super();
        setUndecorated(true);
        initialize();
        // 显示窗口
        this.setVisible(true);
    }

    /**
     * 绘制圆形面板
     */
    public static class RoundedPanel extends JPanel {
        private int arcWidth = 15;
        private int arcHeight = 15;
        private Color backgroundColor = new Color(240, 240, 240); // 默认浅灰色

        public RoundedPanel(int arcWidth, int arcHeight, Color bgColor) {
            this.arcWidth = arcWidth;
            this.arcHeight = arcHeight;
            this.backgroundColor = bgColor;
            setOpaque(false); // 允许自定义绘制背景
        }

        public RoundedPanel(Color bgColor) {
            this(15, 15, bgColor);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 填充圆角背景
            if (backgroundColor != null) {
                g2.setColor(backgroundColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);
            }

            // 绘制子组件
            super.paintComponent(g);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            // 如果需要描边可以在这里绘制
        }
    }

    private JPanel admin_information_panel() {
        if (admin_information_panel == null) {
            admin_information_panel = new JPanel();
            admin_information_panel.setLayout(null);
            admin_information_panel.setBounds(120, 35, 100, 100);
            admin_information_panel.setBackground(Color.BLACK);
            name_label.setText(SaveUserStateTool.getAdmin().getName());
            name_label.setForeground(Color.white);
            name_label.setFont(new Font("宋体", Font.BOLD, 16));
            name_label.setBounds(0, 0, 100, 20);
            JLabel role_label = new JLabel("[管理员]");
            role_label.setForeground(Color.white);
            role_label.setFont(new Font("宋体", Font.BOLD, 16));
            role_label.setBounds(0, 70, 100, 20);
            admin_information_panel.add(role_label);
            admin_information_panel.add(name_label);
        }
        return admin_information_panel;
    }

    private JPanel getAvatar_panel() {
        if (avatar_panel == null) {
            avatar_panel = new JPanel();
            avatar_panel.setLayout(null);
            avatar_panel.setBounds(10, 30, 100, 100);
            avatar_panel.setBackground(Color.BLACK);
            avatar_label.setBounds(0, 0, 100, 100);
            BufferedImage img = ImageUtils.loadAndResizeImage(admin_frame.class, SaveUserStateTool.getAdmin().getAvatarUrl(), 100, 100);
            if (img != null) {
                avatar_label.setImage(img);
            } else {
                System.err.println("图片加载失败，请检查路径或格式");
            }
            avatar_panel.add(avatar_label);
        }
        return avatar_panel;
    }

    /**
     * 功能导航栏
     * @return
     */
    private RoundedPanel infopanel() {
        RoundedPanel infoPanel = new RoundedPanel(Color.WHITE);
        infoPanel.setOpaque(false);
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS)); // 垂直排列
        infoPanel.setBounds(1, 150, 190, 500);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        JPanel shouye_panel = new JPanel();
        shouye_panel.add(Box.createRigidArea(new Dimension(10, 0)));
        shouye_panel.setMaximumSize(new Dimension(200, 30));
        shouye_panel.setMinimumSize(new Dimension(200, 30));
        shouye_panel.setPreferredSize(new Dimension(200, 30));
        shouye_panel.setLayout(new BoxLayout(shouye_panel, BoxLayout.X_AXIS));
        shouye_panel.setBackground(Color.WHITE);
        BufferedImage img = ImageUtils.loadAndResizeImage(this.getClass(), "/image/home.png", 30, 30);
        JButton shouye_btn = new JButton("      首页");
        this.homeButton = shouye_btn;
        shouye_btn.setIcon(new ImageIcon(img)); // 设置图标
        shouye_btn.setHorizontalTextPosition(SwingConstants.LEFT); // 图标在文字左边
        shouye_btn.setIconTextGap(90); // 图标与文字间距
        shouye_btn.setBackground(Color.WHITE);
        shouye_btn.setBorder(null);
        shouye_btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        shouye_btn.addActionListener(e -> {
            // 设置当前按钮字体颜色为蓝色
            shouye_btn.setForeground(Color.BLUE);

            // 其他按钮恢复默认颜色
            for (JButton button : allButtons) {
                if (button != shouye_btn) {
                    button.setForeground(Color.BLACK);
                }
            }
        });
        shouye_btn.addActionListener(e -> {
            showhomepage();
        });
// 将按钮加入全局按钮列表中
        allButtons.add(shouye_btn);
        shouye_panel.add(shouye_btn);

        // 第一个可展开面板
        JPanel panel1 = createExpandablePanel("图书管理                        " + "\u25B6", new String[]{"图书列表", "录入图书"});
        JPanel panel2 = createExpandablePanel("用户管理                        " + "\u25B6", new String[]{"用户列表", "用户添加"});
        JPanel panel3 = createExpandablePanel("借阅管理                        " + "\u25B6", new String[]{"借阅图书", "归还图书"});
        JPanel panel4 = createExpandablePanel("个人中心                        " + "\u25B6", new String[]{"查看信息", "修改信息"});
        JPanel panel5 = createExpandablePanel("公告管理                        " + "\u25B6", new String[]{"公告列表", "公告发布"});
        // 后续可以继续添加更多面板..
        shouye_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel1.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel2.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel3.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel4.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel5.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(shouye_panel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        infoPanel.add(panel1);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20))); // 添加间距
        infoPanel.add(panel2);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        infoPanel.add(panel3);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        infoPanel.add(panel4);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        infoPanel.add(panel5);

        return infoPanel;
    }

    // 创建一个可展开的面板
    private JPanel createExpandablePanel(String title, String[] items) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(0, 0, 0, 0));

        JToggleButton toggleButton = new JToggleButton(title);
        toggleButton.setBackground(Color.white);
        toggleButton.setBorder(null);
        toggleButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!toggleButton.isSelected()) {
                    toggleButton.setBackground(new Color(220, 220, 220)); // 悬停灰色
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!toggleButton.isSelected()) {
                    toggleButton.setBackground(Color.white);
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });

        toggleButton.addActionListener(e -> {
            toggleButton.setBackground(toggleButton.isSelected() ? new Color(200, 200, 200) : Color.white);
        });
        toggleButton.setMinimumSize(new Dimension(170, 30));
        toggleButton.setPreferredSize(new Dimension(170, 30));
        toggleButton.setMaximumSize(new Dimension(Short.MAX_VALUE, 30)); // 允许横向扩展

        toggleButton.setFocusPainted(false);

        JPanel expandPanel = new JPanel();
        expandPanel.setLayout(new BoxLayout(expandPanel, BoxLayout.Y_AXIS));
        expandPanel.setBackground(new Color(245, 245, 245));
        expandPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        expandPanel.setBackground(Color.white);
        expandPanel.setVisible(false);

        for (String item : items) {
            JButton button = new JButton(item);
            String buttonText = button.getText().trim();

            // 保存按钮引用
            switch (buttonText) {
                case "图书列表":
                    this.bookListButton = button;
                    break;
                case "录入图书":
                    this.addBookButton = button;
                    break;
                case "用户列表":
                    this.userListButton = button;
                    break;
                case "用户添加":
                    this.addUserManageButton = button;
                    break;
                case "借阅图书":
                    this.borrowBookButton = button;
                    break;
                case "归还图书":
                    this.returnBookButton = button;
                    break;
                case "查看信息":
                    this.infoPageButton = button;
                    break;
                case "修改信息":
                    this.editPageButton = button;
                    break;
                case "公告发布":
                    this.addannouncementButton = button;
                    break;
                    case "公告列表":
                        this.announcement_listButton = button;
                        break;
            }


            button.setAlignmentX(Component.LEFT_ALIGNMENT); // 左对齐
            button.setMinimumSize(new Dimension(170, 30));
            button.setPreferredSize(new Dimension(170, 30));
            button.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));

            button.setFocusPainted(false);
            button.setBorder(null);
            button.setBackground(Color.white);

            // 添加点击事件
            button.addActionListener(e -> {
                // 设置当前按钮字体颜色为蓝色
                button.setForeground(Color.BLUE);

                // 其他按钮恢复默认颜色
                for (JButton button1 : allButtons) {
                    if (button1 != button) {
                        button1.setForeground(Color.BLACK);
                    }
                }

                // 根据按钮文字执行不同操作
                String buttonTextTrimmed = button.getText().trim(); // 去除空格
                switch (buttonTextTrimmed) {
                    case "图书列表":
                        showBookListPage();
                        break;
                    case "录入图书":
                        showAddBookPage();
                        break;
                    case "用户列表":
                        showUserListPage();
                        break;
                    case "用户添加":
                        addUserManagePage();
                        break;
                    case "借阅图书":
                        borrow_book();
                        break;
                    case "归还图书":
                        return_book();
                        break;
                    case "查看信息":
                        showInfoPage();
                        break;
                    case "修改信息":
                        showEditPage();
                        break;
                    case "公告发布":
                         showaddannouncement();
                        break;
                     case "公告列表":
                          showannouncement_list();
                         break;
                    default:
                        System.out.println("未知按钮：" + buttonTextTrimmed);
                        break;
                }
            });
            allButtons.add(button);

            expandPanel.add(button);
            expandPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        toggleButton.addActionListener(e -> {
            boolean visible = expandPanel.isVisible();
            expandPanel.setVisible(!visible);

            String newText = title.substring(0, title.length() - 1) +
                    (!visible ? "\u25BC" : "\u25B6"); // 替换最后一个字符

            toggleButton.setText(newText);
        });


        container.add(toggleButton);
        container.add(expandPanel);

        return container;
    }
    private void showannouncement_list() {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, "announcement_list");
    }
private void showaddannouncement() {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, "addannouncement");
    }
    private void showhomepage() {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, "home");
    }

    private void return_book() {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, "return_book");
    }

    private void borrow_book() {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, "borrow_book");
    }

    private void showEditPage() {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, "edit_page");
    }

    private void addUserManagePage() {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, "add_manage");
    }

    private void showBookListPage() {
        // 获取并刷新 BookListPanel 数据
        Component[] components = mainPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JScrollPane && ((JScrollPane) comp).getViewport().getView() instanceof BookListPanel) {
                BookListPanel bookListPanel = (BookListPanel) ((JScrollPane) comp).getViewport().getView();
                bookListPanel.refreshData(); // 调用刷新方法
                break;
            }
        }
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, "book_list");
    }


    private void showAddBookPage() {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, "add_book");
    }

    private void showUserListPage() {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, "user_list");
    }

    private void showInfoPage() {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, "info_page");
    }

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

            // 创建一个圆形的剪切区域
            Shape clipShape = new Ellipse2D.Float(0, 0, width, height);
            g2.setClip(clipShape);

            // 在圆形区域内绘制图片
            g2.drawImage(image, 0, 0, width, height, null);

            // 绘制白色边框
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.draw(clipShape);

            g2.dispose();
        }


    }

    private JPanel getnavpanel() {
        if (navpanel == null) {
            navpanel = new JPanel();
            navpanel.setLayout(null);
            navpanel.setBackground(Color.BLACK);
            navpanel.setPreferredSize(new Dimension(200, this.getHeight()));
            navpanel.setBounds(0, 0, 200, this.getHeight());

            RoundedPanel infoPanel = infopanel();
            JScrollPane scrollPane = new JScrollPane(infoPanel);

            // 设置 JScrollPane 的位置和大小
            int scrollX = 5;
            int scrollY = 190; // 菜单开始 Y 坐标
            int scrollWidth = 190;
            int scrollHeight = 510;

            scrollPane.setBounds(scrollX, scrollY, scrollWidth, scrollHeight);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

            scrollPane.setBorder(BorderFactory.createEmptyBorder()); // 去掉边框
            scrollPane.setOpaque(false); // JScrollPane 自身透明
            scrollPane.getViewport().setOpaque(false); // 视口透明
            scrollPane.setBackground(Color.BLACK); // 背景透明

            CommonUIUtils.configureVerticalScrollBar(scrollPane);
            // 保留头像和用户信息面板的绝对布局
            JPanel topPanel = new JPanel();
            topPanel.setLayout(null);
            topPanel.setBackground(Color.BLACK);
            topPanel.setBounds(0, 15, 200, 140); // 高度预留给头像和信息

            // 获取并设置头像面板位置
            JPanel avatarPanel = getAvatar_panel();
            avatarPanel.setBounds(10, 30, 100, 100);
            topPanel.add(avatarPanel);

            // 获取并设置管理员信息面板位置
            JPanel adminInfo = admin_information_panel();
            adminInfo.setBounds(120, 35, 100, 100);
            topPanel.add(adminInfo);

            // 添加到主面板
            navpanel.add(topPanel);
            navpanel.add(scrollPane);
        }

        return navpanel;
    }



    private JPanel getmainpanel() {
        // 创建右侧主工作区

        mainPanel.setLayout(new CardLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(new AddBookPanel());
        JScrollPane scrollPane3=new JScrollPane(new BookListPanel());
        CommonUIUtils.configureVerticalScrollBar(scrollPane);
        CommonUIUtils.configureVerticalScrollBar(scrollPane3);

        mainPanel.add(new home_panel(), "home");//首页
        mainPanel.add(scrollPane, "add_book");// 添加图书
        mainPanel.add(scrollPane3, "book_list");         // 图书列表
        mainPanel.add(new UserListPanel(), "user_list");      // 用户列表
        mainPanel.add(new Editadminpanel(), "edit_page");         // 编辑页面
        mainPanel.add(new AddUserManagePanel(), "add_manage");       // 用户管理
        mainPanel.add(new BorrowBookPanel(), "borrow_book");     // 借阅图书
        String name = SaveUserStateTool.getAdmin().getName();
        mainPanel.add(new ReturnBookPanel(name), "return_book"); // 归还图书
        mainPanel.add(new adminInfoPanel(), "info_page");         // 查看信息
        mainPanel.add(new addannouncement(), "addannouncement");//添加公告
        mainPanel.add(new announcement_list(), "announcement_list");// 公告列表


        return mainPanel;
    }


    private JPanel getpanel() {
        JPanel admin_panel = new JPanel(new BorderLayout());
        // 使用 JSplitPane 分割左右区域
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, getnavpanel(), getmainpanel());
        splitPane.setDividerLocation(200); // 初始分割线位置
        splitPane.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0)));
        splitPane.setDividerSize(3); // 更细的分割线
        splitPane.setForeground(Color.BLACK); // 设置颜色与背景一致
        splitPane.setResizeWeight(0.0); // 固定左侧宽度
        // 将 splitPane 加入主面板
        admin_panel.add(splitPane, BorderLayout.CENTER);
        admin_panel.add(windowtitle.titleBar, BorderLayout.NORTH);

        return admin_panel;

    }

    public void initialize() {
        // 设置窗口大小
        setSize(1366, 768);
        // 居中显示
        setLocationRelativeTo(null);
        windowtitle = new windowUI_title(this);
        windowtitle.settitle("");//设置标题
        windowtitle.setclose();//设置关闭按钮
        windowtitle.setbig();
        windowtitle.setmin();//设置最小化按钮
        windowtitle.setattendant_panel();
        MOuselistener();
        setContentPane(getpanel());
        SaveUserStateTool.addAdminChangeListener(newAdmin -> {
            SwingUtilities.invokeLater(() -> {
                // 更新界面显示
                name_label.setText(newAdmin.getName());
                BufferedImage img = ImageUtils.loadAndResizeImage(
                        getClass(),
                        newAdmin.getAvatarUrl(),
                        avatar_label.getWidth(),
                        avatar_label.getHeight()
                );
                avatar_label.setImage(img);
            });
        });

    }

    /**
     * 添加鼠标监听器（用于更改窗口位置）
     */
    private void MOuselistener() {

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
                Point location = admin_frame.this.getLocation();
                admin_frame.this.setLocation(location.x + e.getX() - mousex, location.y + e.getY() - mousey);
            }
        });
    }

    public static void main(String[] args) {

        new admin_frame();
    }
}