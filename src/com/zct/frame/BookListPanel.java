package com.zct.frame;

import com.zct.DAO.BookDAO;
import com.zct.bean.Book;
import com.zct.listener.NavigationListener;
import com.zct.tool.CommonUIUtils;
import com.zct.tool.ImageUtils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BookListPanel extends JPanel {

    private JTextField searchField;
    private final List<Book> filteredBooks = new ArrayList<>();
    private RoundedPanel searchPanel = null;private static final int ITEMS_PER_PAGE = 6; // 每页显示的图书数量
    private List<Book> allBooks = new ArrayList<>(); // 存储所有图书数据
    private static final int MAX_DISPLAY_PAGES = 5; // 最多显示5个页码按钮
    private int currentPage = 0; // 当前页码
    private JPanel bookListPanel;
    private JPanel paginationPanel;
    private JTextField pageJumpField; // 页码输入框
    private JPanel pageButtonPanel;

    /**
     * 显示书籍详情
     * @param book
     */
    private void showBookDetailsDialog(Book book) {
        // 创建对话框窗口
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(BookListPanel.this), "图书详情", true);
        dialog.setSize(650, 500);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setBackground(Color.WHITE);

        // 主面板：左侧封面 + 右侧信息
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // 左边：封面图片
        JLabel coverLabel = new JLabel();
        String imagePath = book.getCoverImagePath();

        if (imagePath != null && !imagePath.isEmpty()) {
            ImageIcon icon = new ImageIcon(imagePath);
            Image scaled = icon.getImage().getScaledInstance(200, 250, Image.SCALE_SMOOTH);
            coverLabel.setIcon(new ImageIcon(scaled));
        } else {
            coverLabel.setText("无法加载封面");
            coverLabel.setHorizontalAlignment(SwingConstants.CENTER);
            coverLabel.setPreferredSize(new Dimension(200, 250));
        }

        coverLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // 封面下方添加“选择封面”按钮
        JButton selectCoverButton = new JButton("选择新封面");
        selectCoverButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectCoverButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "png", "jpeg"));
            int result = fileChooser.showOpenDialog(dialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                book.setCoverImagePath(selectedFile.getAbsolutePath()); // 更新路径

                // 刷新封面预览
                ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
                Image scaled = icon.getImage().getScaledInstance(200, 250, Image.SCALE_SMOOTH);
                coverLabel.setIcon(new ImageIcon(scaled));
            }
        });

        // 包裹封面和按钮的面板
        JPanel coverPanel = new JPanel();
        coverPanel.setLayout(new BoxLayout(coverPanel, BoxLayout.Y_AXIS));
        coverPanel.setBackground(Color.WHITE);

// 创建一个水平面板用于对齐按钮
        JPanel buttonWrapper = new JPanel();
        buttonWrapper.setLayout(new FlowLayout(FlowLayout.RIGHT)); // 右对齐
        buttonWrapper.setBackground(Color.WHITE);
        buttonWrapper.add(selectCoverButton);

