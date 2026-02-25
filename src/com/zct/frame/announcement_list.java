package com.zct.frame;

import com.zct.DAO.announcement_dao;
import com.zct.bean.Announcement;
import com.zct.tool.CommonUIUtils;
import com.zct.tool.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class announcement_list extends JPanel {
    private JTextField searchField;
    private BookListPanel.RoundedPanel searchPanel = null;
    private announcement_dao  dao = new announcement_dao();
    public announcement_list()
    {
        refreshList();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setMinimumSize(new Dimension(600, 800));
        setPreferredSize(new Dimension(600, 800));
        setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));

        // 标题栏
        JPanel titleContent = CommonUIUtils.createTitlePanel("公告列表");
        titleContent.setAlignmentX(Component.LEFT_ALIGNMENT);

        Box titleBox = Box.createHorizontalBox();
        titleBox.add(titleContent);
        titleBox.add(Box.createHorizontalGlue()); // 让右边空白撑开，实现左对齐效果

        add(titleBox);
        add(Box.createRigidArea(new Dimension(0, 10)));
        // 搜索面板
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        wrapper.setBackground(Color.WHITE);
        wrapper.add(getSearchPanel());
        add(wrapper);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(createNoticeList());
    }
    private JScrollPane createNoticeList() {
        // 模拟数据（后续应从数据库加载）
        List<Announcement> notices = getSampleNotices();

        // 创建表格面板
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);

        for (Announcement notice : notices) {
            listPanel.add(createNoticeItem(notice));
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        // 返回带滚动条的面板
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        return scrollPane;
    }

    /**
     * 创建单个公告项
     */
    private JPanel createNoticeItem(Announcement notice) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BorderLayout(10, 10));
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // 左侧信息区
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 1, 5, 5));
        infoPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("标题：" + notice.getTitle() );
        JLabel contentLabel = new JLabel("内容： " + notice.getContent().substring(0, Math.min(50, notice.getContent().length()))+"..." );
        JLabel metaLabel = new JLabel("发布时间：" + notice.getPublishTime() + " | 发布者：" + notice.getPublisherName());

        infoPanel.add(titleLabel);
        infoPanel.add(contentLabel);
        infoPanel.add(metaLabel);

        // 右侧按钮
        JPanel ButtonPanel = new JPanel();
        ButtonPanel.setLayout(new BoxLayout(ButtonPanel, BoxLayout.Y_AXIS));
        ButtonPanel.setBackground(Color.WHITE);
        ButtonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 20));

        JButton editButton = new JButton("编辑");
        editButton.setBackground(Color.WHITE);
        editButton.setForeground(Color.BLACK);
        editButton.setFocusPainted(false);
        editButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDetailDialog(notice);
            }
        });
        JButton deleteButton = new JButton("删除");
        deleteButton.setBackground(new Color(255, 100, 100));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    announcement_list.this,
                    "确认删除？",
                    "删除确认",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = dao.deleteAnnouncementById(notice.getId());
                if (success) {
                    // 刷新列表
                    refreshList();
                } else {
                    JOptionPane.showMessageDialog(announcement_list.this, "删除失败", "", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        ButtonPanel.add(editButton);
        ButtonPanel.add(Box.createRigidArea(new Dimension(0, 13)));
        ButtonPanel.add(deleteButton);

        // 添加到主面板
        itemPanel.add(infoPanel, BorderLayout.CENTER);
        itemPanel.add(ButtonPanel, BorderLayout.EAST);

        return itemPanel;
    }
    private void refreshList() {
        Component[] components = this.getComponents();
        for (Component comp : components) {
            if (comp instanceof JScrollPane) {
                // 获取 JScrollPane 中的 JPanel
                JPanel listPanel = (JPanel) ((JScrollPane) comp).getViewport().getView();
                listPanel.removeAll(); // 调用 Container 的 removeAll()
            }
        }

        // 重新生成列表内容
        List<Announcement> notices = getSampleNotices();
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);

        for (Announcement notice : notices) {
            listPanel.add(createNoticeItem(notice));
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        // 替换旧内容
        for (Component comp : components) {
            if (comp instanceof JScrollPane) {
                ((JScrollPane) comp).setViewportView(listPanel);
            }
        }

        revalidate();
        repaint();
    }

    /**
     * 显示公告详情弹窗
     */
    private void showDetailDialog(Announcement notice) {
    // 创建标题输入框并初始化为当前公告标题
    JTextField titleField = new JTextField(notice.getTitle());

    // 创建内容文本域
    JTextArea textArea = new JTextArea(notice.getContent());
    textArea.setWrapStyleWord(true);
    textArea.setLineWrap(true);
    textArea.setEditable(true);
    textArea.setBackground(Color.WHITE);

    JScrollPane contentScrollPane = new JScrollPane(textArea);

    // 使用 JPanel 组织标题和内容
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    JLabel  titleLabel = new JLabel("标题");
    panel.add(titleLabel);
    panel.add(titleField);
    panel.add(Box.createRigidArea(new Dimension(0, 10)));
    panel.add(new JLabel("内容"));
    contentScrollPane.setPreferredSize(new Dimension(400, 200));
    panel.add(contentScrollPane);

    // 自定义对话框按钮
    Object[] options = {"保存", "取消"};
    int result = JOptionPane.showOptionDialog(this,
            panel,
            "编辑公告",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]);

    if (result == 0) {
        String newTitle = titleField.getText().trim();
        String newContent = textArea.getText();

        if (newTitle.isEmpty()) {
            JOptionPane.showMessageDialog(this, "标题不能为空", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        notice.setTitle(newTitle);
        notice.setContent(newContent);

        // 调用 DAO 更新数据库中的公告
        boolean success = dao.updateAnnouncement(notice);

        if (success) {
            JOptionPane.showMessageDialog(this, "公告已更新");
            refreshList(); // 刷新列表以反映更改
        } else {
            JOptionPane.showMessageDialog(this, "更新失败，请重试", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}


    /**
     * 从数据库获取公告列表
     */
    private List<Announcement> getSampleNotices() {
        List<Announcement> list = new ArrayList<>();

        try {
            announcement_dao dao = new announcement_dao();
            list = dao.getAllAnnouncements(); // 调用 DAO 查询所有公告
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "加载公告失败，请检查数据库连接", "错误", JOptionPane.ERROR_MESSAGE);
        }

        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "暂无公告信息", "提示", JOptionPane.INFORMATION_MESSAGE);
        }

        return list;
    }
    private BookListPanel.RoundedPanel getSearchPanel() {

        searchPanel = new BookListPanel.RoundedPanel(30, 30, Color.WHITE);
        searchPanel.setMaximumSize(new Dimension(320, 33));
        searchPanel.setMinimumSize(new Dimension(320, 33));
        searchPanel.setPreferredSize(new Dimension(320, 33));

        this.searchField = new JTextField(20);

        JButton searchButton = new JButton();
        BufferedImage img = ImageUtils.loadAndResizeImage(BookListPanel.class, "/image/search_.png", 20, 20);
        if (img != null)
        {
            searchButton.setIcon(new ImageIcon(img));
        }
        else
        {
            System.err.println("无法加载图片");
        }
        searchButton.setBorder(null);
        searchButton.setBackground(Color.white);
        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(announcement_list.this, "请输入搜索关键词", "提示", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            // 调用 DAO 模糊搜索方法
            List<Announcement> results = dao.searchAnnouncementsByTitle(keyword);
            if (results != null && !results.isEmpty()) {
                refreshNoticeList(results);
            } else {
                JOptionPane.showMessageDialog(announcement_list.this,
                        "未找到匹配的公告",
                        "搜索结果",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        JButton resetButton = new JButton();

        BufferedImage img2 = ImageUtils.loadAndResizeImage(BookListPanel.class, "/image/sx.png", 20, 20);
        if (img != null)
        {
            resetButton.setIcon(new ImageIcon(img2));
        }
        else
        {
            System.err.println("无法加载图片");
        }
        resetButton.setBackground(Color.WHITE);
        resetButton.setBorder(null);

        resetButton.addActionListener(e -> {
            // 重置搜索框内容
            searchField.setText("");
            // 刷新为全部公告
            refreshList();
        });

        searchPanel.add(this.searchField);
        searchPanel.add(searchButton);
        searchPanel.add(resetButton);
        return searchPanel;
    }
    private void refreshNoticeList(List<Announcement> notices) {
        Component[] components = this.getComponents();
        for (Component comp : components) {
            if (comp instanceof JScrollPane) {
                JPanel listPanel = (JPanel) ((JScrollPane) comp).getViewport().getView();
                listPanel.removeAll();

                for (Announcement notice : notices) {
                    listPanel.add(createNoticeItem(notice));
                    listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }

                revalidate();
                repaint();
            }
        }
    }

}
