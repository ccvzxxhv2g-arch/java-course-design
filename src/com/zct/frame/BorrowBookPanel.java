package com.zct.frame;

import com.zct.DAO.BookDAO;
import com.zct.DAO.BorrowDAO;
import com.zct.bean.Book;
import com.zct.tool.CommonUIUtils;
import com.zct.tool.ImageUtils;
import com.zct.tool.SaveUserStateTool;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BorrowBookPanel extends JPanel {

    private JTextField searchField;
    private RoundedPanel searchPanel = null;
    private static final int ITEMS_PER_PAGE = 6; // 每页显示数量
    private static final int MAX_DISPLAY_PAGES = 5; // 最多显示页码数
    private List<Book> allBooks = new ArrayList<>();
    private List<Book> filteredBooks = new ArrayList<>();
    private int currentPage = 0;
    private JPanel bookListPanel;
    private JPanel paginationPanel;
    private JPanel pageButtonPanel;
    private JTextField pageJumpField;

    /**
     * 借书页面详情
     * @param book
     */
    private void showBookDetailsDialog(Book book) {
        // 创建对话框窗口
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(BorrowBookPanel.this), "图书详情", true);
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
            BufferedImage img = ImageUtils.loadAndResizeImage(login.class, "/image/cover_image.png", 200, 200);
            coverLabel.setIcon(new ImageIcon(img));
            coverLabel.setHorizontalAlignment(SwingConstants.CENTER);
            coverLabel.setPreferredSize(new Dimension(200, 250));
        }

        coverLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));


        JPanel coverPanel = new JPanel();
        coverPanel.setLayout(new BoxLayout(coverPanel, BoxLayout.Y_AXIS));
        coverPanel.setBackground(Color.WHITE);


        // 添加组件到 coverPanel

        coverPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        coverPanel.add(coverLabel);
        // 右边：详细信息（使用 JLabel + JTextField 组合）
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(9, 1, 10, 10));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        // 书名
        JLabel nameLabel = new JLabel("书名：");
        nameLabel.setFont(new Font("宋体", Font.BOLD, 14));
        JTextField nameField = new JTextField(book.getBook_Name(), 20);
        nameField.setEditable(false);
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
        authorField.setEditable(false);
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
        isbnField.setEditable(false);
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
        priceField.setEditable(false);
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
        categoryField.setEditable(false);
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
        stockField.setEditable(false);
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
        descriptionField.setEditable(false);
        descriptionField.setFont(new Font("宋体", Font.PLAIN, 14));
        JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        descriptionPanel.setBackground(Color.WHITE);
        descriptionPanel.add(descriptionLabel);
        descriptionPanel.add(descriptionField);
        infoPanel.add(descriptionPanel);

        // 添加到主面板
        mainPanel.add(coverPanel, BorderLayout.WEST);
        mainPanel.add(infoPanel, BorderLayout.CENTER);

        //借阅按钮
        JButton editButton = new JButton("借阅");
        editButton.addActionListener(e -> {
            // ? 关闭父窗口
            dialog.dispose();

            // ? 创建子窗口
            JDialog dateDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(BorrowBookPanel.this), "选择借阅和归还时间", true);
            dateDialog.setLayout(new BorderLayout(10, 10));
            dateDialog.setSize(400, 250);
            dateDialog.setLocationRelativeTo(BorrowBookPanel.this);

            // 借阅时间输入框
            JLabel borrowLabel = new JLabel("借阅时间:");
            JTextField borrowField = new JTextField(20);
            JButton borrowBtn = new JButton("...");
            JPanel borrowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            borrowPanel.add(borrowLabel);
            borrowPanel.add(borrowField);
            borrowPanel.add(borrowBtn);

            // 归还时间输入框
            JLabel returnLabel = new JLabel("归还时间:");
            JTextField returnField = new JTextField(20);
            JButton returnBtn = new JButton("...");
            JPanel returnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            returnPanel.add(returnLabel);
            returnPanel.add(returnField);
            returnPanel.add(returnBtn);

            // 按钮面板
            JButton confirmBtn = new JButton("确认借阅");
            JButton cancelBtn = new JButton("取消");

            JPanel dialogButtonPanel = new JPanel();
            dialogButtonPanel.setLayout(new BoxLayout(dialogButtonPanel, BoxLayout.X_AXIS));
            dialogButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            dialogButtonPanel.add(Box.createHorizontalGlue()); // 左侧填充
            dialogButtonPanel.add(confirmBtn);
            dialogButtonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            dialogButtonPanel.add(cancelBtn);

            // 主内容面板
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            contentPanel.add(borrowPanel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            contentPanel.add(returnPanel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            // 初始化默认日期
            Calendar cal = Calendar.getInstance();
            String defaultBorrowDate = String.format("%d-%02d-%02d",
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH)
            );
            cal.add(Calendar.DAY_OF_MONTH, 30); // 默认+30天
            String defaultReturnDate = String.format("%d-%02d-%02d",
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH)
            );

            borrowField.setText(defaultBorrowDate);
            returnField.setText(defaultReturnDate);

            // 添加日期选择器
            new SimpleDatePicker(borrowField);
            new SimpleDatePicker(returnField);

            borrowBtn.addActionListener(evt -> {
                SimpleDatePicker borrowDatePicker = new SimpleDatePicker(borrowField);
                borrowDatePicker.showCalendarPopup();
                borrowField.setText(borrowDatePicker.getSelectedDate());
            });

            returnBtn.addActionListener(evt -> {
                SimpleDatePicker returnDatePicker = new SimpleDatePicker(returnField);
                returnDatePicker.showCalendarPopup();
                returnField.setText(returnDatePicker.getSelectedDate());
            });

            // 确认按钮逻辑
            confirmBtn.addActionListener(evt -> {
                String borrowDate = borrowField.getText();
                String returnDate = returnField.getText();

                // 校验日期格式
                try {
                    java.time.LocalDate.parse(borrowDate);
                    java.time.LocalDate.parse(returnDate);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(BorrowBookPanel.this, "日期格式不正确，请使用 YYYY-MM-DD 格式。", "日期错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }


                BorrowDAO borrowDAO = new BorrowDAO();

                if (book.getstock_quantity() > 0) {
                    boolean success = borrowDAO.borrowBookWithDates(book.getIsbn(),  borrowDate, returnDate);

                    if (success) {
                        JOptionPane.showMessageDialog(BorrowBookPanel.this, "借阅成功！");
                        book.setstock_quantity(book.getstock_quantity() - 1);
                        refreshPanel(bookListPanel); // 刷新图书列表
                        dateDialog.dispose(); // 关闭当前弹窗
                    } else {
                        JOptionPane.showMessageDialog(BorrowBookPanel.this, "借阅失败，请重试。", "错误", JOptionPane.ERROR_MESSAGE);
                        dateDialog.dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(BorrowBookPanel.this, "库存不足，无法借阅。", "库存不足", JOptionPane.WARNING_MESSAGE);
                    dateDialog.dispose();
                }
            });

            // 取消按钮逻辑
            cancelBtn.addActionListener(evt -> dateDialog.dispose());

            // 显示弹窗
            dateDialog.add(contentPanel, BorderLayout.CENTER);
            dateDialog.add(dialogButtonPanel, BorderLayout.SOUTH);
            dateDialog.setVisible(true);
        });



        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(editButton);

        // 组装整个对话框
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(BorrowBookPanel.this); // 居中显示
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
            Book book1 =  filteredBooks.get(i);
            Book book2 = null;

            if (i + 1 < end) {
                book2 =  filteredBooks.get(i + 1);
            }

            // 添加一行两个图书卡片的面板
            bookListPanel.add(createBookRowPanel(book1, book2));
            bookListPanel.add(Box.createRigidArea(new Dimension(0, 10))); // 行间距
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

        searchPanel = new RoundedPanel(30,30,Color.white);
        searchPanel.setMaximumSize(new Dimension(320, 40));
        searchPanel.setMinimumSize(new Dimension(320, 40));
        searchPanel.setPreferredSize(new Dimension(320, 40));
        this.searchField = new JTextField(20);
        JButton searchButton = new JButton();
        BufferedImage img = ImageUtils.loadAndResizeImage(login.class, "/image/search_.png", 20, 20);
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

        JButton refreshButton = new JButton();

        BufferedImage img2 = ImageUtils.loadAndResizeImage(BookListPanel.class, "/image/sx.png", 20, 20);
        if (img2 != null)
        {
            refreshButton.setIcon(new ImageIcon(img2));
        }
        else
        {
            System.err.println("无法加载图片");
        }
        refreshButton.setBackground(Color.WHITE);
        refreshButton.setBorder(null);
        // 添加事件监听器
        searchButton.addActionListener(e -> performSearch());
        refreshButton.addActionListener(e -> {
            searchField.setText("");
            performSearch();
        });
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);
        return searchPanel;
    }
    private void performSearch() {
        String keyword = searchField.getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            filteredBooks.clear();
            filteredBooks.addAll(allBooks);
        } else {
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
            }

            currentPage = 0;
            showCurrentPage(); // 刷新当前页
            refreshPageButtons(pageButtonPanel);

            if (filteredBooks.isEmpty()) {
                JOptionPane.showMessageDialog(
                        BorrowBookPanel.this,
                        "未找到匹配的图书，请尝试其他关键词。",
                        "查询结果",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        BorrowBookPanel.this,
                        "已找到与关键字匹配的图书。",
                        "查询结果",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        }

        currentPage = 0;
        showCurrentPage();
        refreshPageButtons(pageButtonPanel);
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
            BufferedImage img = ImageUtils.loadAndResizeImage(login.class, "/image/cover_image.png", 80, 100);
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
        JButton BorrowBookButton = new JButton("借阅");
        BorrowBookButton.setFocusPainted(false);
        BorrowBookButton.setForeground(Color.white);
        BorrowBookButton.setBackground(new Color(50,125,170));


        detailButton.addActionListener(e -> showBookDetailsDialog(book));

        BorrowBookButton.addActionListener(e -> {
            // 创建日期选择弹窗
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(BorrowBookPanel.this), "选择借阅和归还时间", true);
            dialog.setLayout(new BorderLayout(10, 10));
            dialog.setSize(400, 250);
            dialog.setLocationRelativeTo(BorrowBookPanel.this);

            // 借阅时间输入框
            JLabel borrowLabel = new JLabel("借阅时间:");
            JTextField borrowField = new JTextField(20);
            JButton borrowBtn = new JButton("...");
            JPanel borrowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            borrowPanel.add(borrowLabel);
            borrowPanel.add(borrowField);
            borrowPanel.add(borrowBtn);

            // 归还时间输入框
            JLabel returnLabel = new JLabel("归还时间:");
            JTextField returnField = new JTextField(20);
            JButton returnBtn = new JButton("...");
            JPanel returnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            returnPanel.add(returnLabel);
            returnPanel.add(returnField);
            returnPanel.add(returnBtn);

            // 按钮面板
            JButton confirmBtn = new JButton("确认借阅");
            JButton cancelBtn = new JButton("取消");

            JPanel dialogButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            dialogButtonPanel.add(confirmBtn);
            dialogButtonPanel.add(cancelBtn);

            // 主内容面板
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            contentPanel.add(borrowPanel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            contentPanel.add(returnPanel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            // 初始化默认日期
            Calendar cal = Calendar.getInstance();
            String defaultBorrowDate = String.format("%d-%02d-%02d",
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH)
            );
            cal.add(Calendar.DAY_OF_MONTH, 30); // 默认+30天
            String defaultReturnDate = String.format("%d-%02d-%02d",
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH)
            );

            borrowField.setText(defaultBorrowDate);
            returnField.setText(defaultReturnDate);

            // 添加日期选择器
            SimpleDatePicker borrowDatePicker = new SimpleDatePicker(borrowField);
            SimpleDatePicker returnDatePicker = new SimpleDatePicker(returnField);

            borrowBtn.addActionListener(evt -> {
                SimpleDatePicker datePicker = new SimpleDatePicker(borrowField);
                datePicker.showCalendarPopup();
                borrowField.setText(datePicker.getSelectedDate());
            });

            returnBtn.addActionListener(evt -> {
                SimpleDatePicker datePicker = new SimpleDatePicker(returnField);
                datePicker.showCalendarPopup();
                returnField.setText(datePicker.getSelectedDate());
            });

            // 确认按钮逻辑
            confirmBtn.addActionListener(evt -> {
                String borrowDate = borrowField.getText();
                String returnDate = returnField.getText();

                // 校验日期格式
                try {
                    java.time.LocalDate.parse(borrowDate);
                    java.time.LocalDate.parse(returnDate);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(BorrowBookPanel.this, "日期格式不正确，请使用 YYYY-MM-DD 格式。", "日期错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                BorrowDAO borrowDAO = new BorrowDAO();

                // 校验库存
                if (book.getstock_quantity() > 0) {
                    boolean success = borrowDAO.borrowBookWithDates(book.getIsbn(), borrowDate, returnDate);

                    if (success) {
                        JOptionPane.showMessageDialog(BorrowBookPanel.this, "借阅成功！");
                        book.setstock_quantity(book.getstock_quantity() - 1); // 减少库存
                        refreshPanel(bookListPanel); // 刷新图书列表
                        dialog.dispose(); // 关闭弹窗
                    } else {
                        JOptionPane.showMessageDialog(BorrowBookPanel.this, "借阅失败，请重试。", "错误", JOptionPane.ERROR_MESSAGE);
                        dialog.dispose(); // 即使失败也关闭
                    }
                } else {
                    JOptionPane.showMessageDialog(BorrowBookPanel.this, "库存不足，无法借阅。", "库存不足", JOptionPane.WARNING_MESSAGE);
                    dialog.dispose(); // 关闭弹窗
                }
            });

            // 取消按钮逻辑
            cancelBtn.addActionListener(evt -> dialog.dispose());

            // 将组件添加到对话框
            dialog.add(contentPanel, BorderLayout.CENTER);
            dialog.add(dialogButtonPanel, BorderLayout.SOUTH);

            dialog.setVisible(true);
        });




        buttonPanel.add(detailButton);
        buttonPanel.add(BorrowBookButton);

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
        // 1. 从数据库重新加载所有图书
        loadAllBooks();

        // 2. 清空当前面板内容
        panel.removeAll();

        // 3. 重新构建并添加当前页的图书列表
        showCurrentPage(); // 这个方法内部已经清空 panel 并添加新数据

        // 4. 可选：如果当前页已无数据，自动跳转到上一页或首页
        int totalPages = (int) Math.ceil((double) allBooks.size() / ITEMS_PER_PAGE);
        if (currentPage >= totalPages && totalPages > 0) {
            currentPage = totalPages - 1;
            showCurrentPage();
        }
    }



    public static class SimpleDatePicker extends JPanel {
        private JTextField dateField;
        private int year, month;
        public String getSelectedDate() {
            return dateField.getText(); // 假设 dateField 是 JTextField
        }

        public SimpleDatePicker(JTextField field) {
            this.dateField = field;
            setLayout(new BorderLayout());
            JButton datePickerBtn = new JButton(); // 可以设置图标
            datePickerBtn.setOpaque(true);
            datePickerBtn.setBorder(null);
            datePickerBtn.setFocusPainted(false);
            datePickerBtn.setBackground(Color.white);

            // 加载日历图标（可选）
            BufferedImage imageUrl = ImageUtils.loadAndResizeImage(BookListPanel.class, "/image/calendar.png", 20, 20);
            if (imageUrl != null) {
                datePickerBtn.setIcon(new ImageIcon(imageUrl));
            }
            // 设置按钮大小和边距
            datePickerBtn.setPreferredSize(new Dimension(30, 25));  // 固定大小
            datePickerBtn.setMaximumSize(new Dimension(30, 25));

            // 使用 JPanel 包裹按钮，防止布局冲突
            JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
            wrapper.setBackground(Color.WHITE);
            wrapper.add(datePickerBtn);

            add(wrapper, BorderLayout.EAST);  // 添加到东侧，避免挤占主输入框空间

            datePickerBtn.addActionListener(e -> showCalendarPopup());
        }

        private void showCalendarPopup() {
            JDialog dialog = new JDialog((Frame) null, "选择日期", true);
            dialog.setLayout(new BorderLayout());

            JPanel calendarPanel = new JPanel(new GridLayout(7, 7));
            String[] days = {"日", "一", "二", "三", "四", "五", "六"};
            for (String day : days) {
                JLabel label = new JLabel(day, SwingConstants.CENTER);
                label.setBorder(BorderFactory.createLineBorder(Color.black));
                calendarPanel.add(label);
            }

            Calendar cal = Calendar.getInstance();
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);

            updateCalendar(cal, calendarPanel, dialog);

            JButton prevBtn = new JButton("<");
            JButton nextBtn = new JButton(">");
            JButton okBtn = new JButton("确定");

            JPanel controlPanel = new JPanel(new BorderLayout());
            JLabel monthLabel = new JLabel(getMonthName(month) + " " + year, SwingConstants.CENTER);
            controlPanel.add(prevBtn, BorderLayout.WEST);
            controlPanel.add(monthLabel, BorderLayout.CENTER);
            controlPanel.add(nextBtn, BorderLayout.EAST);

            dialog.add(controlPanel, BorderLayout.NORTH);
            dialog.add(calendarPanel, BorderLayout.CENTER);
            dialog.add(okBtn, BorderLayout.SOUTH);

            prevBtn.addActionListener(e -> {
                if (month == 0) {
                    month = 11;
                    year--;
                } else {
                    month--;
                }
                monthLabel.setText(getMonthName(month) + " " + year);
                updateCalendar(Calendar.getInstance(), calendarPanel, dialog);
            });

            nextBtn.addActionListener(e -> {
                if (month == 11) {
                    month = 0;
                    year++;
                } else {
                    month++;
                }
                monthLabel.setText(getMonthName(month) + " " + year);
                updateCalendar(Calendar.getInstance(), calendarPanel, dialog);
            });

            okBtn.addActionListener(e -> {
                dialog.dispose();
            });

            dialog.setSize(300, 280);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        }

        private void updateCalendar(Calendar baseCal, JPanel panel, JDialog dialog) {
            panel.removeAll();

            Calendar cal = (Calendar) baseCal.clone();
            cal.set(year, month, 1);

            int startDay = cal.get(Calendar.DAY_OF_WEEK);
            int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

            for (int i = 1; i < startDay; i++) {
                panel.add(new JLabel(""));
            }

            for (int day = 1; day <= daysInMonth; day++) {
                JLabel dayLabel = new JLabel(String.valueOf(day), SwingConstants.CENTER);
                dayLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                int finalDay = day;
                dayLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        String selectedDate = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", finalDay);
                        dateField.setText(selectedDate);
                        dialog.dispose();
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        dayLabel.setBackground(Color.LIGHT_GRAY);
                        dayLabel.setOpaque(true);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        dayLabel.setBackground(null);
                        dayLabel.setOpaque(false);
                    }
                });
                panel.add(dayLabel);
            }

            dialog.revalidate();
            dialog.repaint();
        }

        private String getMonthName(int month) {
            String[] months = {"一月", "二月", "三月", "四月", "五月", "六月",
                    "七月", "八月", "九月", "十月", "十一月", "十二月"};
            return months[month];
        }
    }

    public BorrowBookPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));

        // 标题面板
        JPanel titleContent = CommonUIUtils.createTitlePanel("借阅图书");
        titleContent.setAlignmentX(Component.LEFT_ALIGNMENT);

        Box titleBox = Box.createHorizontalBox();
        titleBox.add(titleContent);
        titleBox.add(Box.createHorizontalGlue()); // 右侧填充实现右对齐效果

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
        add(createPaginationPanel());

        // 初始加载数据和页面
        loadAllBooks(); //  加载全部书籍
        filteredBooks.addAll(allBooks); // 初始化过滤列表
        currentPage = 0; // 设置当前页为第一页
        showCurrentPage(); // 显示当前页内容

        // 直接刷新 pageButtonPanel
        refreshPageButtons(pageButtonPanel);
    }

    private JPanel createPaginationPanel() {
        paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        paginationPanel.setBackground(Color.WHITE);

        JButton prevButton = new JButton("上一页");
        JButton nextButton = new JButton("下一页");

        pageButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        refreshPageButtons(pageButtonPanel); // 初始刷新页码按钮

        JLabel jumpLabel = new JLabel("跳转至第");
        pageJumpField = new JTextField(3);
        JButton jumpButton = new JButton("跳转");

        JPanel jumpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        jumpPanel.setBackground(Color.WHITE);
        jumpPanel.add(jumpLabel);
        jumpPanel.add(pageJumpField);
        jumpPanel.add(new JLabel("页"));
        jumpPanel.add(jumpButton);

        paginationPanel.add(prevButton);
        paginationPanel.add(pageButtonPanel);
        paginationPanel.add(nextButton);
        paginationPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        paginationPanel.add(jumpPanel);

        // 上一页事件
        prevButton.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                showCurrentPage();
                refreshPageButtons(pageButtonPanel);
            }
        });

        // 下一页事件
        nextButton.addActionListener(e -> {
            if ((currentPage + 1) * ITEMS_PER_PAGE < filteredBooks.size()) {
                currentPage++;
                showCurrentPage();
                refreshPageButtons(pageButtonPanel);
            }
        });

        // 跳转事件
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
                    JOptionPane.showMessageDialog(BorrowBookPanel.this,
                            "请输入有效的页码（1~" + totalPages + "）", "页码错误", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(BorrowBookPanel.this,
                        "请输入有效的数字页码", "格式错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        return paginationPanel;
    }

}
