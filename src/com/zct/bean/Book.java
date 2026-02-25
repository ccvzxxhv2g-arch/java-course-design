package com.zct.bean;

import java.sql.Date;

public class Book {
    private int id;
    private String book_Name;
    private String isbn;
    private String author;
    private String description;
    private String category;
    private double price;
    private Date publishDate;
    private String publisher;
    private int stock_quantity;
    private String coverImagePath;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getBook_Name() { return book_Name; }
    public void setBook_Name(String book_Name) { this.book_Name = book_Name; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public Date getPublishDate() { return publishDate; }
    public void setPublishDate(Date publishDate) { this.publishDate = publishDate; }
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public int getstock_quantity() { return stock_quantity; }
    public void setstock_quantity(int stock_quantity) { this.stock_quantity = stock_quantity; }
    public String getCoverImagePath() {
        return coverImagePath; }
    public void setCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath; }
}