// 添加组件到 coverPanel
        coverPanel.add(coverLabel);
        coverPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        coverPanel.add(buttonWrapper); // 添加带右对齐的按钮容器
        coverPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        // 右边：详细信息（使用 JLabel + JTextField 组合）
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(9, 1, 10, 10));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        // 书名
        JLabel nameLabel = new JLabel("书名：");
        nameLabel.setFont(new Font("宋体", Font.BOLD, 14));
        JTextField nameField = new JTextField(book.getBook_Name(), 20);
        nameField.setFont(new Font("宋体", Font.PLAIN, 14));
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.setBackground(Color.WHITE);
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        infoPanel.add(namePanel);

        // 作者
        JLabel authorLabel = new JLabel("作者：");
        authorLabel.setFont(new Font("宋体", Font.BOLD, 14));
        JTextField authorField = new JTextField(book.getAuthor(), 20);
        authorField.setFont(new Font("宋体", Font.PLAIN, 14));
        JPanel authorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        authorPanel.setBackground(Color.WHITE);
        authorPanel.add(authorLabel);
        authorPanel.add(authorField);
        infoPanel.add(authorPanel);

        // ISBN
        JLabel isbnLabel = new JLabel("ISBN：");
        isbnLabel.setFont(new Font("宋体", Font.BOLD, 14));
        JTextField isbnField = new JTextField(book.getIsbn(), 20);
        isbnField.setFont(new Font("宋体", Font.PLAIN, 14));
        JPanel isbnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        isbnPanel.setBackground(Color.WHITE);
        isbnPanel.add(isbnLabel);
        isbnPanel.add(isbnField);
        infoPanel.add(isbnPanel);

        // 价格
        JLabel priceLabel = new JLabel("价格：");
        priceLabel.setFont(new Font("宋体", Font.BOLD, 14));
        JTextField priceField = new JTextField(String.valueOf(book.getPrice()), 20);
        priceField.setFont(new Font("宋体", Font.PLAIN, 14));
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pricePanel.setBackground(Color.WHITE);
        pricePanel.add(priceLabel);
        pricePanel.add(priceField);
        infoPanel.add(pricePanel);

        // 类别
        JLabel categoryLabel = new JLabel("类别：");
        categoryLabel.setFont(new Font("宋体", Font.BOLD, 14));
        JTextField categoryField = new JTextField(book.getCategory(), 20);
        categoryField.setFont(new Font("宋体", Font.PLAIN, 14));
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoryPanel.setBackground(Color.WHITE);
        categoryPanel.add(categoryLabel);
        categoryPanel.add(categoryField);
        infoPanel.add(categoryPanel);


        // 库存
        JLabel stockLabel = new JLabel("库存：");
        stockLabel.setFont(new Font("宋体", Font.BOLD, 14));
        JTextField stockField = new JTextField(String.valueOf(book.getstock_quantity()), 20);
        stockField.setFont(new Font("宋体", Font.PLAIN, 14));
        JPanel stockPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        stockPanel.setBackground(Color.WHITE);
        stockPanel.add(stockLabel);
        stockPanel.add(stockField);
        infoPanel.add(stockPanel);

        // 简介
        JLabel descriptionLabel = new JLabel("简介：");
        descriptionLabel.setFont(new Font("宋体", Font.BOLD, 14));
        JTextField descriptionField = new JTextField(book.getDescription(), 20);
        descriptionField.setFont(new Font("宋体", Font.PLAIN, 14));
        JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        descriptionPanel.setBackground(Color.WHITE);
        descriptionPanel.add(descriptionLabel);
        descriptionPanel.add(descriptionField);
        infoPanel.add(descriptionPanel);
