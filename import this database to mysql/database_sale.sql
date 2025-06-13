CREATE DATABASE  IF NOT EXISTS `websale` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `websale`;
-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: websale
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart_item`
--

DROP TABLE IF EXISTS `cart_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cart_id` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  `quantity` int DEFAULT '1',
  `price_at_add` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `cart_id` (`cart_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `cart_item_ibfk_1` FOREIGN KEY (`cart_id`) REFERENCES `cart` (`id`) ON DELETE CASCADE,
  CONSTRAINT `cart_item_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_item`
--

LOCK TABLES `cart_item` WRITE;
/*!40000 ALTER TABLE `cart_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `cart_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `thumbnail` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'Laptop','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ-BsOPDrRzqWhEGz_PXaeLjV25bUiGFyxL0g&s'),(2,'PC Gaming','https://pcmarket.vn/media/product/12002_pc_5090.jpg'),(3,'Linh kiện máy tính','https://product.hstatic.net/200000860097/product/1024__4__f656c63b34d042ceb797184cef3f9628.png'),(4,'Màn hình','https://cdn.tgdd.vn//News/1499650//man-hinh-may-tinh-5-800x450-1.jpg'),(5,'Bàn phím','https://www.kiiboom.com/cdn/shop/files/1_3e0cca45-914f-4d30-aeb6-0e81c5546cbb.png?v=1718680995&width=1946'),(6,'Chuột',NULL),(7,'Tai nghe',NULL),(8,'Loa',NULL),(9,'Thiết bị mạng',NULL),(10,'Phụ kiện khác',NULL);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback`
--

DROP TABLE IF EXISTS `feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedback` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int DEFAULT NULL,
  `rating` int DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `feedback_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback`
--

LOCK TABLES `feedback` WRITE;
/*!40000 ALTER TABLE `feedback` DISABLE KEYS */;
/*!40000 ALTER TABLE `feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gallery`
--

DROP TABLE IF EXISTS `gallery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gallery` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int DEFAULT NULL,
  `thumbnail` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `gallery_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gallery`
--

LOCK TABLES `gallery` WRITE;
/*!40000 ALTER TABLE `gallery` DISABLE KEYS */;
/*!40000 ALTER TABLE `gallery` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory`
--

DROP TABLE IF EXISTS `inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int DEFAULT NULL,
  `store_id` int DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  KEY `store_id` (`store_id`),
  CONSTRAINT `inventory_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `inventory_ibfk_2` FOREIGN KEY (`store_id`) REFERENCES `store` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory`
--

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
INSERT INTO `inventory` VALUES (1,1,1,20),(2,1,2,15),(3,1,3,10),(4,2,1,5),(5,2,2,8),(6,3,3,20),(7,4,1,1),(8,5,2,10),(9,6,3,15),(10,7,1,22),(11,8,2,21),(12,9,3,9),(13,10,1,13),(14,11,1,6),(15,11,2,1),(16,11,3,5),(17,2,3,0),(18,5,1,0),(19,5,3,0),(20,12,1,2),(21,12,2,2),(22,12,3,3),(23,13,1,1),(24,13,2,0),(25,13,3,0),(26,14,1,1),(27,14,2,1),(28,14,3,1),(29,7,2,0),(30,7,3,5),(31,6,1,4),(32,6,2,2),(33,15,1,1),(34,15,2,1),(35,15,3,1),(36,16,1,1),(37,16,2,1),(38,16,3,0);
/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `product_id` int NOT NULL,
  `quantity` int NOT NULL,
  `price_at_order` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_order_items_order` (`order_id`),
  KEY `fk_order_items_product` (`product_id`),
  CONSTRAINT `fk_order_items_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_order_items_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `order_date` datetime NOT NULL,
  `total_amount` double NOT NULL,
  `status` varchar(50) NOT NULL DEFAULT 'Pending',
  `shipping_address` text NOT NULL,
  `shipping_city` varchar(100) NOT NULL,
  `shipping_district` varchar(100) NOT NULL,
  `shipping_phone` varchar(20) NOT NULL,
  `receiver_name` varchar(255) NOT NULL,
  `payment_method` varchar(50) NOT NULL,
  `payment_status` varchar(50) NOT NULL DEFAULT 'Unpaid',
  `notes` text,
  PRIMARY KEY (`id`),
  KEY `fk_orders_user` (`user_id`),
  CONSTRAINT `fk_orders_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `category_id` int DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `price` int DEFAULT NULL,
  `discount` int DEFAULT NULL,
  `thumbnail` tinytext,
  `description` tinytext,
  `deleted` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_product_category` (`category_id`),
  CONSTRAINT `fk_product_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,1,'Laptop Dell Inspiron 15',18000000,10,'https://ttcenter.com.vn/uploads/product/vbpz7rhu-989-acer-nitro-5-an515-57-core-i5-11400h-8gb-512gb-rtx-1650-15-fhd-ips-144hz.jpg','Laptop Dell bền bỉ, hiệu năng ổn định cho học tập và văn phòng.',0,NULL,'2025-06-08 05:46:38'),(2,2,'PC Gaming Ryzen 5 RTX 4060',25000000,50,'https://bizweb.dktcdn.net/thumb/large/100/487/158/products/1-5172ed2c-0623-4ccf-ba7b-7d9061ed5ead.jpg?v=1729440786813','PC chơi game mạnh mẽ, CPU Ryzen 5, VGA RTX 4060, RAM 16GB.',0,NULL,'2025-06-08 06:30:44'),(3,3,'RAM DDR4 16GB Bus 3200MHz',1200000,5,'ram_ddr4_16gb.jpg','RAM DDR4 chính hãng, tốc độ cao phù hợp cho cả game và làm việc.',0,NULL,NULL),(4,4,'Màn hình LG UltraWide 29 inch',5200000,7,'monitor_lg_ultrawide_29.jpg','Màn hình LG tỉ lệ 21:9, phù hợp đa nhiệm và giải trí.',0,NULL,NULL),(5,5,'Bàn phím cơ AKKO 3068B',1500000,10,'https://akkogear.com.vn/wp-content/uploads/2021/11/ban-phim-co-akko-3068b-multi-modes-black-pink-01.jpg','Bàn phím cơ không dây, thiết kế nhỏ gọn, nhiều switch tùy chọn.',0,NULL,'2025-06-08 06:56:27'),(6,6,'Chuột Logitech G304',800000,8,'logitech_g304.jpg','Chuột gaming không dây, cảm biến HERO chính xác.',0,NULL,NULL),(7,7,'Tai nghe Razer Kraken X',1300000,12,'razer_kraken_x.jpg','Tai nghe gaming 7.1, nhẹ và đeo thoải mái nhiều giờ.',0,NULL,NULL),(8,8,'Loa Soundmax A2122',950000,5,'soundmax_a2122.jpg','Loa 2.1 công suất lớn, âm thanh mạnh mẽ phù hợp giải trí.',0,NULL,NULL),(9,9,'Router WiFi TP-Link Archer AX55',1900000,10,'tplink_archer_ax55.jpg','Router WiFi 6 tốc độ cao, phù hợp cho nhà nhiều thiết bị.',0,NULL,NULL),(10,10,'Giá đỡ màn hình NB F80',600000,0,'gia_do_nb_f80.jpg','Giá đỡ màn hình tiện lợi, dễ dàng điều chỉnh độ cao và góc nghiêng.',0,NULL,NULL),(11,2,'Acer Nitro 5 sieu ngu',230000000,10,'/assets/img/vbpz7rhu-989-acer-nitro-5-an515-57-core-i5-11400h-8gb-512gb-rtx-1650-15-fhd-ips-144hz.jpg','Laptop acer nitro 5 giảm giá',0,'2025-06-08 05:05:47','2025-06-08 05:31:17'),(12,2,'blue archive',100000,10,'https://www.tnc.com.vn/uploads/product/sp2024/ext/laptop-hp-15-fd1043tu-core-5-9z2w9pa-134462.jpg','luu tru xanh',0,'2025-06-12 16:22:39','2025-06-12 16:22:39'),(13,2,'hehe',100000,10,'','',0,'2025-06-12 20:22:00','2025-06-12 20:22:00'),(14,2,'test',100,NULL,'','',0,'2025-06-12 20:23:30','2025-06-12 20:23:30'),(15,10,'blabla',2000000,20,'','sdfadf',0,'2025-06-12 20:59:35','2025-06-12 21:06:34'),(16,1,'Laptop acer',10000000,20,'https://ktmt.vnmediacdn.com/images/2022/03/16/5-1647399987-hinh-anh-bao-ve-moi-truong-kinhtemoitruong-3.jpg','laptop bao ve moi truong',0,'2025-06-13 07:16:31','2025-06-13 07:17:32');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(40) NOT NULL,
  `description` varchar(150) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ROLE_CUSTOMER','Người sử dụng dịch vụ hoặc mua hàng'),(2,'ROLE_CASHIER','Phụ trách thanh toán và hóa đơn'),(3,'ROLE_WAREHOUSE_MANAGER','Quản lý hàng tồn kho và nhập xuất'),(4,'ROLE_HR_MANAGER','Quản lý thông tin và công việc nhân viên'),(5,'ROLE_TECHNICAL_STAFF','Xử lý kỹ thuật và bảo trì thiết bị'),(6,'ROLE_SHIPPER','Giao hàng đến khách'),(7,'ROLE_SALES_STAFF','Tư vấn và bán sản phẩm cho khách'),(8,'ROLE_CUSTOMER_SERVICE','Nhân viên chăm sóc khách hàng (Hỗ trợ, giải quyết khiếu nại)'),(9,'ROLE_ADMIN','Quản trị hệ thống và phân quyền'),(10,'ROLE_DIRECTOR','Quản lý toàn bộ hoạt động kinh doanh'),(11,'ROLE_USER','Vai trò mặc định cho người dùng hệ thống');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store`
--

DROP TABLE IF EXISTS `store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `store` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(56) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store`
--

LOCK TABLES `store` WRITE;
/*!40000 ALTER TABLE `store` DISABLE KEYS */;
INSERT INTO `store` VALUES (1,'Tech Store Hà Nội','Số 1 Đại Cồ Việt, Hai Bà Trưng, Hà Nội'),(2,'Tech Store Đà Nẵng','42 Bạch Đằng, Hải Châu, Đà Nẵng'),(3,'Tech Store Hồ Chí Minh','180 Lý Chính Thắng, Quận 3, TP. HCM');
/*!40000 ALTER TABLE `store` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tech_task`
--

DROP TABLE IF EXISTS `tech_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tech_task` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `task_type` text,
  `status` text,
  `note` text,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tech_task_user` (`user_id`),
  CONSTRAINT `fk_tech_task_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tech_task`
--

LOCK TABLES `tech_task` WRITE;
/*!40000 ALTER TABLE `tech_task` DISABLE KEYS */;
/*!40000 ALTER TABLE `tech_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(64) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `deleted` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Nguyen Van A','a@shop.com','e10adc3949ba59abbe56e057f20f883e','0901000001','Hà Nội',0,'2025-06-08 09:46:51','2025-06-08 09:46:51',_binary '\0'),(2,'Tran Thi B','b@shop.com','e10adc3949ba59abbe56e057f20f883e','0901000002','HCM',0,'2025-06-08 09:46:51','2025-06-08 09:46:51',_binary '\0'),(3,'Le Van C','c@shop.com','e10adc3949ba59abbe56e057f20f883e','0901000003','Đà Nẵng',0,'2025-06-08 09:46:51','2025-06-08 09:46:51',_binary '\0'),(4,'Pham Thi D','d@shop.com','e10adc3949ba59abbe56e057f20f883e','0901000004','Cần Thơ',0,'2025-06-08 09:46:51','2025-06-08 09:46:51',_binary '\0'),(5,'Hoang Van E','e@shop.com','e10adc3949ba59abbe56e057f20f883e','0901000005','Hải Phòng',0,'2025-06-08 09:46:51','2025-06-08 09:46:51',_binary '\0'),(6,'Vo Thi F','f@shop.com','e10adc3949ba59abbe56e057f20f883e','0901000006','Nghệ An',0,'2025-06-08 09:46:51','2025-06-08 09:46:51',_binary '\0'),(7,'Nguyen Van G','g@shop.com','e10adc3949ba59abbe56e057f20f883e','0901000007','Quảng Ninh',0,'2025-06-08 09:46:51','2025-06-08 09:46:51',_binary '\0'),(8,'Tran Van H','h@shop.com','e10adc3949ba59abbe56e057f20f883e','0901000008','Huế',0,'2025-06-08 09:46:51','2025-06-08 09:46:51',_binary '\0'),(9,'Bui Thi I','i@shop.com','e10adc3949ba59abbe56e057f20f883e','0901000009','Lào Cai',0,'2025-06-08 09:46:51','2025-06-08 09:46:51',_binary '\0'),(10,'Dang Van J','j@shop.com','e10adc3949ba59abbe56e057f20f883e','0901000010','Nam Định',0,'2025-06-08 09:46:51','2025-06-08 09:46:51',_binary '\0'),(11,'tuannguyen','tuan.na225772@sis.hust.edu.vn','tuantech2004','0987962495',NULL,0,'2025-06-12 18:45:01','2025-06-12 18:45:01',_binary ''),(12,'tuan.na225772@sis.hust.edu.vn','tuan.na22523772@sis.hust.edu.vn','tuantech2004','324234234234',NULL,0,'2025-06-12 20:15:53','2025-06-12 20:15:53',_binary ''),(14,'truong thi dieu linh','linh.ntd@sis.hust.edu.vn','123456','0123456789',NULL,0,'2025-06-13 07:14:13','2025-06-13 07:14:13',_binary '');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_roles`
--

DROP TABLE IF EXISTS `users_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `role_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_users_roles_user` (`user_id`),
  KEY `fk_users_roles_role` (`role_id`),
  CONSTRAINT `fk_users_roles_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `fk_users_roles_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_roles`
--

LOCK TABLES `users_roles` WRITE;
/*!40000 ALTER TABLE `users_roles` DISABLE KEYS */;
INSERT INTO `users_roles` VALUES (1,1,1),(2,2,2),(3,3,3),(4,4,4),(5,5,5),(6,6,6),(7,7,7),(8,8,8),(9,9,9),(10,10,10),(11,11,9),(12,12,11),(13,14,11);
/*!40000 ALTER TABLE `users_roles` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-13 21:22:30
