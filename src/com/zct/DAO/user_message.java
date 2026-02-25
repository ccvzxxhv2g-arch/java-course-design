package com.zct.DAO;

import com.zct.bean.USER;
import com.zct.tool.SaveUserStateTool;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class user_message {

    // 用户登录
    public static boolean userLogin(USER user) {
        Connection conn = null;
        try {
            String username = user.getName();
            String pwd = user.getPwd();

            // 输入验证
            if(username == null || username.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "用户名不能为空", "登录错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            conn = DAO.getConn();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT password FROM user_message WHERE name=?"
            );
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String dbPassword = rs.getString(1);
                if (dbPassword.equals(pwd)) {
                    SaveUserStateTool.setUser(user);
                    updateLastLoginTime(username);
                    JOptionPane.showMessageDialog(null,
                            "欢迎回来，" + username + "!",
                            "登录成功",
                            JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null,
                            "密码不正确",
                            "登录失败",
                            JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "用户名不存在",
                        "登录失败",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "数据库连接异常:\n" + ex.getMessage(),
                    "系统错误",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return false;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "系统发生异常:\n" + ex.getMessage(),
                    "系统错误",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.err.println("数据库连接关闭异常: " + ex.getMessage());
            }
        }
    }

    public static void updateLastLoginTime(String username) {
        String sql = "UPDATE user_message SET last_login_time = now() WHERE name = ?";
        try (Connection conn = DAO.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 根据用户名获取用户对象
    public static USER getuserByName(String name) {
        Connection conn = null;
        try {
            conn = DAO.getConn();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM user_message WHERE name = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                USER user = new USER();
                user.setName(rs.getString("name"));
                user.setPwd(rs.getString("password"));
                user.setAvatarUrl(rs.getString("avatar_url"));

                // ? 补全字段
                user.setPhone(rs.getString("phone"));
                user.setEmail(rs.getString("email"));
                user.setAddress(rs.getString("address"));
                user.setBorrowLimit(rs.getInt("borrow_limit"));
                user.setRegisterDate(rs.getString("register_date"));
                user.setLastLoginTime(rs.getString("last_login_time"));

                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException ignored) {}
        }
        return null;
    }


    public static boolean insertUser(USER user) {
        Connection conn = null;
        try {
            String name = user.getName();
            String pwd = user.getPwd();
            String okPwd = user.getOkPwd();
            String avatarUrl = user.getAvatarUrl();

            String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()\\-_=+\\[\\]{}|;:'\",.<>/?]).*$";

            if (name == null || name.trim().isEmpty() ||
                    pwd == null || pwd.trim().isEmpty() ||
                    okPwd == null || okPwd.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "用户名或密码不能为空");
                return false;
            }

            if (!pwd.equals(okPwd)) {
                JOptionPane.showMessageDialog(null, "两次输入的密码不一致");
                return false;
            }

            if (pwd.length() < 8) {
                JOptionPane.showMessageDialog(null, "密码长度不能小于8个字符");
                return false;
            }

            if (!pwd.matches(passwordRegex)) {
                JOptionPane.showMessageDialog(null, "必须包含数字、小写、大写和特殊字符");
                return false;
            }

            conn = DAO.getConn();
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO user_message (name, password, avatar_url) VALUES (?, ?, ?)"
            );
            ps.setString(1, name);
            ps.setString(2, pwd);
            ps.setString(3, avatarUrl);
            int flag = ps.executeUpdate();

            if (flag > 0) {
                JOptionPane.showMessageDialog(null, "注册成功");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "注册失败");
                return false;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            String errorMessage = ex.getMessage();

            if (errorMessage.contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(null, "用户名已存在，请更换用户名");
            } else {
                JOptionPane.showMessageDialog(null, "系统异常：" + errorMessage);
            }
            return false;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException ignored) {}
        }
    }

    /**
     * 使用 USER 对象插入新用户到数据库
     */
    /**
     * 使用 USER 对象插入新用户到数据库
     */
    public static boolean insertUser2(USER user) {
        Connection conn = null;
        try {
            String name = user.getName();
            String pwd = user.getPwd();
            String avatarUrl = user.getAvatarUrl();
            String phone = user.getPhone();
            String email = user.getEmail();
            String address = user.getAddress();

            String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()\\-_=+\\[\\]{}|;:'\",.<>/?]).*$";

            // 1. 基本非空检查
            if (name == null || name.trim().isEmpty() ||
                    pwd == null) {
                JOptionPane.showMessageDialog(null, "用户名或密码不能为空");
                return false;
            }

            // 3. 密码长度检查
            if (pwd.length() < 8) {
                JOptionPane.showMessageDialog(null, "密码长度不能小于8个字符");
                return false;
            }

            // 4. 密码复杂度检查
            if (!pwd.matches(passwordRegex)) {
                JOptionPane.showMessageDialog(null, "密码必须包含数字、小写字母、大写字母和特殊字符");
                return false;
            }

            // 5. 用户名唯一性检查
            if (isUserNameExists(name)) {
                JOptionPane.showMessageDialog(null, "用户名已存在，请更换用户名");
                return false;
            }

            conn = DAO.getConn();
            String sql = "INSERT INTO user_message (name, password, avatar_url, phone, email, address) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            // 设置参数
            ps.setString(1, name);
            ps.setString(2, pwd);
            ps.setString(3, avatarUrl);
            ps.setString(4, phone);
            ps.setString(5, email);
            ps.setString(6, address);

            // 执行插入
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "用户注册成功");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "用户注册失败");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "数据库异常：" + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException ignored) {}
        }
    }


    // 查询所有用户
    public static List<USER> listUsers() {
        List<USER> users = new ArrayList<>();
        String sql = "SELECT name, password, avatar_url, phone, email, address, borrow_limit, register_date, last_login_time FROM user_message";
        try (Connection conn = DAO.getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                USER user = new USER();
                user.setName(rs.getString("name"));
                user.setPwd(rs.getString("password"));
                user.setAvatarUrl(rs.getString("avatar_url"));
                user.setPhone(rs.getString("phone"));
                user.setEmail(rs.getString("email"));
                user.setAddress(rs.getString("address"));
                user.setBorrowLimit(rs.getInt("borrow_limit"));
                user.setRegisterDate(rs.getString("register_date"));
                user.setLastLoginTime(rs.getString("last_login_time"));

                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    // 模糊查询用户（按字段）
    public static List<USER> queryUsers(String field, String value) {
        List<USER> users = new ArrayList<>();
        // 修改 SQL 查询语句，选取所有需要的列
        String sql = "SELECT name, password, avatar_url, phone, email, address, borrow_limit, register_date, last_login_time FROM user_message WHERE " + field + " LIKE ?";
        try (Connection conn = DAO.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + value + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                USER user = new USER();
                user.setName(rs.getString("name"));
                user.setPwd(rs.getString("password"));
                user.setAvatarUrl(rs.getString("avatar_url"));
                user.setPhone(rs.getString("phone"));
                user.setEmail(rs.getString("email"));
                user.setAddress(rs.getString("address"));
                user.setBorrowLimit(rs.getInt("borrow_limit"));
                user.setRegisterDate(rs.getString("register_date"));
                user.setLastLoginTime(rs.getString("last_login_time"));

                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }


    public static boolean updateUser(String originalName, String newName,
                                     String password, String avatarUrl,
                                     String phone, String email, String address) {
        Connection conn = null;
        try {
            // 1. 基本非空校验
            if (newName == null || newName.trim().isEmpty()) {
                throw new IllegalArgumentException("用户名不能为空");
            }

            // 2. 用户名唯一性校验（如果修改了用户名）
            if (!originalName.equals(newName) && isUserNameExists(newName)) {
                throw new IllegalArgumentException("该用户名已存在");
            }

            // 3. 密码校验（如果提供了新密码）
            if (password != null && !password.isEmpty()) {
                String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()\\-_=+\\[\\]{}|;:'\",.<>/?]).*$";

                // 3.1 密码长度检查
                if (password.length() < 8) {
                    throw new IllegalArgumentException("密码长度不能小于8个字符");
                }

                // 3.2 密码复杂度检查
                if (!password.matches(passwordRegex)) {
                    throw new IllegalArgumentException("密码必须包含数字、小写字母、大写字母和特殊字符");
                }
            }

            // 4. 邮箱格式校验
            if (email != null && !email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new IllegalArgumentException("邮箱格式不正确");
            }

            // 5. 手机号格式校验
            if (phone != null && !phone.isEmpty() && !phone.matches("^1[3-9]\\d{9}$")) {
                throw new IllegalArgumentException("请输入有效的手机号码");
            }

            // 6. 地址长度校验
            if (address != null && address.length() > 200) {
                throw new IllegalArgumentException("地址长度不能超过200个字符");
            }

            conn = DAO.getConn();
            String sql = "UPDATE user_message SET name=?, password=?, avatar_url=?, phone=?, email=?, address=? WHERE name=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, newName);
            ps.setString(2, password);
            ps.setString(3, avatarUrl);
            ps.setString(4, phone);
            ps.setString(5, email);
            ps.setString(6, address);
            ps.setString(7, originalName);

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                // 更新成功后更新全局状态
                USER updatedUser = getuserByName(newName);
                if (updatedUser != null) {
                    SaveUserStateTool.setUser(updatedUser);
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("数据库操作异常: " + e.getMessage(), e);
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("数据库连接关闭异常: " + e.getMessage());
            }
        }
    }

    // 在 user_message.java 中修改 updateUser 方法
    public static boolean updateUser2(String originalName, String newName,
                                      String password, String avatarUrl,
                                      String phone, String email, String address) {
        Connection conn = DAO.getConn();
        try {
            // 1. 基本非空检查
            if (newName == null || newName.trim().isEmpty()) {
                throw new IllegalArgumentException("用户名不能为空");
            }

            // 2. 检查用户名是否已存在（如果修改了用户名）
            if (!originalName.equals(newName) && isUserNameExists(newName)) {
                throw new IllegalArgumentException("该用户名已存在");
            }

            // 3. 密码验证（如果提供了新密码）
            if (password != null && !password.isEmpty()) {
                String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()\\-_=+\\[\\]{}|;:'\",.<>/?]).*$";

                // 3.1 密码长度检查
                if (password.length() < 8) {
                    throw new IllegalArgumentException("密码长度不能小于8个字符");
                }

                // 3.2 密码复杂度检查
                if (!password.matches(passwordRegex)) {
                    throw new IllegalArgumentException("密码必须包含数字、小写字母、大写字母和特殊字符");
                }
            }

            // 4. 邮箱格式检查
            if (email != null && !email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new IllegalArgumentException("邮箱格式不正确");
            }

            // 5. 电话号码检查
            if (phone != null && !phone.isEmpty() && !phone.matches("^1[3-9]\\d{9}$")) {
                throw new IllegalArgumentException("请输入有效的手机号码");
            }

            // 构建 SQL，密码为空时不更新密码字段
            String sql;
            if (password == null || password.isEmpty()) {
                sql = "UPDATE user_message SET name=?, avatar_url=?, phone=?, email=?, address=? WHERE name=?";
            } else {
                sql = "UPDATE user_message SET name=?, password=?, avatar_url=?, phone=?, email=?, address=? WHERE name=?";
            }

            PreparedStatement pstmt = conn.prepareStatement(sql);
            int paramIndex = 1;
            pstmt.setString(paramIndex++, newName);

            if (password != null && !password.isEmpty()) {
                pstmt.setString(paramIndex++, password);
            }

            pstmt.setString(paramIndex++, avatarUrl);
            pstmt.setString(paramIndex++, phone);
            pstmt.setString(paramIndex++, email);
            pstmt.setString(paramIndex++, address);
            pstmt.setString(paramIndex, originalName);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                // 更新成功后更新全局状态
                USER updatedUser = getuserByName(newName);
                if (updatedUser != null) {
                    SaveUserStateTool.setUser(updatedUser);
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("数据库操作异常: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("数据库连接关闭异常: " + e.getMessage());
            }
        }
    }

    private static boolean isUserNameExists(String name) {
        String sql = "SELECT COUNT(*) FROM user_message WHERE name = ?";
        try (Connection conn = DAO.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除用户（按用户名）
    public static boolean deleteUser(String name) {
        Connection conn = null;
        try {
            conn = DAO.getConn();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM user_message WHERE name = ?");
            ps.setString(1, name);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException ignored) {}
        }
    }
}