// 出版社
        JLabel publisherLabel = new   JLabel("出版社：");
        publisherLabel.setFont(new Font("宋体", Font.BOLD, 14));
        JTextField publisherField = new JTextField(book.getPublisher(), 20);
        publisherField.setFont(new Font("宋体", Font.PLAIN, 14));
        JPanel publisherPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        publisherPanel.setBackground(Color.WHITE);
        publisherPanel.add(publisherLabel);
        publisherPanel.add(publisherField);
        infoPanel.add(publisherPanel);

        // 出版日期
        JLabel publishDateLabel = new JLabel("出版日期：");
        publishDateLabel.setFont(new Font("宋体", Font.BOLD, 14));
        JTextField publishDateField = new JTextField(book.getPublishDate().toString(), 20);
        publishDateField.setFont(new Font("宋体", Font.PLAIN, 14));
        CommonUIUtils.SimpleDatePicker datePicker = new CommonUIUtils.SimpleDatePicker(publishDateField);
        datePicker.setMaximumSize(new Dimension(50, publishDateField.getPreferredSize().height + 10));
        datePicker.setPreferredSize(new Dimension(50, publishDateField.getPreferredSize().height + 10));
        datePicker.setBackground(Color.WHITE);
        JPanel publishDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        publishDatePanel.setBackground(Color.WHITE);
        publishDatePanel.add(publishDateLabel);
        publishDatePanel.add(publishDateField);
        publishDatePanel.add(datePicker);
        infoPanel.add(publishDatePanel);

        // 添加到主面板
        mainPanel.add(coverPanel, BorderLayout.WEST);
        mainPanel.add(infoPanel, BorderLayout.CENTER);

        // 修改按钮
        JButton editButton = new JButton("保存修改");
        editButton.addActionListener(e -> {
            try {
                // 获取输入内容
                String newBookName = nameField.getText();
                String newAuthor = authorField.getText();
                String newIsbn = isbnField.getText();
                double newPrice = Double.parseDouble(priceField.getText());
                String newCategory = categoryField.getText();
                String newPublisher = publisherField.getText();
                Date newPublishDate = Date.valueOf(publishDateField.getText());
                int newStock = Integer.parseInt(stockField.getText());
                String newDescription = descriptionField.getText();

                // 更新 Book 对象
                book.setBook_Name(newBookName);
                book.setAuthor(newAuthor);
                book.setIsbn(newIsbn);
                book.setPrice(newPrice);
                book.setCategory(newCategory);
                book.setPublisher(newPublisher);
                book.setPublishDate(newPublishDate); // 注意：如果数据库是 DATE 类型，建议转为 java.sql.Date
                book.setstock_quantity(newStock);
                book.setDescription(newDescription);

                // 调用 DAO 层更新数据库
                BookDAO bookDAO = new BookDAO();
                boolean success = bookDAO.updateBook(book);

                if (success) {
                    JOptionPane.showMessageDialog(dialog, "修改成功！");
                    showCurrentPage();
                } else {
                    JOptionPane.showMessageDialog(dialog, "修改失败，请检查输入数据。", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "价格或库存数量格式不正确。", "输入错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(editButton);

        // 组装整个对话框
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(BookListPanel.this); // 居中显示
        dialog.setVisible(true);
    }


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
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 设置边框颜色
            g2.setColor(Color.BLACK);

            // 绘制圆角矩形边框（宽度为2px）
            Stroke oldStroke = g2.getStroke();
            g2.setStroke(new BasicStroke(2)); // 边框粗细
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
            g2.setStroke(oldStroke);

            g2.dispose();
        }

    }
    private void loadAllBooks() {
        BookDAO bookDAO = new BookDAO();
        allBooks = bookDAO.getAllBooks();
    }
    private void showCurrentPage() {
        bookListPanel.removeAll();

        int start = currentPage * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, filteredBooks.size());

        for (int i = start; i < end; i += 2) {
            Book book1 = filteredBooks.get(i);
            Book book2 = null;

            if (i + 1 < end) {
                book2 = filteredBooks.get(i + 1);
            }

            bookListPanel.add(createBookRowPanel(book1, book2));
            bookListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        bookListPanel.revalidate();
        bookListPanel.repaint();
    }
    /**
     * 刷新页码按钮，支持自动滚动
     */
    private void refreshPageButtons(JPanel panel) {
        panel.removeAll();

        int totalPages = (int) Math.ceil((double) filteredBooks.size() / ITEMS_PER_PAGE);

        if (totalPages <= 0) {
            panel.revalidate();
            panel.repaint();
            return;
        }

        // 自动计算 startPage，确保当前页始终居中或靠近中间
        int halfDisplay = MAX_DISPLAY_PAGES / 2;

        int startPage;
        if (totalPages <= MAX_DISPLAY_PAGES) {
            // 总页数小于等于最大显示数，直接从第一页开始
            startPage = 0;
        } else {
            // 如果当前页靠前，保持从 0 开始
            if (currentPage <= halfDisplay) {
                startPage = 0;
            }
            // 如果当前页靠后，从倒数第五页开始
            else if (currentPage >= totalPages - halfDisplay - 1) {
                startPage = totalPages - MAX_DISPLAY_PAGES;
            }
            // 中间情况，使当前页居中显示
            else {
                startPage = currentPage - halfDisplay;
            }
        }

        // 生成页码按钮
        for (int i = 0; i < MAX_DISPLAY_PAGES && (startPage + i) < totalPages; i++) {
            final int page = startPage + i;
            JButton pageButton = new JButton(String.valueOf(page + 1));
            if (page == currentPage) {
                pageButton.setBackground(Color.LIGHT_GRAY); // 高亮当前页
            }

            pageButton.addActionListener(e -> {
                currentPage = page;
                showCurrentPage(); // 刷新图书列表
                refreshPageButtons(panel); // 刷新页码按钮
            });

            panel.add(pageButton);
        }

        panel.revalidate();
        panel.repaint();
    }






    private RoundedPanel getSearchPanel() {
        searchPanel = new RoundedPanel(30, 30, Color.WHITE);
        searchPanel.setMaximumSize(new Dimension(320, 40));
        searchPanel.setMinimumSize(new Dimension(320, 40));
        searchPanel.setPreferredSize(new Dimension(320, 40));

        this.searchField = new JTextField(20);

        // 添加回车键触发搜索
        this.searchField.addActionListener(e -> performSearch());

        JButton searchButton = new JButton("查询");
        searchButton.addActionListener(e -> performSearch());

        searchPanel.add(this.searchField);
        searchPanel.add(searchButton);
        return searchPanel;
    }



    private void performSearch() {
        String keyword = searchField.getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            // 如果关键词为空，显示所有图书
            filteredBooks.clear();
            filteredBooks.addAll(allBooks);
        } else {
            // 按照关键词过滤图书
            filteredBooks.clear();
            for (Book book : allBooks) {
                if (
                        book.getBook_Name().toLowerCase().contains(keyword) ||
                                book.getIsbn().toLowerCase().contains(keyword) ||
                                book.getAuthor().toLowerCase().contains(keyword) ||
                                (book.getPublisher() != null && book.getPublisher().toLowerCase().contains(keyword))
                ) {
                    filteredBooks.add(book);
                }

            }currentPage = 0;
            showCurrentPage();

            // ? 新增：刷新页码按钮
            Component[] components = paginationPanel.getComponents();
            for (Component comp : components) {
                if (comp instanceof JPanel && ((JPanel) comp).getComponents().length > 0 &&
                        ((JPanel) comp).getComponent(0) instanceof JButton) {
                    refreshPageButtons((JPanel) comp);
                    break;
                }
            }
            // 如果没有匹配结果
            if (filteredBooks.isEmpty()) {
                JOptionPane.showMessageDialog(
                        BookListPanel.this,
                        "未找到匹配的图书，请尝试其他关键词。",
                        "查询结果",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
            else{
                JOptionPane.showMessageDialog(
                        BookListPanel.this,
                        "找到如下与关键字匹配的图书。",
                        "查询结果",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        }

        currentPage = 0; // 回到第一页
        showCurrentPage(); // 刷新显示

    }




    /**
     * 创建一个包含两个图书卡片的面板（一行显示两个）
     */
    private JPanel createBookRowPanel(Book book1, Book book2) {
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        rowPanel.setBackground(Color.WHITE);

        // 添加第一个图书卡片
        rowPanel.add(createSingleBookItemPanel(book1));

        // 添加第二个图书卡片（如果存在）
        if (book2 != null) {
            rowPanel.add(Box.createRigidArea(new Dimension(60, 0)));
            rowPanel.add(createSingleBookItemPanel(book2));
        }

        return rowPanel;
    }

    /**
     * 创建单个图书卡片
     */
    private JPanel createSingleBookItemPanel(Book book) {
        JPanel bookItemPanel = new JPanel(new BorderLayout());
        bookItemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        bookItemPanel.setBackground(Color.WHITE);
        bookItemPanel.setPreferredSize(new Dimension(520, 160)); // 调整宽度为近似一半
        bookItemPanel.setMinimumSize(new Dimension(520, 160));
        bookItemPanel.setMaximumSize(new Dimension(520, 160));

        // 图书封面
        JLabel coverLabel = new JLabel();
        String imagePath = book.getCoverImagePath();

        if (imagePath != null && !imagePath.isEmpty()) {
            ImageIcon icon = new ImageIcon(imagePath);
            Image scaled = icon.getImage().getScaledInstance(80, 100, Image.SCALE_SMOOTH);
            coverLabel.setIcon(new ImageIcon(scaled));
        } else {
            BufferedImage img = ImageUtils.loadAndResizeImage(BookListPanel.class, "/image/cover_image.png", 80, 100);
            coverLabel.setIcon(new ImageIcon(img));
            coverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }

        coverLabel.setPreferredSize(new Dimension(80, 100));

        // 图书信息
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 1, 5, 5));
        infoPanel.setBackground(Color.WHITE);

        // 书名
        JLabel nameLabel = new JLabel("书名：");
        nameLabel.setFont(new Font("宋体", Font.BOLD, 14)); // 加粗标签
        JLabel nameValueLabel = new JLabel(book.getBook_Name());
        nameValueLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        nameValueLabel.setPreferredSize(new Dimension(200, nameValueLabel.getPreferredSize().height+5));
        nameValueLabel.setMaximumSize(new Dimension(200, nameValueLabel.getPreferredSize().height+5));
        nameValueLabel.setMinimumSize(new Dimension(200, nameValueLabel.getPreferredSize().height+5));

        nameValueLabel.setFont(new Font("宋体", Font.PLAIN, 14));

        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.setBackground(Color.WHITE);
        namePanel.add(nameLabel);
        namePanel.add(nameValueLabel);

        infoPanel.add(namePanel);

// 作者
        JLabel authorLabel = new JLabel("作者：");
        authorLabel.setFont(new Font("宋体", Font.BOLD, 14));
        JLabel authorValueLabel = new JLabel(book.getAuthor());
        authorValueLabel.setFont(new Font("宋体", Font.PLAIN, 14));
        authorValueLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        authorValueLabel.setPreferredSize(new Dimension(200, authorValueLabel.getPreferredSize().height+5));
        authorValueLabel.setMaximumSize(new Dimension(200, authorValueLabel.getPreferredSize().height+5));
        authorValueLabel.setMinimumSize(new Dimension(200, authorValueLabel.getPreferredSize().height+5));

        JPanel authorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        authorPanel.setBackground(Color.WHITE);
        authorPanel.add(authorLabel);
        authorPanel.add(authorValueLabel);

        infoPanel.add(authorPanel);

// 库存
        JLabel stockLabel = new JLabel("库存：");
        stockLabel.setFont(new Font("宋体", Font.BOLD, 14));
        JLabel stockValueLabel = new JLabel(String.valueOf(book.getstock_quantity()));
        stockValueLabel.setFont(new Font("宋体", Font.PLAIN, 14));
        stockValueLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        stockValueLabel.setPreferredSize(new Dimension(200, stockValueLabel.getPreferredSize().height+5));
        stockValueLabel.setMaximumSize(new Dimension(200, stockValueLabel.getPreferredSize().height+5));
        stockValueLabel.setMinimumSize(new Dimension(200, stockValueLabel.getPreferredSize().height+5));

        JPanel stockPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        stockPanel.setBackground(Color.WHITE);
        stockPanel.add(stockLabel);
        stockPanel.add(stockValueLabel);

        infoPanel.add(stockPanel);
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0)); // top, left, bottom, right
        buttonPanel.setBackground(Color.WHITE);

        JButton detailButton = new JButton("查看");
        detailButton.setBackground(Color.white);
        detailButton.setFocusPainted(false);
        detailButton.setForeground(Color.BLACK);
        JButton deleteButton = new JButton("删除");
        deleteButton.setBackground(new Color(255, 100, 100));

        detailButton.addActionListener(e -> showBookDetailsDialog(book));

        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(BookListPanel.this,
                    "确认删除《" + book.getBook_Name() + "》吗？",
                    "删除确认",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                BookDAO bookDAO = new BookDAO();
                bookDAO.deleteBook((long) book.getId());
                JOptionPane.showMessageDialog(BookListPanel.this, "删除成功");
                refreshPanel(bookListPanel);
            }
        });

        buttonPanel.add(detailButton);
        buttonPanel.add(deleteButton);

        // 组装
        bookItemPanel.add(coverLabel, BorderLayout.WEST);
        bookItemPanel.add(infoPanel, BorderLayout.CENTER);
        bookItemPanel.add(buttonPanel, BorderLayout.EAST);

        return bookItemPanel;
    }


    /**
     * 刷新指定的面板，重新加载图书列表
     *
     * @param panel 要刷新的面板（通常是 bookListPanel）
     */
    /**
     * 刷新面板：从数据库重新加载图书数据并刷新界面
     */
    /**
     * 刷新面板：从数据库重新加载图书数据并刷新界面，保持当前页不变
     */
    private void refreshPanel(JPanel panel) {
        // 1. 重新加载所有图书
        loadAllBooks(); // 这个方法已经从数据库加载了 allBooks

        // 2. 更新 filteredBooks
        filteredBooks.clear();
        filteredBooks.addAll(allBooks); // 保证与 allBooks 数据一致

        // 3. 清空并刷新面板
        panel.removeAll();

        // 4. 显示当前页
        showCurrentPage(); // 现在使用的是最新的 filteredBooks

        // 5. 重新布局
        panel.revalidate();
        panel.repaint();
    }



    public BookListPanel() {
        // 设置布局和背景
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));



        // 初始化标题面板
        JPanel titleContent = CommonUIUtils.createTitlePanel("图书列表");
        titleContent.setAlignmentX(Component.LEFT_ALIGNMENT);

        Box titleBox = Box.createHorizontalBox();
        titleBox.add(titleContent);
        titleBox.add(Box.createHorizontalGlue());

        add(titleBox);
        add(Box.createRigidArea(new Dimension(0, 10)));

        // 搜索面板
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        wrapper.setBackground(Color.WHITE);
        wrapper.add(getSearchPanel());
        add(wrapper);
        add(Box.createRigidArea(new Dimension(0, 10)));

        // 图书列表面板
        bookListPanel = new JPanel();
        bookListPanel.setLayout(new BoxLayout(bookListPanel, BoxLayout.Y_AXIS));
        bookListPanel.setBackground(Color.WHITE);
        add(bookListPanel);

        // 分页面板
        this.paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.paginationPanel.setBackground(Color.WHITE);

