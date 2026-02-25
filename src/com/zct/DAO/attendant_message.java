package com.zct.DAO;

import com.zct.bean.ADMIN;
import com.zct.tool.SaveUserStateTool;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class attendant_message {

    public static boolean adminLogin(ADMIN admin) {
        Connection conn = null;
        try {
            String username = admin.getName();
            String pwd = admin.getPwd();
            conn = DAO.getConn();
            PreparedStatement ps = conn.prepareStatement("SELECT password FROM attendant_message WHERE name=?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next() && rs.getRow() > 0) {
                String password = rs.getString(1);
                if (password.equals(pwd)) {

                    // 更新最后登录时间
                    try (PreparedStatement updatePs = DAO.getConn().prepareStatement("UPDATE attendant_message SET last_login_time = NOW() WHERE name = ?")) {
                        updatePs.setString(1, username);
                        updatePs.executeUpdate();
                    }

                    SaveUserStateTool.setAdmin(admin);
                    JOptionPane.showMessageDialog(null, username + " 成功登录系统");
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "密码输入错误");
                    return false;
                }
            } else {
                JOptionPane.showMessageDialog(null, "用户名不存在");
                return false;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "数据库异常：\n" + ex.getMessage());
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static ADMIN getAdminByName(String name) {
    Connection conn = null;
    try {
        conn = DAO.getConn();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM attendant_message WHERE name = ?");
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            ADMIN admin = new ADMIN();
            admin.setName(rs.getString("name"));
            admin.setPwd(rs.getString("password"));
            admin.setAvatarUrl(rs.getString("avatar_url"));

            // ? 补充字段
            admin.setPhone(rs.getString("phone"));
            admin.setEmail(rs.getString("email"));
            admin.setAddress(rs.getString("address"));
            admin.setBorrowLimit(rs.getInt("borrow_limit"));

            // ? 新增字段
            admin.setRegisterDate(rs.getString("register_date"));
            admin.setLastLoginTime(rs.getString("last_login_time"));

            return admin;
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (conn != null) conn.close();
        } catch (Exception ignored) {}
    }
    return null;
}



    public static void insertUser(ADMIN admin) {
    Connection conn = null;
    try {
        String adminname = admin.getName();
        String pwd = admin.getPwd();
        String okPwd = admin.getOkPwd();
        String avatarUrl = admin.getAvatarUrl();
        String password1 = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()\\-_=+\\[\\]{}|;:'\",.<>/?]).*$";

        if (adminname == null || adminname.trim().equals("") ||
                pwd == null || pwd.trim().equals("") ||
                okPwd == null || okPwd.trim().equals("")) {
            JOptionPane.showMessageDialog(null, "用户名或密码不能为空");
            return;
        }
        if (!pwd.trim().equals(okPwd.trim())) {
            JOptionPane.showMessageDialog(null, "两次输入的密码不一致");
            return;
        }
        if (pwd.length() < 8) {
            JOptionPane.showMessageDialog(null, "密码不能少于8个字符");
            return;
        }
        if (!pwd.matches(password1)) {
            JOptionPane.showMessageDialog(null, "密码需包含数字、大小写字母及特殊字符");
            return;
        }

        conn = DAO.getConn();
        String sql = "INSERT INTO attendant_message (name, password, avatar_url, phone, email, address, borrow_limit) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, adminname.trim());
        ps.setString(2, pwd.trim());
        ps.setString(3, avatarUrl);
        ps.setString(4, admin.getPhone());
        ps.setString(5, admin.getEmail());
        ps.setString(6, admin.getAddress());
        ps.setInt(7, admin.getBorrowLimit());

        int flag = ps.executeUpdate();
        if (flag > 0) {
            JOptionPane.showMessageDialog(null, "注册成功");
        } else {
            JOptionPane.showMessageDialog(null, "注册失败");
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        String errorMessage = ex.getMessage();
        if (errorMessage.contains("Duplicate entry")) {
            JOptionPane.showMessageDialog(null, "用户名已存在，请更换用户名");
        } else {
            JOptionPane.showMessageDialog(null, "系统异常：" + errorMessage);
        }
    } finally {
        try {
            if (conn != null) conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

    /**
     * 更新管理员信息（支持修改用户名）
     * @param admin 新的管理员信息
     * @param originalName 原始用户名（用于查找记录）
     * @return 是否更新成功
     * @throws IllegalArgumentException 如果输入验证失败
     */
    public static boolean updateUserInfo(ADMIN admin, String originalName) throws IllegalArgumentException {
        // 验证密码格式
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};:'\"\\\\|,.<>\\/?]).{8,}$";
        if (!admin.getPwd().matches(passwordPattern)) {
            throw new IllegalArgumentException("密码必须至少8位，包含大小写字母、数字和特殊字符");
        }

        // 验证电话号码格式
        String phonePattern = "^1\\d{10}$";
        if (!admin.getPhone().matches(phonePattern)) {
            throw new IllegalArgumentException("电话号码格式不正确");
        }

        // 验证邮箱格式
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!admin.getEmail().matches(emailPattern)) {
            throw new IllegalArgumentException("邮箱格式不正确");
        }

        // 验证借阅数量
        String borrowPattern = "^[1-9]\\d*$";
        if (!String.valueOf(admin.getBorrowLimit()).matches(borrowPattern)) {
            throw new IllegalArgumentException("可借阅数量必须为正整数");
        }

        // 检查用户名是否已存在（如果修改了用户名）
        if (!admin.getName().equals(originalName)) {
            if (isAdminNameExists(admin.getName())) {
                throw new IllegalArgumentException("该用户名已存在");
            }
        }

        // SQL更新语句，包含用户名更新
        String sql = "UPDATE attendant_message SET name=?, password=?, avatar_url=?, phone=?, email=?, address=?, borrow_limit=?, last_login_time=NOW() WHERE name=?";
        try (Connection conn = DAO.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 设置新值
            pstmt.setString(1, admin.getName());
            pstmt.setString(2, admin.getPwd());
            pstmt.setString(3, admin.getAvatarUrl());
            pstmt.setString(4, admin.getPhone());
            pstmt.setString(5, admin.getEmail());
            pstmt.setString(6, admin.getAddress());
            pstmt.setInt(7, admin.getBorrowLimit());
            // WHERE条件使用原始用户名
            pstmt.setString(8, originalName);

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 检查管理员用户名是否已存在
     */
    private static boolean isAdminNameExists(String name) {
        String sql = "SELECT COUNT(*) FROM attendant_message WHERE name = ?";
        try (Connection conn = DAO.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
