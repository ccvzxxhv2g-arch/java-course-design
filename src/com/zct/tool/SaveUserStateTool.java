package com.zct.tool;

import com.zct.bean.ADMIN;
import com.zct.bean.USER;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户状态保存工具类
 * 用于登录后保存当前用户信息（管理员或普通用户）
 */
public class SaveUserStateTool {

    // 当前登录的管理员对象
    private static ADMIN admin;
    // 当前登录的普通用户对象
    private static USER user;
    // 当前用户名
    private static String name;

    // 新增：管理员变更监听器列表
    private static final List<AdminChangeListener> adminListeners = new ArrayList<>();
    // 新增：用户变更监听器列表
    private static final List<UserChangeListener> userListeners = new ArrayList<>();

    // 新增：管理员变更监听接口
    public interface AdminChangeListener {
        void onAdminChanged(ADMIN newAdmin);
    }

    // 新增：用户变更监听接口
    public interface UserChangeListener {
        void onUserChanged(USER newUser);
    }

    /**
     * 添加管理员变更监听器
     */
    public static void addAdminChangeListener(AdminChangeListener listener) {
        if (!adminListeners.contains(listener)) {
            adminListeners.add(listener);
        }
    }

    /**
     * 添加用户变更监听器
     */
    public static void addUserChangeListener(UserChangeListener listener) {
        if (!userListeners.contains(listener)) {
            userListeners.add(listener);
        }
    }
    /**
     * 设置当前登录的管理员并通知监听器
     */
    public static void setAdmin(ADMIN admin) {
        SaveUserStateTool.admin = admin;
        if (admin != null && admin.getName() != null) {
            SaveUserStateTool.name = admin.getName();
        } else {
            SaveUserStateTool.name = null;
        }
        notifyAdminChanged(admin); // 新增：触发管理员变更通知
    }

    /**
     * 设置当前登录的普通用户并通知监听器
     */
    public static void setUser(USER user) {
        SaveUserStateTool.user = user;
        if (user != null && user.getName() != null) {
            SaveUserStateTool.name = user.getName();
        } else {
            SaveUserStateTool.name = null;
        }
        notifyUserChanged(user); // 新增：触发用户变更通知
    }

    /**
     * 通知所有管理员监听器
     */
    private static void notifyAdminChanged(ADMIN newAdmin) {
        for (AdminChangeListener listener : adminListeners) {
            listener.onAdminChanged(newAdmin);
        }
    }

    /**
     * 通知所有用户监听器
     */
    private static void notifyUserChanged(USER newUser) {
        for (UserChangeListener listener : userListeners) {
            listener.onUserChanged(newUser);
        }
    }

    // 其他原有方法保持不变...
    public static USER getUser() {
        return user;
    }

    public static ADMIN getAdmin() {
        return admin;
    }

    public static String getUsername() {
        return name;
    }

    public static boolean isAdmin() {
        return admin != null;
    }

    public static boolean isUser() {
        return user != null;
    }

    /**
     * 清除当前用户状态（用于退出登录）
     */
    public static void clearUserState() {
        admin = null;
        user = null;
        name = null;
        // 退出时不需要通知监听器
    }
}
