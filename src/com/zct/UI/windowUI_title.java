package com.zct.UI;

import com.zct.bean.ADMIN;
import com.zct.tool.SaveUserStateTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class windowUI_title implements windowUI {

    protected JFrame frame;
    public JPanel titleBar;
    public JButton closeButton;
    public JButton minButton;
    public JButton bigButton;
    public windowUI_title(JFrame frame) {
        this.frame = frame;
        this.titleBar = new JPanel(new BorderLayout());
        this.closeButton = new JButton("X");
        this.minButton = new JButton("—");
        this.bigButton = new JButton("□");
    }
    /*
     * 设置标题栏
     */
    public void settitle(String title)
    {
        frame.setUndecorated(true);
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleBar.add(titleLabel, BorderLayout.CENTER);
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.add(titleLabel);
        centerPanel.setOpaque(false); // 保持透明不影响整体风格
        titleBar.add(centerPanel, BorderLayout.WEST);
        titleLabel.setOpaque(true);
        frame.add(titleBar, BorderLayout.NORTH);
    }
    /*
     * 设置关闭按钮
     */
    public void setclose()
    {
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.setFocusPainted(false);closeButton.addActionListener(e -> {
        frame.dispose(); // 只关闭当前窗口，不退出程序
    });

        closeButton.setContentAreaFilled(false); // 禁用默认背景填充
        closeButton.setBorderPainted(false);     // 隐藏边框
    }
    public void setmin()
    {

        minButton.setBorder(BorderFactory.createEmptyBorder());
        minButton.setFocusPainted(false);
        minButton.setContentAreaFilled(false);
        minButton.setBorderPainted(false);
        minButton.addActionListener(e -> {
            frame.setExtendedState(JFrame.ICONIFIED);
        });
    }

    public void setbig()
    {

        bigButton.setFocusPainted(false);
        bigButton.addActionListener(e -> {
        if ((frame.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
            frame.setExtendedState(JFrame.NORMAL); // 恢复窗口大小
            bigButton.setText("□"); // 可选：改图标为还原
        } else {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // 最大化
            bigButton.setText("?"); // 可选：表示“还原”
        }
    });
        bigButton.setContentAreaFilled(false); // 禁用默认背景填充
        bigButton.setBorderPainted(false);     // 隐藏边框
        bigButton.addMouseListener(new MouseAdapter() {
            @Override
    /*
    设置鼠标监视器，产生悬停效果
    */
            public void mouseEntered(MouseEvent e) {
                bigButton.setBackground(new Color(70, 130, 180)); // 钢蓝色，更柔和的高亮
                bigButton.setForeground(Color.WHITE); // 白色字体提高对比度
            }

            @Override
            public void mouseExited(MouseEvent e) {
                bigButton.setBackground(new Color(0, 0, 0)); // 恢复黑色背景
                bigButton.setForeground(Color.GRAY); // 白色字体
            }

            @Override
            public void mousePressed(MouseEvent e) {
                bigButton.setBackground(new Color(50, 50, 50)); // 深灰色表示按下状态
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                bigButton.setBackground(new Color(70, 130, 180)); // 释放后回到高亮色
            }
        });


    }

    /*
    添加顶部的按钮面板。后续可以添加更多的按钮
    */
    public void setattendant_panel()
    {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS)); // 水平排列排列
        buttonPanel.setAlignmentX(Component.RIGHT_ALIGNMENT); // 整体靠右
        closeButton.setPreferredSize(new Dimension(30, 30));
        closeButton.setHorizontalAlignment(SwingConstants.CENTER);
        closeButton.setBackground(new Color(0,0,0));
        closeButton.setForeground(Color.GRAY);
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
    /*
    设置鼠标监视器，产生悬停效果
    */
            public void mouseEntered(MouseEvent e) {
                closeButton.setBackground(new Color(70, 130, 180)); // 钢蓝色，更柔和的高亮
                closeButton.setForeground(Color.WHITE); // 白色字体提高对比度
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeButton.setBackground(new Color(0, 0, 0)); // 恢复黑色背景
                closeButton.setForeground(Color.GRAY); // 白色字体
            }

            @Override
            public void mousePressed(MouseEvent e) {
                closeButton.setBackground(new Color(50, 50, 50)); // 深灰色表示按下状态
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                closeButton.setBackground(new Color(70, 130, 180)); // 释放后回到高亮色
            }
        });

        bigButton.setPreferredSize(new Dimension(30, 30));
        bigButton.setHorizontalAlignment(SwingConstants.CENTER);
        bigButton.setBackground(new Color(0,0,0));
        bigButton.setForeground(Color.GRAY);

        minButton.setPreferredSize(new Dimension(30, 30));
        minButton.setHorizontalAlignment(SwingConstants.CENTER);
        minButton.setBackground(new Color(0,0,0));
        minButton.setForeground(Color.GRAY);
        minButton.addMouseListener(new MouseAdapter() {
        @Override
    /*
    设置鼠标监视器，产生悬停效果
    */
        public void mouseEntered(MouseEvent e) {
            minButton.setBackground(new Color(70, 130, 180)); // 钢蓝色，更柔和的高亮
            minButton.setForeground(Color.WHITE); // 白色字体提高对比度
        }

        @Override
        public void mouseExited(MouseEvent e) {
            minButton.setBackground(new Color(0, 0, 0)); // 恢复黑色背景
            minButton.setForeground(Color.GRAY); // 白色字体
        }

        @Override
        public void mousePressed(MouseEvent e) {
            minButton.setBackground(new Color(50, 50, 50)); // 深灰色表示按下状态
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            minButton.setBackground(new Color(70, 130, 180)); // 释放后回到高亮色
        }
    });