// 页码按钮面板
        JPanel pageButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pageButtonPanel.setBackground(Color.WHITE);

// 加载图书数据
        loadAllBooks(); // 先加载 allBooks
        filteredBooks.addAll(allBooks); // 初始化 filteredBooks

// 刷新页码按钮
// 初始化 pageButtonPanel 成员变量
        this.pageButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        refreshPageButtons(pageButtonPanel); // 初始刷新页码按钮

// 创建分页控件
        JButton prevButton = new JButton("上一页");
        JButton nextButton = new JButton("下一页");
        JButton backButton = new JButton("返回");
        JButton refreshButton = new JButton("刷新");

// 页码跳转组件
        JPanel jumpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        jumpPanel.setBackground(Color.WHITE);

        JLabel jumpLabel = new JLabel("跳转至第");
        pageJumpField = new JTextField(3);
        JButton jumpButton = new JButton("跳转");

        jumpPanel.add(jumpLabel);
        jumpPanel.add(pageJumpField);
        jumpPanel.add(jumpButton);

// 添加组件到分页面板
        this.paginationPanel.add(prevButton);
        this.paginationPanel.add(pageButtonPanel);
        this.paginationPanel.add(nextButton);
        this.paginationPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        this.paginationPanel.add(jumpPanel);
        this.paginationPanel.add(backButton);
        this.paginationPanel.add(refreshButton);

