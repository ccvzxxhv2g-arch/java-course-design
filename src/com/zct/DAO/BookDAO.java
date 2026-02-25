package com.zct.DAO;


import com.zct.bean.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    public boolean addBook(Book book) {
        System.out.println("开始添加图书: " + book.getBook_Name()); // 调试日志

        String sql = "INSERT INTO books (cover_image, book_name, isbn, author, description, category, price, publish_date, publisher, stock_quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DAO.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 设置参数
            if (book.getCoverImagePath() != null) {
                pstmt.setString(1, book.getCoverImagePath());
            } else {
                pstmt.setNull(1, java.sql.Types.BLOB);
            }

            pstmt.setString(2, book.getBook_Name());
            pstmt.setString(3, book.getIsbn());
            pstmt.setString(4, book.getAuthor());
            pstmt.setString(5, book.getDescription());
            pstmt.setString(6, book.getCategory());
            pstmt.setDouble(7, book.getPrice());

            // 处理可能为null的日期
            if (book.getPublishDate() != null) {
                pstmt.setDate(8, book.getPublishDate());
            } else {
                pstmt.setNull(8, java.sql.Types.DATE);
            }

            pstmt.setString(9, book.getPublisher());
            pstmt.setInt(10, book.getstock_quantity());

            // 执行插入
            int rows = pstmt.executeUpdate();
            System.out.println("图书添加结果: " + (rows > 0)); // 调试日志
            return rows > 0;
        } catch (SQLException ex) {
            System.err.println("插入图书数据时出现异常: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
    public Book getBookById(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (Connection conn = DAO.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Book book = new Book();
                    book.setId(rs.getInt("id"));
                    book.setCoverImagePath(rs.getString("cover_image"));
                    book.setBook_Name(rs.getString("book_name"));
                    book.setIsbn(rs.getString("isbn"));
                    book.setAuthor(rs.getString("author"));
                    book.setDescription(rs.getString("description"));
                    book.setCategory(rs.getString("category"));
                    book.setPrice(rs.getDouble("price"));
                    book.setPublishDate(rs.getDate("publish_date"));
                    book.setPublisher(rs.getString("publisher"));
                    book.setstock_quantity(rs.getInt("stock_quantity"));
                    return book;
                }
            }
        } catch (SQLException ex) {
            System.err.println("查询图书失败: " + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }
    public Book getBookByIsbn(String isbn) {
        String sql = "SELECT * FROM books WHERE isbn = ?";
        try (Connection conn = DAO.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, isbn);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Book book = new Book();
                    book.setId(rs.getInt("id"));
                    book.setBook_Name(rs.getString("book_name"));
                    book.setIsbn(rs.getString("isbn"));
                    book.setAuthor(rs.getString("author"));
                    return book;
                }
            }
        } catch (SQLException ex) {
            System.err.println("根据ISBN查询图书失败: " + ex.getMessage());
            ex.printStackTrace();
        }

        return null;
    }
    public Book getBookByTitleAndAuthor(String title, String author) {
        String sql = "SELECT * FROM books WHERE book_name = ? AND author = ?";
        try (Connection conn = DAO.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, author);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Book book = new Book();
                    book.setId(rs.getInt("id"));
                    book.setBook_Name(rs.getString("book_name"));
                    book.setIsbn(rs.getString("isbn"));
                    book.setAuthor(rs.getString("author"));
                    return book;
                }
            }
        } catch (SQLException ex) {
            System.err.println("根据书名和作者查询图书失败: " + ex.getMessage());
            ex.printStackTrace();
        }

        return null;
    }



    public boolean deleteBook(Long id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = DAO.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            System.err.println("删除图书失败: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET book_name=?, isbn=?, author=?, price=?, publish_date=?, publisher=?, stock_quantity=?,description=?,category=?,cover_image=? WHERE id=?";
        try (Connection conn = DAO.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getBook_Name());
            pstmt.setString(2, book.getIsbn());
            pstmt.setString(3, book.getAuthor());
            pstmt.setDouble(4, book.getPrice());
            if (book.getPublishDate() != null) {
                pstmt.setDate(5, new java.sql.Date(book.getPublishDate().getTime()));
            } else {
                pstmt.setNull(5, java.sql.Types.DATE);
            }
            pstmt.setString(6, book.getPublisher());
            pstmt.setInt(7, book.getstock_quantity());
            pstmt.setString(8, book.getDescription());
            pstmt.setString(9, book.getCategory());
            if (book.getCoverImagePath() != null) {
                pstmt.setString(10, book.getCoverImagePath());
            } else {
                pstmt.setNull(10, java.sql.Types.BLOB);
            }
            pstmt.setLong(11, book.getId());

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException ex) {
            System.err.println("更新图书失败: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT id, book_name, isbn, author, category, price, stock_quantity,cover_image,description,publish_date,publisher FROM books";

        try (Connection conn = DAO.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Book book = new Book();
                book.setId((int) rs.getLong("id"));
                book.setBook_Name(rs.getString("book_name"));
                book.setIsbn(rs.getString("isbn"));
                book.setAuthor(rs.getString("author"));
                book.setCategory(rs.getString("category"));
                book.setPrice(rs.getDouble("price"));
                book.setCoverImagePath(rs.getString("cover_image")); // 确保字段名一致
                book.setDescription(rs.getString("description"));
                book.setPublishDate(rs.getDate("publish_date"));
                book.setPublisher(rs.getString("publisher"));
                book.setstock_quantity(rs.getInt("stock_quantity"));
                books.add(book);
            }

        } catch (SQLException ex) {
            System.err.println("查询图书列表失败: " + ex.getMessage());
            ex.printStackTrace();
        }

        return books;
    }


}