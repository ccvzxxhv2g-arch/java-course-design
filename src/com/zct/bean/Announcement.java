package com.zct.bean;

import java.time.LocalDateTime;

public class Announcement {

    private int id;             // 公告ID
    private String title;       // 标题
    private String content;     // 内容
    private LocalDateTime publishTime; // 发布时间
    private String publisherName;      // 发布者姓名

    // Getter 和 Setter 方法

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }
}
