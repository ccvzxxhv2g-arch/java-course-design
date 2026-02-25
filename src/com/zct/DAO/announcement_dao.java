package com.zct.DAO;

import com.zct.bean.Announcement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class announcement_dao {

    /**
     * 添加一条公告
     */
    public boolean addAnnouncement(Announcement announcement) {
        String sql = "INSERT INTO announcement (title, content, publish_time, publisher_name) VALUES (?, ?, NOW(), ?)";
        try (Connection conn = DAO.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, announcement.getTitle());
            pstmt.setString(2, announcement.getContent());
            pstmt.setString(3, announcement.getPublisherName());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除公告（按ID）
     */
    public boolean deleteAnnouncementById(int id) {
        String sql = "DELETE FROM announcement WHERE id = ?";
        try (Connection conn = DAO.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询所有公告
     */
    public List<Announcement> getAllAnnouncements() {
        List<Announcement> announcements = new ArrayList<>();
        String sql = "SELECT id, title, content, publish_time, publisher_name FROM announcement ORDER BY publish_time DESC";

        try (Connection conn = DAO.getConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Announcement a = new Announcement();
                a.setId(rs.getInt("id"));
                a.setTitle(rs.getString("title"));
                a.setContent(rs.getString("content"));
                a.setPublishTime(rs.getTimestamp("publish_time").toLocalDateTime());
                a.setPublisherName(rs.getString("publisher_name"));
                announcements.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return announcements;
    }

    /**
     * 根据标题关键字进行模糊搜索
     */
    public List<Announcement> searchAnnouncementsByTitle(String keyword) {
        List<Announcement> results = new ArrayList<>();
        String sql = "SELECT id, title, content, publish_time, publisher_name FROM announcement WHERE title LIKE ?";

        try (Connection conn = DAO.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 添加通配符 % 用于模糊查询
            pstmt.setString(1, "%" + keyword + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Announcement a = new Announcement();
                    a.setId(rs.getInt("id"));
                    a.setTitle(rs.getString("title"));
                    a.setContent(rs.getString("content"));
                    a.setPublishTime(rs.getTimestamp("publish_time").toLocalDateTime());
                    a.setPublisherName(rs.getString("publisher_name"));
                    results.add(a);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }


    /**
     * 更新公告信息
     */
    public boolean updateAnnouncement(Announcement announcement) {
        String sql = "UPDATE announcement SET title = ?, content = ?, publisher_name = ? WHERE id = ?";
        try (Connection conn = DAO.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, announcement.getTitle());
            pstmt.setString(2, announcement.getContent());
            pstmt.setString(3, announcement.getPublisherName());
            pstmt.setInt(4, announcement.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
