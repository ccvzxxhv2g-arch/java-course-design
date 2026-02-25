// File: src/com/zct/frame/ReturnBookPanel.java

package com.zct.frame;

import com.zct.DAO.BorrowDAO;
import com.zct.bean.BorrowRecord;
import com.zct.bean.Book;
import com.zct.tool.CommonUIUtils;
import com.zct.tool.ImageUtils;
import com.zct.tool.SaveUserStateTool;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class ReturnBookPanel extends JPanel {
    private BorrowedListPanel borrowedListPanel;
    private BorrowDAO borrowDAO = new BorrowDAO();
    private JButton refreshButton;
    public ReturnBookPanel(String name) {
        initUI();

        borrowedListPanel.refresh(SaveUserStateTool.getAdmin().getName()); // 刷新借阅列表
    }

    // 刷新数据的方法
    private void refreshData() {
        String username = SaveUserStateTool.getAdmin().getName();
        if (username == null || username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "用户信息异常，请重新登录", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 更新用户信息
        nameLabel.setText(username);

        int borrowedCount = borrowDAO.getBorrowedBooksCount(username);
        int borrowable = Math.max(10 - borrowedCount, 0);
        borrowableNumLabel.setText(String.valueOf(borrowable));

        // 刷新表格数据
        borrowedListPanel.refresh(username);
    }

    private void initUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setMinimumSize(new Dimension(580, 800));
        setPreferredSize(new Dimension(600, 900));
        setMaximumSize(new Dimension(620, 10000));
        setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));

        JPanel titleContent = CommonUIUtils.createTitlePanel("归还图书");
        titleContent.setAlignmentX(Component.LEFT_ALIGNMENT);

        Box titleBox = Box.createHorizontalBox();
        titleBox.add(titleContent);
        titleBox.add(Box.createHorizontalGlue()); // 右侧填充实现右对齐效果

         add(titleBox);
        add(Box.createRigidArea(new Dimension(0, 30)));
        // 用户名面板
        add(namePanel());
        add(Box.createRigidArea(new Dimension(0, 30)));

        // 角色面板
        add(statusPanel());
        add(Box.createRigidArea(new Dimension(0, 30)));

        // 可借数量面板
        add(numPanel());
        add(Box.createRigidArea(new Dimension(0, 30)));

        // 借阅列表面板
        add(borrowLIstPanel());
        add(Box.createRigidArea(new Dimension(0, 30)));

        // ====== 添加刷新按钮 ======
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        refreshButton = new JButton("刷新");
        refreshButton.setBackground(new Color(50, 125, 170));
        refreshButton.setForeground(Color.WHITE);

        refreshButton.addActionListener(e -> {
            refreshData(); // 调用刷新方法
        });

        buttonPanel.add(refreshButton);
        add(buttonPanel);

        // 加载真实数据
        borrowedListPanel = new BorrowedListPanel(SaveUserStateTool.getAdmin().getName());
        add(borrowedListPanel);

    }

    // 姓名面板
    private JPanel namePanel() {
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
        namePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        namePanel.setBackground(Color.white);

        JLabel label = new JLabel("姓名 ");
        label.setPreferredSize(new Dimension(70, 30));

        nameLabel = new JLabel();
        nameLabel.setPreferredSize(new Dimension(200, 30));
        nameLabel.setText(SaveUserStateTool.getAdmin().getName());

        namePanel.add(Box.createRigidArea(new Dimension(30, 0)));
        namePanel.add(label);
        namePanel.add(nameLabel);

        return namePanel;
    }

    private JLabel nameLabel; //显示用户名

    // 身份面板
    private JPanel statusPanel() {
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusPanel.setBackground(Color.white);

        JLabel label = new JLabel("身份 ");
        label.setPreferredSize(new Dimension(70, 30));

        roleLabel = new JLabel();
        roleLabel.setPreferredSize(new Dimension(200, 30));
        roleLabel.setText(SaveUserStateTool.isAdmin() ? "管理员" : "用户");

        statusPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        statusPanel.add(label);
        statusPanel.add(roleLabel);
        return statusPanel;
    }


    private JLabel roleLabel; // 显示身份（管理员/用户）

    // 可借数量面板
    private JPanel numPanel() {
        JPanel numPanel = new JPanel();
        numPanel.setLayout(new BoxLayout(numPanel, BoxLayout.X_AXIS));
        numPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        numPanel.setBackground(Color.white);

        JLabel label = new JLabel("可借数量 ");
        label.setPreferredSize(new Dimension(70, 30));

        borrowableNumLabel = new JLabel();
        borrowableNumLabel.setPreferredSize(new Dimension(200, 30));
        int borrowedCount = borrowDAO.getBorrowedBooksCount(SaveUserStateTool.getAdmin().getName());
        int borrowable = Math.max(10 - borrowedCount, 0); // 最多 10 本
        borrowableNumLabel.setText(String.valueOf(borrowable));

        numPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        numPanel.add(label);
        numPanel.add(borrowableNumLabel);
        return numPanel;
    }

    private JLabel borrowableNumLabel;



    private JPanel borrowLIstPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBackground(Color.white);
        JLabel label = new JLabel("已借列表 ");
        label.setPreferredSize(new Dimension(70, 30));
        panel.add(Box.createRigidArea(new Dimension(30, 0)));
        panel.add(label);
        return panel;
    }

    public BorrowedListPanel getBorrowedListPanel() {
        return borrowedListPanel;
    }

    public class BorrowedBook {
        private String coverImagePath;
        private String bookName;
        private String author;
        private String isbn;
        private String publisher;
        private String category;
        private String borrowDate;
        private String dueDate;
        private String returnDate;
        private boolean isOverdue;

        public BorrowedBook(String coverImagePath, String bookName, String author, String isbn,
                            String publisher, String category, String borrowDate, String dueDate, String returnDate,boolean isOverdue) {
            this.coverImagePath = coverImagePath;
            this.bookName = bookName;
            this.author = author;
            this.isbn = isbn;
            this.publisher = publisher;
            this.category = category;
            this.borrowDate = borrowDate;
            this.dueDate = dueDate;
            this.returnDate = returnDate;
            this.isOverdue = isOverdue;
        }

        public String getCoverImagePath() { return coverImagePath; }
        public String getBookName() { return bookName; }
        public String getAuthor() { return author; }
        public String getIsbn() { return isbn; }
        public String getPublisher() { return publisher; }
        public String getCategory() { return category; }
        public String getBorrowDate() { return borrowDate; }
        public String getDueDate() { return dueDate; }
        public String getReturnDate() { return returnDate; }
        public boolean isOverdue() { return isOverdue; }
    }

    public class BorrowedBookTableModel extends AbstractTableModel {
        private List<BorrowedBook> borrowedBooks;
        private String[] columnNames = {"封面", "书名", "作者", "ISBN", "出版社", "分类", "借阅时间", "应还时间", "归还时间", "是否逾期", "操作"};

        public void refreshData(String username) {
            this.borrowedBooks.clear();
            this.borrowedBooks.addAll(loadBorrowedBooksFromDB(username));
            fireTableDataChanged();
        }

        // 加载数据
        public List<BorrowedBook> loadBorrowedBooksFromDB(String username) {
            BorrowDAO borrowDAO = new BorrowDAO();
            List<BorrowRecord> records = borrowDAO.getBorrowRecordsByUser(username);
            List<BorrowedBook> result = new ArrayList<>();

            for (BorrowRecord record : records) {
                Book book = borrowDAO.getBookByISBN(record.getISBN());
                if (book != null) {
                    result.add(new BorrowedBook(
                            book.getCoverImagePath(),
                            book.getBook_Name(),
                            book.getAuthor(),
                            book.getIsbn(),
                            book.getPublisher(),
                            book.getCategory(),
                            record.getCreate_time().toString(),
                            record.getEnd_time().toString(),
                            (record.getUpdate_time() != null ? record.getUpdate_time().toString() : "未归还"),
                            borrowDAO.isOverdue(record)
                    ));
                }
            }

            return result;
        }


        public BorrowedBookTableModel(List<BorrowedBook> borrowedBooks) {
            this.borrowedBooks = borrowedBooks;
        }

        @Override
        public int getRowCount() { return borrowedBooks.size(); }

        @Override
        public int getColumnCount() { return columnNames.length; }

        @Override
        public String getColumnName(int column) { return columnNames[column]; }

        @Override
        public Object getValueAt(int row, int column) {
            BorrowedBook book = borrowedBooks.get(row);
            switch (column) {
                case 0: return book.getCoverImagePath();
                case 1: return book.getBookName();
                case 2: return book.getAuthor();
                case 3: return book.getIsbn();
                case 4: return book.getPublisher();
                case 5: return book.getCategory();
                case 6: return book.getBorrowDate();
                case 7: return book.getDueDate();
                case 8: return book.getReturnDate(); // 返回归还时间字符串
                case 9: return book.isOverdue() ? "是" : "否";
                case 10: return new JButton[] {new JButton("续借"), new JButton("归还")};
                default: return null;
            }
        }
        public void updateBook(int row, BorrowedBook updatedBook) {
            if (row >= 0 && row < borrowedBooks.size()) {
                borrowedBooks.set(row, updatedBook);
                fireTableCellUpdated(row, 7);  // 刷新 "应还时间" 列（索引 7）
                fireTableCellUpdated(row, 9);  // 刷新 "是否逾期" 列（索引 9）
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) return ImageIcon.class;
            else if (columnIndex == 9) return JButton[].class;
            return super.getColumnClass(columnIndex);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 10;
        }

        public BorrowedBook getBookAtRow(int row) {
            return borrowedBooks.get(row);
        }
    }

    public class BorrowedListPanel extends JPanel {
        private JTable table;
        private BorrowedBookTableModel model;

        public BorrowedListPanel(String username) {
            setLayout(new BorderLayout());

            List<BorrowedBook> data = loadBorrowedBooksFromDB(username);
            model = new BorrowedBookTableModel(data);

            table = new JTable(model);
            table.setRowHeight(100);
            table.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());
            table.getColumnModel().getColumn(10).setCellRenderer(new MultiButtonRenderer());
            table.getColumnModel().getColumn(10).setCellEditor(new MultiButtonEditor(model, SaveUserStateTool.getAdmin().getName(), this));


            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);
        }
        /**
         * 刷新表格数据
         */
        public void refresh(String username) {
            model.refreshData(username); // 调用模型刷新数据
            table.updateUI(); // 更新 UI
        }

        private List<BorrowedBook> loadBorrowedBooksFromDB(String username) {
            BorrowDAO borrowDAO = new BorrowDAO();
            List<BorrowRecord> records = borrowDAO.getBorrowRecordsByUser(username);
            List<BorrowedBook> result = new ArrayList<>();

            for (BorrowRecord record : records) {
                System.out.println("record.ISBN: " + record.getISBN());
                Book book = borrowDAO.getBookByISBN(record.getISBN());
                if (book != null) {
                    result.add(new BorrowedBook(
                            book.getCoverImagePath(),
                            book.getBook_Name(),
                            book.getAuthor(),
                            book.getIsbn(),
                            book.getPublisher(),
                            book.getCategory(),
                            record.getCreate_time().toString(),
                            record.getEnd_time().toString(),
                            (record.getUpdate_time() != null ? record.getUpdate_time().toString() : "未归还"),
                            borrowDAO.isOverdue(record)
                    ));
                }
            }

            return result;
        }
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
                System.out.println("Image path: " + imagePath);

                try {
                    // 尝试作为本地文件路径加载
                    File imageFile = new File(imagePath);
                    if (imageFile.exists()) {
                        BufferedImage originalImage = ImageIO.read(imageFile);
                        Image scaledImage = originalImage.getScaledInstance(60, 80, Image.SCALE_SMOOTH);
                        setIcon(new ImageIcon(scaledImage));
                        setText("");
                    } else {
                        BufferedImage img = ImageUtils.loadAndResizeImage(ReturnBookPanel.class, "/image/cover_image.png", 80, 100);
                        setIcon(new ImageIcon(img));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    setIcon(null);
                    setText("??");
                    setForeground(Color.RED);
                }
            } else {
                BufferedImage img = ImageUtils.loadAndResizeImage(ReturnBookPanel.class, "/image/cover_image.png", 80, 100);
                setIcon(new ImageIcon(img));
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
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setOpaque(true);
            panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());

            if (value instanceof JButton[]) {
                for (JButton button : (JButton[]) value) {
                    button.setPreferredSize(new Dimension(80, 25));
                    button.setBackground(Color.LIGHT_GRAY);
                    button.setForeground(Color.BLACK);
                    button.setAlignmentX(Component.CENTER_ALIGNMENT);
                    panel.add(Box.createRigidArea(new Dimension(0, 13)));
                    panel.add(button);
                    panel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }

            return panel;
        }
    }
    public class MultiButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JButton[] buttons;
        private JPanel panel;
        private BorrowedBook book;
        private BorrowedBookTableModel model;
        private int row;


        private String currentUserName;
        private BorrowedListPanel borrowedListPanel;

        public MultiButtonEditor(BorrowedBookTableModel model) {
            this(model, null, null); // 默认构造函数
        }

        public MultiButtonEditor(BorrowedBookTableModel model, String currentUserName, BorrowedListPanel borrowedListPanel) {
            this.model = model;
            this.currentUserName = currentUserName;
            this.borrowedListPanel = borrowedListPanel;

            this.panel = new JPanel();
            this.panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            this.buttons = new JButton[]{
                    new JButton("续借"),
                    new JButton("归还")
            };

            for (JButton btn : buttons) {
                btn.setPreferredSize(new Dimension(80, 25));
                btn.setBackground(Color.LIGHT_GRAY);
                btn.setForeground(Color.BLACK);
                btn.setAlignmentX(Component.CENTER_ALIGNMENT);
                panel.add(Box.createRigidArea(new Dimension(0, 13)));
                panel.add(btn);
                panel.add(Box.createRigidArea(new Dimension(0, 10)));

                btn.addActionListener(e -> {
                    fireEditingStopped();
                    if ("归还".equals(btn.getText())) {
                        // 获取当前书籍信息
                        BorrowDAO borrowDAO = new BorrowDAO();

                        // 更新数据库中的归还状态
                        boolean success = borrowDAO.returnBook(book.getIsbn(), currentUserName);

                        if (success) {
                            JOptionPane.showMessageDialog(panel, "归还成功");

                            // 刷新界面数据
                            BorrowRecord record = borrowDAO.getBorrowRecordsByUser(currentUserName).stream()
                                    .filter(r -> r.getISBN().equals(book.getIsbn()))
                                    .findFirst()
                                    .orElse(null);

                            if (record != null) {
                                Book bookInfo = borrowDAO.getBookByISBN(record.getISBN());
                                if (bookInfo != null) {
                                    BorrowedBook updatedBook = new BorrowedBook(
                                            bookInfo.getCoverImagePath(),
                                            bookInfo.getBook_Name(),
                                            bookInfo.getAuthor(),
                                            bookInfo.getIsbn(),
                                            bookInfo.getPublisher(),
                                            bookInfo.getCategory(),
                                            record.getCreate_time().toString(),
                                            record.getEnd_time().toString(),
                                            new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
                                            borrowDAO.isOverdue(record)
                                    );
                                    model.updateBook(row, updatedBook);
                                }
                            }
                            // 刷新整个列表
                            borrowedListPanel.refresh(currentUserName);

                        } else {
                            JOptionPane.showMessageDialog(panel, "归还失败，请重试");
                        }
                    }

                    if ("续借".equals(btn.getText())) {
                        if (book.isOverdue()) {
                            JOptionPane.showMessageDialog(panel, "该书已逾期，无法续借");
                            return;
                        }

                        // 弹出输入框让用户输入续借天数
                        String input = JOptionPane.showInputDialog(panel, "请输入要续借的天数（1-10 天）：", "续借天数", JOptionPane.QUESTION_MESSAGE);

                        if (input == null || input.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(panel, "输入为空，取消续借");
                            return;
                        }

                        int days;
                        try {
                            days = Integer.parseInt(input);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(panel, "请输入有效的数字");
                            return;
                        }

                        if (days < 1 || days > 10) {
                            JOptionPane.showMessageDialog(panel, "续借天数必须在 1-10 天之间");
                            return;
                        }

                        // 解析当前应还时间
                        Date dueDate;
                        try {
                            dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(book.getDueDate());
                        } catch (ParseException ex) {
                            JOptionPane.showMessageDialog(panel, "日期解析失败");
                            return;
                        }

                        // 计算新应还时间
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dueDate);
                        cal.add(Calendar.DAY_OF_MONTH, days);
                        java.sql.Date newDueDate = new java.sql.Date(cal.getTimeInMillis());

                        // 调用 DAO 更新数据库
                        BorrowDAO borrowDAO = new BorrowDAO();
                        boolean success = borrowDAO.renewBook(book.getIsbn(), currentUserName, newDueDate);

                        if (success) {
                            JOptionPane.showMessageDialog(panel, "续借成功：" + days + " 天");

                            // 重新加载记录并刷新界面
                            BorrowRecord record = borrowDAO.getBorrowRecordsByUser(currentUserName).stream()
                                    .filter(r -> r.getISBN().equals(book.getIsbn()))
                                    .findFirst()
                                    .orElse(null);

                            if (record != null) {
                                Book bookInfo = borrowDAO.getBookByISBN(record.getISBN());
                                if (bookInfo != null) {
                                    BorrowedBook updatedBook = new BorrowedBook(
                                            bookInfo.getCoverImagePath(),
                                            bookInfo.getBook_Name(),
                                            bookInfo.getAuthor(),
                                            bookInfo.getIsbn(),
                                            bookInfo.getPublisher(),
                                            bookInfo.getCategory(),
                                            record.getCreate_time().toString(),
                                            record.getEnd_time().toString(),
                                            (record.getUpdate_time() != null ? record.getUpdate_time().toString() : "未归还"),
                                            borrowDAO.isOverdue(record)
                                    );
                                    model.updateBook(row, updatedBook); // 刷新特定行
                                }
                            }
                            borrowedListPanel.refresh(currentUserName);
                        } else {
                            JOptionPane.showMessageDialog(panel, "续借失败，请重试");
                        }
                    }
                });



            }
        }

        @Override
        public Object getCellEditorValue() {
            return buttons;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            this.book = model.getBookAtRow(row);
            return panel;
        }

        public String getCurrentUserName() {
            return currentUserName;
        }

        public BorrowedListPanel getBorrowedListPanel() {
            return borrowedListPanel;
        }
    }


    public void setBorrowedListPanel(BorrowedListPanel borrowedListPanel) {
        this.borrowedListPanel = borrowedListPanel;
    }
}

