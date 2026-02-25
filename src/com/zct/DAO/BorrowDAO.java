package com.zct.DAO;

import com.zct.bean.BorrowRecord;
import com.zct.bean.Book;
import com.zct.tool.SaveUserStateTool;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BorrowDAO {

    private Connection connection;

    public BorrowDAO() {
        // 初始化数据库连接
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/libraryman_sys", "root", "123456");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取用户角色：管理员 或 用户
     *
     * @param name 用户名
     * @return "admin" 或 "user"
     */
    public String getUserRole(String name) {
        String checkAttendant = "SELECT COUNT(*) FROM attendant_message WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(checkAttendant)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return "admin";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "user"; // 默认普通用户
    }



    // 获取用户已借未还数量
    public int getBorrowedBooksCount(String name) {
        String sql = "SELECT COUNT(*) FROM book_borrow_records WHERE name = ? AND ret = 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 用户借阅图书
     *
     * @param name 借书人用户名
     * @param ISBN     图书ISBN编号
     * @return 是否借阅成功
     */
    public boolean borrowBook(String name, String ISBN) {
        String sqlInsert = "INSERT INTO book_borrow_records (ISBN, name, create_time, end_time, ret) VALUES (?, ?, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 1)";
        String sqlUpdateStock = "UPDATE books SET stock_quantity = stock_quantity - 1 WHERE ISBN = ? AND stock_quantity > 0";

        try (PreparedStatement stmt1 = connection.prepareStatement(sqlInsert);
             PreparedStatement stmt2 = connection.prepareStatement(sqlUpdateStock)) {

            stmt1.setString(1, ISBN);
            stmt1.setString(2, name);
            stmt2.setString(1, ISBN);

            return stmt1.executeUpdate() > 0 && stmt2.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 用户归还图书
     *
     * @param ISBN     图书ISBN编号
     * @param name 借阅人用户名
     * @return 是否归还成功
     */
    public boolean returnBook(String ISBN, String name) {
        String sqlUpdateRecord = "UPDATE book_borrow_records SET update_time = NOW(), ret = 0 WHERE ISBN = ? AND name = ? AND ret = 1";
        String sqlUpdateStock = "UPDATE books SET stock_quantity = stock_quantity + 1 WHERE ISBN = ?";
        System.out.println("尝试归还 ISBN: " + ISBN + ", 用户名: " + name);
        try (PreparedStatement stmt1 = connection.prepareStatement(sqlUpdateRecord);
             PreparedStatement stmt2 = connection.prepareStatement(sqlUpdateStock)) {

            stmt1.setString(1, ISBN);
            stmt1.setString(2, name);
            stmt2.setString(1, ISBN);

            return stmt1.executeUpdate() > 0 && stmt2.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean renewBook(String ISBN, String name, Date newDueDate) {
        String sql = "UPDATE book_borrow_records SET end_time = ? WHERE ISBN = ? AND name = ? AND ret = 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, newDueDate);
            stmt.setString(2, ISBN);
            stmt.setString(3, name);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    /**
     * 获取用户的当前所有未归还的借阅记录
     *
     * @param name 用户名
     * @return 借阅记录列表
     */
    public List<BorrowRecord> getBorrowRecordsByUser(String name) {
        String sql = "SELECT * FROM book_borrow_records WHERE name = ? AND ret = 1";
        List<BorrowRecord> records = new ArrayList<>();
        System.out.println("正在查询用户 [" + name + "] 的借阅记录...");


        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                BorrowRecord record = new BorrowRecord();
                record.setId(rs.getInt("id"));
                record.setISBN(rs.getString("ISBN"));
                record.setName(rs.getString("name"));
                record.setCreate_time(rs.getDate("create_time"));
                record.setUpdate_time(rs.getDate("update_time"));
                record.setEnd_time(rs.getDate("end_time"));
                record.setRet(rs.getInt("ret"));
                records.add(record);
            }

        } catch (SQLException e) {
            System.err.println("查询借阅记录失败: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("从数据库获取到的借阅记录数量：" + records.size()); // 调试输
        return records;
    }

    /**
     * 判断某本图书是否已逾期
     *
     * @param record 借阅记录
     * @return 是否逾期
     */
    public boolean isOverdue(BorrowRecord record) {
        Date now = new Date(System.currentTimeMillis());
        return now.after(record.getEnd_time());
    }

    /**
     * 获取图书信息（用于检查库存）
     *
     * @param ISBN 图书ISBN
     * @return 图书实体
     */
    public Book getBookByISBN(String ISBN) {
        String sql = "SELECT * FROM books WHERE ISBN = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, ISBN);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setIsbn(rs.getString("ISBN"));
                book.setBook_Name(rs.getString("book_Name"));
                book.setCoverImagePath(rs.getString("cover_image"));
                book.setAuthor(rs.getString("author"));
                book.setPublisher(rs.getString("publisher"));
                book.setCategory(rs.getString("category"));
                book.setstock_quantity(rs.getInt("stock_quantity"));
                return book;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新图书库存数量
     *
     * @param ISBN   图书ISBN
     * @param change 变化值（+1 或 -1）
     * @return 是否更新成功
     */
    public boolean updatestock_quantity(String ISBN, int change) {
        String sql = "UPDATE books SET stock_quantity = stock_quantity + ? WHERE ISBN = ? AND stockQuantity + ? >= 0";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, change);
            stmt.setString(2, ISBN);
            stmt.setInt(3, change);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean borrowBookWithDates(String ISBN, String borrowDate, String returnDate) {
        // 从工具类中获取当前用户名，若为空则设为 "test"
        String name = SaveUserStateTool.getUsername();
        if (name == null || name.isEmpty()) {
            name = "test"; // 默认值
        }

        String sqlInsert = "INSERT INTO book_borrow_records (ISBN, name, create_time, end_time, ret) VALUES (?, ?, ?, ?, 1)";
        String sqlUpdateStock = "UPDATE books SET stock_quantity = stock_quantity - 1 WHERE ISBN = ? AND stock_quantity > 0";

        try (PreparedStatement stmt1 = connection.prepareStatement(sqlInsert);
             PreparedStatement stmt2 = connection.prepareStatement(sqlUpdateStock)) {

            stmt1.setString(1, ISBN);
            stmt1.setString(2, name); // 使用工具类提供的用户名
            stmt1.setDate(3, Date.valueOf(borrowDate));
            stmt1.setDate(4, Date.valueOf(returnDate));
            stmt2.setString(1, ISBN);

            return stmt1.executeUpdate() > 0 && stmt2.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取所有已借出的图书记录
     *
     * @return 已借图书列表
     */
    public List<BorrowRecord> getAllBorrowedBooks() {
        String sql = "SELECT * FROM book_borrow_records WHERE ret = 1";
        List<BorrowRecord> records = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                BorrowRecord record = new BorrowRecord();
                record.setId(rs.getInt("id"));
                record.setISBN(rs.getString("ISBN"));
                record.setName(rs.getString("name"));
                record.setCreate_time(rs.getDate("create_time"));
                record.setUpdate_time(rs.getDate("update_time"));
                record.setEnd_time(rs.getDate("end_time"));
                record.setRet(rs.getInt("ret"));
                records.add(record);
            }

        } catch (SQLException e) {
            System.err.println("查询所有已借图书失败: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("从数据库获取到已借图书数量：" + records.size());
        return records;
    }
    /**
     * 根据任意字段模糊查询借阅记录
     *
     * @param column 字段名（如 "name", "ISBN", "create_time"）
     * @param value  查询值
     * @return 匹配的借阅记录列表
     */
    public List<BorrowRecord> searchBorrowRecordsByField(String column, String value) {
        String sql = "SELECT * FROM book_borrow_records WHERE `" + column + "` LIKE ?";
        List<BorrowRecord> records = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + value + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                BorrowRecord record = new BorrowRecord();
                record.setId(rs.getInt("id"));
                record.setISBN(rs.getString("ISBN"));
                record.setName(rs.getString("name"));
                record.setCreate_time(rs.getDate("create_time"));
                record.setUpdate_time(rs.getDate("update_time"));
                record.setEnd_time(rs.getDate("end_time"));
                record.setRet(rs.getInt("ret"));
                records.add(record);
            }

        } catch (SQLException e) {
            System.err.println("根据字段 [" + column + "] 查询失败: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("查询到匹配记录数: " + records.size());
        return records;
    }

}