// 添加按钮并设置它们之间的间距
        buttonPanel.add(minButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(3, 5))); // 垂直间距
        buttonPanel.add(bigButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(3, 5)));
        buttonPanel.add(closeButton);
        buttonPanel.setOpaque(false);
        JPanel welcome = new JPanel(new BorderLayout());
        welcome.setBackground(new Color(0, 0, 0,0));
        //JLabel label = new JLabel("欢迎您，"+SaveUserStateTool.getAdmin().getName()+" [管理员]");
        JLabel label = new JLabel(" 图书管理系统",SwingConstants.LEFT);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("宋体", Font.BOLD, 25)); // 设置字体、风格和大小
        welcome.add(label);
        //titleBar.setBackground(new Color(0, 144, 255));
        titleBar.setBackground(new Color(0, 0, 0));
        titleBar.setPreferredSize(new Dimension(1366, 60));
        titleBar.add(welcome,BorderLayout.WEST);
        titleBar.add(buttonPanel, BorderLayout.EAST);
    }

    public void setbtnlogin_panel()
    {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setBackground(new Color(0, 0, 0));
        buttonPanel.setOpaque(false);
        closeButton.setPreferredSize(new Dimension(30, 30));
        closeButton.setHorizontalAlignment(SwingConstants.CENTER);
        closeButton.setBackground(new Color(0,0,0));
        closeButton.setForeground(Color.BLACK);
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            /*
            设置鼠标监视器，产生悬停效果
            */
            public void mouseEntered(MouseEvent e) {
                closeButton.setBackground(Color.RED);
                closeButton.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeButton.setBackground(new Color(230, 230, 230));
                closeButton.setForeground(Color.BLACK);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                closeButton.setBackground(Color.DARK_GRAY);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                closeButton.setBackground(Color.RED);
            }
        });

        minButton.setPreferredSize(new Dimension(30, 30));
        minButton.setHorizontalAlignment(SwingConstants.CENTER);
        minButton.setBackground(new Color(0,0,0));
        minButton.setForeground(Color.BLACK);

        minButton.addMouseListener(new MouseAdapter() {
            @Override
            /*
            设置鼠标监视器，产生悬停效果
            */
            public void mouseEntered(MouseEvent e) {
                minButton.setBackground(Color.RED);
                minButton.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                minButton.setBackground(new Color(0, 0, 0));
                minButton.setForeground(Color.BLACK);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                minButton.setBackground(Color.DARK_GRAY);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                minButton.setBackground(Color.RED);
            }
        });
        buttonPanel.add(minButton);     // 后添加，靠右
        buttonPanel.add(closeButton); //   先添加，靠左
        titleBar.setBackground(new Color(0, 0, 0));
        titleBar.setOpaque(false);
        titleBar.add(buttonPanel, BorderLayout.EAST);
    }
}
