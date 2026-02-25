package com.zct.bean;

public class USER {

    private String name;        // 用户名
    private String pwd;         // 密码
    private String okPwd;       // 确认密码
    private String avatarUrl;   // 头像URL
    private String phone;       // 手机号
    private String email;       // 邮箱
    private String address;     // 地址
    private int borrowLimit;    // 借阅上限
    private String registerDate;     // 对应 register_date
    private String lastLoginTime;    // 对应 last_login_time

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getOkPwd() {
        return okPwd;
    }

    public void setOkPwd(String okPwd) {
        this.okPwd = okPwd;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getBorrowLimit() {
        return borrowLimit;
    }

    public void setBorrowLimit(int borrowLimit) {
        this.borrowLimit = borrowLimit;
    }
}
