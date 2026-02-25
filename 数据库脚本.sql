-- 创建数据库
CREATE DATABASE IF NOT EXISTS `libraryman_sys` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `libraryman_sys`;

-- 1. 公告表 (announcement)
CREATE TABLE IF NOT EXISTS `announcement` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL COMMENT '公告标题',
  `content` TEXT NOT NULL COMMENT '公告内容',
  `publish_time` DATETIME NOT NULL COMMENT '发布时间',
  `publisher_name` VARCHAR(100) NOT NULL COMMENT '发布者姓名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统公告表';

-- 2. 管理员/工作人员表 (attendant_message)
CREATE TABLE IF NOT EXISTS `attendant_message` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码',
  `avatar_url` VARCHAR(255) DEFAULT NULL COMMENT '头像路径',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `address` VARCHAR(255) DEFAULT NULL COMMENT '地址',
  `borrow_limit` INT(11) DEFAULT 10 COMMENT '借阅上限',
  `register_date` DATETIME DEFAULT NULL COMMENT '注册时间',
  `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员/工作人员信息表';

-- 3. 图书表 (books)
CREATE TABLE IF NOT EXISTS `books` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `book_name` VARCHAR(255) NOT NULL COMMENT '书名',
  `isbn` VARCHAR(50) NOT NULL COMMENT 'ISBN编号',
  `author` VARCHAR(100) NOT NULL COMMENT '作者',
  `description` TEXT COMMENT '图书描述',
  `category` VARCHAR(100) DEFAULT NULL COMMENT '分类',
  `price` DECIMAL(10,2) NOT NULL COMMENT '价格',
  `publish_date` DATE DEFAULT NULL COMMENT '出版日期',
  `publisher` VARCHAR(255) DEFAULT NULL COMMENT '出版社',
  `stock_quantity` INT(11) NOT NULL DEFAULT 0 COMMENT '库存数量',
  `cover_image` VARCHAR(255) DEFAULT NULL COMMENT '封面图片路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图书信息表';

-- 4. 图书借阅记录表 (book_borrow_records)
CREATE TABLE IF NOT EXISTS `book_borrow_records` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `ISBN` VARCHAR(50) NOT NULL COMMENT '图书ISBN',
  `name` VARCHAR(100) NOT NULL COMMENT '借阅人姓名',
  `create_time` DATE NOT NULL COMMENT '借阅创建时间',
  `update_time` DATE DEFAULT NULL COMMENT '更新时间',
  `end_time` DATE NOT NULL COMMENT '应还时间',
  `ret` TINYINT(1) DEFAULT 0 COMMENT '是否归还(0未还/1已还)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图书借阅记录表';

-- 5. 用户信息表 (user_message)
CREATE TABLE IF NOT EXISTS `user_message` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码',
  `avatar_url` VARCHAR(255) DEFAULT NULL COMMENT '头像路径',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `address` VARCHAR(255) DEFAULT NULL COMMENT '地址',
  `borrow_limit` INT(11) DEFAULT 10 COMMENT '借阅上限',
  `register_date` DATETIME DEFAULT NULL COMMENT '注册时间',
  `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';