// 添加到主面板
        add(this.paginationPanel);

// 显示第一页
        showCurrentPage();

// 绑定跳转按钮事件
        jumpButton.addActionListener(e -> {
            String input = pageJumpField.getText().trim();
            try {
                int targetPage = Integer.parseInt(input) - 1;
                int totalPages = (int) Math.ceil((double) filteredBooks.size() / ITEMS_PER_PAGE);

                if (targetPage >= 0 && targetPage < totalPages) {
                    currentPage = targetPage;
                    showCurrentPage();
                    refreshPageButtons(pageButtonPanel);
                } else {
                    JOptionPane.showMessageDialog(BookListPanel.this, "请输入有效的页码（1~" + totalPages + "）", "页码错误", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(BookListPanel.this, "请输入合法的数字页码", "格式错误", JOptionPane.ERROR_MESSAGE);
            }
        });


        // 注册刷新事件监听器（移动到这里）
        NavigationListener.addRefreshListener(this::refreshData);

        // 添加分页和返回按钮事件
        prevButton.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                showCurrentPage();
                refreshPageButtons(pageButtonPanel);
            }
        });

        nextButton.addActionListener(e -> {
            if ((currentPage + 1) * ITEMS_PER_PAGE < filteredBooks.size()) {
                currentPage++;
                showCurrentPage();
                refreshPageButtons(pageButtonPanel);
            }
        });

        backButton.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                showCurrentPage();
                return;
            }

            if (!searchField.getText().trim().isEmpty() || filteredBooks.size() != allBooks.size()) {
                resetSearchAndShowAllBooks();
                return;
            }

            Window window = SwingUtilities.getWindowAncestor(BookListPanel.this);
            if (window instanceof JDialog) {
                window.dispose();
            } else {
                if (window != null) {
                    window.dispose();
                }
                SwingUtilities.invokeLater(() -> {
                    admin_frame adminFrame = new admin_frame();
                    adminFrame.setVisible(true);
                });
            }
        });

        refreshButton.addActionListener(e -> {
            currentPage = 0;
            filteredBooks.clear();
            filteredBooks.addAll(allBooks);
            showCurrentPage();
        });
    }
    /**
     * 刷新数据的方法，供 NavigationListener 调用
     */
    public void refreshData() {
        loadAllBooks(); // 重新加载所有图书数据
        filteredBooks.clear();
        filteredBooks.addAll(allBooks); // 恢复显示全部
        currentPage = 0;
        showCurrentPage(); // 刷新当前页
    }
    /**
     * 重置搜索并显示所有图书
     */

    private void resetSearchAndShowAllBooks() {
        searchField.setText(""); // 清空搜索框
        filteredBooks.clear();
        filteredBooks.addAll(allBooks); // 恢复显示全部图书
        currentPage = 0;
        showCurrentPage(); // 刷新当前页显示
    }

}

