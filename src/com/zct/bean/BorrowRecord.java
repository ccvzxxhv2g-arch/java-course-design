package com.zct.bean;

import java.sql.Date;

public class BorrowRecord {
    private int id;             // 借阅记录ID，主键
    private String ISBN;        // 图书ISBN编号
    private String name;        // 借书人的用户名
    private Date create_time;   // 借阅时间（创建记录时间）
    private Date update_time;   // 更新时间（实际归还时间）
    private Date end_time;      // 应归还时间
    private int ret;            // 归还状态：0-已归还，1-未归还

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getISBN() { return ISBN; }
    public void setISBN(String ISBN) { this.ISBN = ISBN; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Date getCreate_time() { return create_time; }
    public void setCreate_time(Date create_time) { this.create_time = create_time; }

    public Date getUpdate_time() { return update_time; }
    public void setUpdate_time(Date update_time) { this.update_time = update_time; }

    public Date getEnd_time() { return end_time; }
    public void setEnd_time(Date end_time) { this.end_time = end_time; }

    public int getRet() { return ret; }
    public void setRet(int ret) { this.ret = ret; }
}
