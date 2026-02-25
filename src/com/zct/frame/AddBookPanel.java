package com.zct.frame;

import com.zct.DAO.BookDAO;
import com.zct.bean.Book;
import com.zct.listener.NavigationListener;
import com.zct.tool.CommonUIUtils;
import com.zct.tool.ImageUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddBookPanel extends JPanel {

    private JTextField bookNameField;// 图书名称
    private JTextField isbnField;// 图书编号
    private JTextField authorField;// 作者
    private JTextField priceField;// 价格
    private JTextField publishDateField;// 出版日期
    private JTextField publisherField;// 出版社
    private JTextField stockQuantityField;// 库存数量
    private JTextField descriptionField;// 描述
    private JLabel coverImageLabel = new JLabel();//封面标签
    private File selectedImageFile = null;
    private String selectedCategory = ""; // 当前选择的分类
    private String previousPage = ""; // 保留字段用于记录上一页面


    public AddBookPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setMinimumSize(new Dimension(580, 800));
        setPreferredSize(new Dimension(600, 900));
        setMaximumSize(new Dimension(620, 10000));
        setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));

        add(CommonUIUtils.createTitlePanel("添加图书"));
        add(Box.createRigidArea(new Dimension(0, 30)));

        add(getCoverImagePanel());
        add(Box.createRigidArea(new Dimension(0, 20)));

        add(getBookNamePanel());
        add(Box.createRigidArea(new Dimension(0, 20)));

        add(getIsbnPanel());
        add(Box.createRigidArea(new Dimension(0, 20)));

        add(getAuthorPanel());
        add(Box.createRigidArea(new Dimension(0, 20)));

        add(getDescriptionPanel());
        add(Box.createRigidArea(new Dimension(0, 20)));

        add(getCategoryPanel());
        add(Box.createRigidArea(new Dimension(0, 20)));

        add(getPricePanel());
        add(Box.createRigidArea(new Dimension(0, 20)));

        add(getPublishDatePanel());
        add(Box.createRigidArea(new Dimension(0, 20)));

        add(getPublisherPanel());
        add(Box.createRigidArea(new Dimension(0, 20)));

        add(getStockPanel());
        add(Box.createRigidArea(new Dimension(0, 20)));

        add(getBtnPanel());
    }

    /**
     * 封面面板
     * @return
     */
    private JPanel getCoverImagePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("图书封面");
        label.setFont(new Font("宋体", Font.PLAIN, 16));
        label.setForeground(Color.BLACK);

        coverImageLabel.setPreferredSize(new Dimension(150, 150));
        coverImageLabel.setMaximumSize(new Dimension(150, 150));
        coverImageLabel.setMinimumSize(new Dimension(150, 150));
        coverImageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JButton selectImageButton = new JButton();
        selectImageButton.setFocusPainted(false);
        selectImageButton.setBackground(Color.WHITE);
        selectImageButton.setBorder(null);
        JPanel imageButtonPanel = new JPanel();
        imageButtonPanel.setLayout(new BoxLayout(imageButtonPanel, BoxLayout.Y_AXIS));
        imageButtonPanel.setBackground(Color.WHITE);
        imageButtonPanel.add(Box.createRigidArea(new Dimension(0, 120)));
        imageButtonPanel.add(selectImageButton);

        BufferedImage image = ImageUtils.loadAndResizeImage(AddBookPanel.class, "/image/upload_picture.png", 30, 30);
        if (image != null) {
            selectImageButton.setIcon(new ImageIcon(image));
        } else {
            System.out.println("图片加载失败");
        }

        selectImageButton.addActionListener(this::selectAvatar);

        panel.add(Box.createRigidArea(new Dimension(68, 0)));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(30, 0)));
        panel.add(coverImageLabel);
        panel.add(imageButtonPanel);
        return panel;
    }

    /**
     * 选择封面
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
                    System.err.println("无法读取图片文件");
                    JOptionPane.showMessageDialog(this, "请选择有效的图片文件");
                    return;
                }

                BufferedImage resizedImage = ImageUtils.resizeImage(originalImage, 150, 150);
                coverImageLabel.setIcon(new ImageIcon(resizedImage));
                coverImageLabel.revalidate();
                coverImageLabel.repaint();

            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "读取图片失败: " + ex.getMessage());
            }
        }
    }

    /**
     * 图书名称面板
     * @return
     */
    private JPanel getBookNamePanel() {
        JPanel bookNamePanel = new JPanel();
        bookNamePanel.setLayout(new BoxLayout(bookNamePanel, BoxLayout.X_AXIS));
        bookNamePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        bookNamePanel.setBackground(Color.WHITE);

        JLabel label1 = new JLabel("*");
        label1.setForeground(Color.RED);
        label1.setFont(new Font("宋体", Font.BOLD, 16));
        JLabel label = new JLabel("图书名称");
        label.setFont(new Font("宋体", Font.PLAIN, 16));
        label.setForeground(Color.BLACK);
        bookNameField = new JTextField(20);

        bookNameField.setMaximumSize(new Dimension(600, bookNameField.getPreferredSize().height + 10));
        bookNameField.setPreferredSize(new Dimension(600, bookNameField.getPreferredSize().height + 10));
        setupPlaceholder(bookNameField, "请输入图书名称");

        bookNamePanel.add(Box.createRigidArea(new Dimension(50, 0)));
        bookNamePanel.add(label1);
        bookNamePanel.add(Box.createRigidArea(new Dimension(10, 0)));
        bookNamePanel.add(label);
        bookNamePanel.add(Box.createRigidArea(new Dimension(30, 0)));
        bookNamePanel.add(bookNameField);
        return bookNamePanel;
    }

    /**
     * 编号面板
     * @return
     */
    private JPanel getIsbnPanel() {
        JPanel ISBNPanel = new JPanel();
        ISBNPanel.setLayout(new BoxLayout(ISBNPanel, BoxLayout.X_AXIS));
        ISBNPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        ISBNPanel.setBackground(Color.WHITE);

        JLabel label1 = new JLabel("*");
        label1.setForeground(Color.RED);
        label1.setFont(new Font("宋体", Font.BOLD, 16));
        JLabel label = new JLabel("ISBN编号");
        label.setFont(new Font("宋体", Font.PLAIN, 16));
        label.setForeground(Color.BLACK);
        isbnField = new JTextField(20);

        isbnField.setMaximumSize(new Dimension(600, isbnField.getPreferredSize().height + 10));
        isbnField.setPreferredSize(new Dimension(600, isbnField.getPreferredSize().height + 10));
        setupPlaceholder(isbnField, "请输入ISBN编号");

        ISBNPanel.add(Box.createRigidArea(new Dimension(50, 0)));
        ISBNPanel.add(label1);
        ISBNPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        ISBNPanel.add(label);
        ISBNPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        ISBNPanel.add(isbnField);
        return ISBNPanel;
    }

    /**
     * 分类
     * @return
     */
    private JPanel getCategoryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("图书类别");
        label.setFont(new Font("宋体", Font.PLAIN, 16));
        label.setForeground(Color.BLACK);

        JButton dropdownBtn = new JButton("请选择                              "+"");
        dropdownBtn.setFont(new Font("宋体", Font.PLAIN, 14));
        dropdownBtn.setBackground(Color.WHITE);
        dropdownBtn.setMaximumSize(new Dimension(300, dropdownBtn.getPreferredSize().height + 8));
        dropdownBtn.setPreferredSize(new Dimension(300, dropdownBtn.getPreferredSize().height + 8));
        dropdownBtn.setFocusPainted(false);
        dropdownBtn.setHorizontalAlignment(SwingConstants.LEFT);
        dropdownBtn.setHorizontalTextPosition(SwingConstants.LEFT);

        JPopupMenu popupMenu = new JPopupMenu();

        String[] categories = {"文学　", "科技　", "历史　", "小说　", "教育　", "管理　", "语言　", "其他　"};

        for (String category : categories) {
            JMenuItem item = new JMenuItem(category);
            item.addActionListener(e -> {
                dropdownBtn.setText(category + "                              "+"");
                dropdownBtn.setForeground(Color.BLACK);
                selectedCategory = category.trim();
            });
            popupMenu.add(item);
        }

        dropdownBtn.addActionListener(e -> popupMenu.show(dropdownBtn, 0, dropdownBtn.getHeight()));

        panel.add(Box.createRigidArea(new Dimension(70, 0)));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(28, 0)));
        panel.add(dropdownBtn);

        return panel;
    }

    /**
     * 作者
     * @return
     */
    private JPanel getAuthorPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBackground(Color.WHITE);

        JLabel required = new JLabel("*");
        required.setForeground(Color.RED);
        required.setFont(new Font("宋体", Font.BOLD, 16));

        JLabel label = new JLabel("作者");
        label.setFont(new Font("宋体", Font.PLAIN, 16));
        label.setForeground(Color.BLACK);

        authorField = new JTextField(20);
        authorField.setMaximumSize(new Dimension(600, authorField.getPreferredSize().height + 10));
        authorField.setPreferredSize(new Dimension(600, authorField.getPreferredSize().height + 10));
        setupPlaceholder(authorField, "请输入作者姓名");

        panel.add(Box.createRigidArea(new Dimension(50, 0)));
        panel.add(required);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(62, 0)));
        panel.add(authorField);

        return panel;
    }

    private JPanel getPricePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("价格");
        label.setFont(new Font("宋体", Font.PLAIN, 16));
        label.setForeground(Color.BLACK);

        priceField = new JTextField(20);
        priceField.setMaximumSize(new Dimension(600, priceField.getPreferredSize().height + 10));
        priceField.setPreferredSize(new Dimension(600, priceField.getPreferredSize().height + 10));
        setupPlaceholder(priceField, "请输入图书定价");

        panel.add(Box.createRigidArea(new Dimension(70, 0)));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(60, 0)));
        panel.add(priceField);

        return panel;
    }

    private JPanel getPublishDatePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("出版时间");
        label.setFont(new Font("宋体", Font.PLAIN, 16));
        label.setForeground(Color.BLACK);

        publishDateField = new JTextField(20);
        publishDateField.setMaximumSize(new Dimension(600, publishDateField.getPreferredSize().height + 10));
        publishDateField.setPreferredSize(new Dimension(600, publishDateField.getPreferredSize().height + 10));
        setupPlaceholder(publishDateField, "请选择出版时间（如2024-01-01）");

        SimpleDatePicker datePicker = new SimpleDatePicker(publishDateField);
        datePicker.setMaximumSize(new Dimension(50, publishDateField.getPreferredSize().height + 10));
        datePicker.setPreferredSize(new Dimension(50, publishDateField.getPreferredSize().height + 10));
        datePicker.setBackground(Color.WHITE);

        panel.add(Box.createRigidArea(new Dimension(70, 0)));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(28, 0)));
        panel.add(publishDateField);
        panel.add(datePicker);

        return panel;
    }

    private JPanel getPublisherPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("出版社");
        label.setFont(new Font("宋体", Font.PLAIN, 16));
        label.setForeground(Color.BLACK);

        publisherField = new JTextField(20);
        publisherField.setMaximumSize(new Dimension(600, publisherField.getPreferredSize().height + 10));
        publisherField.setPreferredSize(new Dimension(600, publisherField.getPreferredSize().height + 10));
        setupPlaceholder(publisherField, "请输入出版社名称");

        panel.add(Box.createRigidArea(new Dimension(70, 0)));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(44, 0)));
        panel.add(publisherField);

        return panel;
    }

    private JPanel getStockPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("库存数量");
        label.setFont(new Font("宋体", Font.PLAIN, 16));
        label.setForeground(Color.BLACK);

        stockQuantityField = new JTextField(20);
        stockQuantityField.setMaximumSize(new Dimension(600, stockQuantityField.getPreferredSize().height + 10));
        stockQuantityField.setPreferredSize(new Dimension(600, stockQuantityField.getPreferredSize().height + 10));
        setupPlaceholder(stockQuantityField, "请输入库存数量");

        panel.add(Box.createRigidArea(new Dimension(70, 0)));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(28, 0)));
        panel.add(stockQuantityField);

        return panel;
    }

    private JPanel getDescriptionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("图书简介");
        label.setFont(new Font("宋体", Font.PLAIN, 16));
        label.setForeground(Color.BLACK);

        descriptionField = new JTextField(20);
        descriptionField.setMaximumSize(new Dimension(600, descriptionField.getPreferredSize().height + 10));
        descriptionField.setPreferredSize(new Dimension(600, descriptionField.getPreferredSize().height + 10));
        setupPlaceholder(descriptionField, "请输入图书简介");

        panel.add(Box.createRigidArea(new Dimension(70, 0)));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(28, 0)));
        panel.add(descriptionField);

        return panel;
    }

    private JPanel getBtnPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBackground(Color.WHITE);

        JButton backButton = new JButton("返回");
        backButton.setFont(new Font("宋体", Font.PLAIN, 16));
        backButton.setBackground(Color.white);
        backButton.setForeground(new Color(50, 125, 170));
        backButton.setFocusPainted(false);

        JButton preserveButton = new JButton("保存");
        preserveButton.setFont(new Font("宋体", Font.PLAIN, 16));
        preserveButton.setBackground(new Color(50, 125, 170));
        preserveButton.setForeground(Color.white);
        preserveButton.setFocusPainted(false);
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(Box.createRigidArea(new Dimension(300, 0)));
        panel.add(backButton);
        panel.add(Box.createRigidArea(new Dimension(200, 0)));
        panel.add(preserveButton);

        preserveButton.addActionListener(e -> saveBookToDatabase());
        backButton.addActionListener(e -> {
            // 获取当前窗口（可能是 JDialog 或 JFrame）
            Window window = SwingUtilities.getWindowAncestor(AddBookPanel.this);

            if (window != null) {
                window.dispose(); // 关闭当前窗口

                // 打开图书列表页面
                SwingUtilities.invokeLater(() -> {
                    admin_frame adminFrame = new admin_frame();
                    adminFrame.setVisible(true);
                });
            }
        });

        return panel;

    }


    /**
     * 设置文本框输入提示
     * @param field
     * @param hint
     */
    private void setupPlaceholder(JTextField field, String hint) {
        field.setText(hint);
        field.setForeground(Color.GRAY);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(hint)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(hint);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    /**
     * 日历选择
     */
    public static class SimpleDatePicker extends JPanel {
        private JTextField dateField;
        private int year, month;

        public SimpleDatePicker(JTextField field) {
            this.dateField = field;
            setLayout(new BorderLayout());
            JButton datePickerBtn = new JButton();// ? 使用类加载器加载图片资源
            datePickerBtn.setOpaque(true);
            datePickerBtn.setBorder(null);
            datePickerBtn.setFocusPainted(false);
            datePickerBtn.setBackground(Color.white);
            BufferedImage imageUrl = ImageUtils.loadAndResizeImage(admin_frame.class, "/image/calendar.png", 30, 30);
            if (imageUrl != null) {
                datePickerBtn.setIcon(new ImageIcon(imageUrl));
            } else {
                System.err.println("找不到图片: /image/calendar.png");
            }

            datePickerBtn.addActionListener(e -> showCalendarPopup());
            add(datePickerBtn, BorderLayout.EAST);
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

            // 补齐前面的空白
            for (int i = 1; i < startDay; i++) {
                panel.add(new JLabel(""));
            }

            // 添加每一天
            for (int day = 1; day <= daysInMonth; day++) {
                JLabel dayLabel = new JLabel(String.valueOf(day), SwingConstants.CENTER);
                dayLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                int finalDay = day;
                dayLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        String selectedDate = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", finalDay);
                        dateField.setForeground(Color.BLACK);
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
    private void saveBookToDatabase() {
        // 获取用户输入
        String bookName = bookNameField.getText();
        String isbn = isbnField.getText();
        String author = authorField.getText();
        String priceStr = priceField.getText();
        String publishDateStr = publishDateField.getText();
        String publisher = publisherField.getText();
        String stockStr = stockQuantityField.getText();
        String description = descriptionField.getText();

        // 验证必填字段
        if (bookName.isEmpty() || isbn.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请填写所有必填字段！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 验证价格
        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "价格必须是有效的数字！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 验证库存数量
        int stock;
        try {
            stock = Integer.parseInt(stockStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "库存数量必须是有效的整数！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 验证日期格式
        Date utilDate;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            utilDate = sdf.parse(publishDateStr);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "出版日期格式必须为 yyyy-MM-dd！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // 检查 ISBN 是否已存在
        BookDAO bookDAO = new BookDAO();
        Book existingBook = bookDAO.getBookByIsbn(isbn); // 假设 DAO 中有这个方法
        if (existingBook != null) {
            JOptionPane.showMessageDialog(this, "该 ISBN 已存在，请勿重复添加！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }      // 检查书名+作者是否已存在
        Book existingBookByNameAndAuthor = bookDAO.getBookByTitleAndAuthor(bookName, author);
        if (existingBookByNameAndAuthor != null) {
            JOptionPane.showMessageDialog(this, "该书名和作者组合已存在，可能为重复图书！", "提示", JOptionPane.WARNING_MESSAGE);
            int confirm = JOptionPane.showConfirmDialog(this, "确认继续添加吗？", "警告", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return; // 用户取消操作
            }
        }
        // 构建 Book 对象
        Book book = new Book();
        book.setBook_Name(bookName);
        book.setIsbn(isbn);
        book.setAuthor(author);
        book.setDescription(description);
        book.setCategory(selectedCategory);
        book.setPrice(price);
        book.setPublishDate(new java.sql.Date(utilDate.getTime()));
        book.setPublisher(publisher);
        book.setstock_quantity(stock);

        // 处理封面图并转成 Base64 字符串
        if (selectedImageFile != null) {
            System.out.println("选中的图片路径: " + selectedImageFile.getAbsolutePath());

            if (!selectedImageFile.exists()) {
                JOptionPane.showMessageDialog(this, "文件不存在，请重新选择图片！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!selectedImageFile.canRead()) {
                JOptionPane.showMessageDialog(this, "没有权限读取该文件！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                BufferedImage img = ImageIO.read(selectedImageFile);
                if (img == null) {
                    JOptionPane.showMessageDialog(this, "无法识别图片内容，请选择有效的图片文件。", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 创建目标文件夹
                File targetDir = new File("images/book_covers");
                if (!targetDir.exists()) {
                    targetDir.mkdirs();
                }

                // 使用 ISBN命名文件
                String fileName = book.getIsbn() + ".jpg";
                File destFile = new File(targetDir, fileName);

                // 复制图片文件
                BufferedImage resizedImg = ImageUtils.resizeImage(img, 300, 400); // 可选压缩
                ImageIO.write(resizedImg, "jpg", destFile);

                // 设置图书封面路径
                book.setCoverImagePath("images/book_covers/" + fileName); // 存储相对路径

            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "读取图片失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // 调用 DAO 层保存到数据库
        boolean success = bookDAO.addBook(book);
        if (success) {
            int choice = JOptionPane.showConfirmDialog(this, "书籍添加成功！是否继续添加？", "提示", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                clearFormFields(); // 清空当前表单
                bookNameField.requestFocusInWindow();

            } else {
                bookNameField.requestFocusInWindow();
            }
            NavigationListener.fireRefreshEvent("book_list");
        }
        else {
            JOptionPane.showMessageDialog(this, "图书保存失败，请查看日志！", "错误", JOptionPane.ERROR_MESSAGE);
        };

    }

    private void clearFormFields() {
        bookNameField.setText("");
        isbnField.setText("");
        authorField.setText("");
        priceField.setText("");
        publishDateField.setText("");
        publisherField.setText("");
        stockQuantityField.setText("");
        descriptionField.setText("");
        coverImageLabel.setIcon(null); // 清除封面预览
        selectedImageFile = null;
        selectedCategory = ""; // 重置分类
    }


}
