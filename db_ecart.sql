-- MySQL dump 10.13  Distrib 8.0.44, for Linux (x86_64)
--
-- Host: localhost    Database: db_cart
-- ------------------------------------------------------
-- Server version	8.0.44-0ubuntu0.24.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `m_order_items`
--

DROP TABLE IF EXISTS `m_order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `m_order_items` (
  `id` int NOT NULL AUTO_INCREMENT,
  `image_url` varchar(255) DEFAULT NULL,
  `insert_time` datetime(6) DEFAULT NULL,
  `item_name` varchar(255) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `unit_price` double DEFAULT NULL,
  `order_id` int DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKthfpxyomxthlxky8n2yiw79uj` (`order_id`),
  KEY `FK2snah4mna1o1wdqd2l6f3aike` (`product_id`),
  CONSTRAINT `FK2snah4mna1o1wdqd2l6f3aike` FOREIGN KEY (`product_id`) REFERENCES `m_product` (`id`),
  CONSTRAINT `FKthfpxyomxthlxky8n2yiw79uj` FOREIGN KEY (`order_id`) REFERENCES `m_orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `m_orders`
--

DROP TABLE IF EXISTS `m_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `m_orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `insert_time` datetime(6) DEFAULT NULL,
  `delivery_date` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `order_status` varchar(255) DEFAULT NULL,
  `order_tracking_no` varchar(255) DEFAULT NULL,
  `rezorpay_order_id` varchar(255) DEFAULT NULL,
  `rezorpay_payment_id` varchar(255) DEFAULT NULL,
  `total_price` double DEFAULT NULL,
  `total_quantity` int DEFAULT NULL,
  `address_id` int DEFAULT NULL,
  `customer_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKearm7mj08wdpiokarp204m1ft` (`address_id`),
  KEY `FKqe8huh7uh3wvtyfaanwfwub5m` (`customer_id`),
  KEY `FK2uth1oerxxy3v63rs0bewqd5j` (`user_id`),
  CONSTRAINT `FK2uth1oerxxy3v63rs0bewqd5j` FOREIGN KEY (`user_id`) REFERENCES `m_users` (`id`),
  CONSTRAINT `FKearm7mj08wdpiokarp204m1ft` FOREIGN KEY (`address_id`) REFERENCES `m_user_address` (`id`),
  CONSTRAINT `FKqe8huh7uh3wvtyfaanwfwub5m` FOREIGN KEY (`customer_id`) REFERENCES `m_users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `m_parameters`
--

DROP TABLE IF EXISTS `m_parameters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `m_parameters` (
  `id` int NOT NULL AUTO_INCREMENT,
  `insert_time` datetime(6) DEFAULT NULL,
  `insert_by` int DEFAULT NULL,
  `param_key` varchar(255) DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `update_by` int DEFAULT NULL,
  `param_val` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `m_product`
--

DROP TABLE IF EXISTS `m_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `m_product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `insert_time` datetime(6) DEFAULT NULL,
  `product_name` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `unit_price` double DEFAULT NULL,
  `unit_stock` int DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `m_product_notification`
--

DROP TABLE IF EXISTS `m_product_notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `m_product_notification` (
  `id` int NOT NULL AUTO_INCREMENT,
  `insert_time` datetime(6) DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  `status` varchar(55) DEFAULT 'pending',
  `update_time` datetime(6) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `m_system_error`
--

DROP TABLE IF EXISTS `m_system_error`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `m_system_error` (
  `id` int NOT NULL AUTO_INCREMENT,
  `error_code` varchar(255) DEFAULT NULL,
  `error_description` varchar(255) DEFAULT '',
  `error_status` varchar(255) DEFAULT NULL,
  `insert_time` datetime(6) DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `m_temp_cart_items`
--

DROP TABLE IF EXISTS `m_temp_cart_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `m_temp_cart_items` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  `product_image` varchar(255) DEFAULT NULL,
  `product_name` varchar(255) DEFAULT NULL,
  `product_title` varchar(255) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `unit_price` double DEFAULT NULL,
  `unit_stock` int DEFAULT NULL,
  `user-id` int DEFAULT NULL,
  `insert_time` datetime(6) DEFAULT NULL,
  `status` varchar(30) DEFAULT 'pending',
  `update_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `m_user_address`
--

DROP TABLE IF EXISTS `m_user_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `m_user_address` (
  `id` int NOT NULL AUTO_INCREMENT,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `house_number` varchar(255) DEFAULT NULL,
  `insert_time` datetime(6) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `zip_code` varchar(255) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `is_default` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FKj3h3pvlf4ic24cmjln1xmebu7` (`user_id`),
  CONSTRAINT `FKj3h3pvlf4ic24cmjln1xmebu7` FOREIGN KEY (`user_id`) REFERENCES `m_users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `m_users`
--

DROP TABLE IF EXISTS `m_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `m_users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `enable` int DEFAULT '0',
  `insert_time` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `account_non_expired` int DEFAULT '0',
  `account_non_lock` int DEFAULT '0',
  `credential_non_expired` int DEFAULT '0',
  `remarks` varchar(255) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_forget_password`
--

DROP TABLE IF EXISTS `t_forget_password`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_forget_password` (
  `id` int NOT NULL AUTO_INCREMENT,
  `insert_time` datetime(6) DEFAULT NULL,
  `status` varchar(30) DEFAULT 'pending',
  `token` varchar(255) DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_razorpay_orders`
--

DROP TABLE IF EXISTS `t_razorpay_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_razorpay_orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `amount` int DEFAULT NULL,
  `amount_due` int DEFAULT NULL,
  `amount_paid` int DEFAULT NULL,
  `attempts` int DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `currency` varchar(255) DEFAULT NULL,
  `entity` varchar(255) DEFAULT NULL,
  `order_id` varchar(255) DEFAULT NULL,
  `receipt` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_send_email_details`
--

DROP TABLE IF EXISTS `t_send_email_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_send_email_details` (
  `id` int NOT NULL AUTO_INCREMENT,
  `attachment` mediumblob,
  `bcc_emails` text,
  `body` text,
  `cc_emails` text,
  `filename` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `status` varchar(30) DEFAULT 'pending',
  `subject` varchar(255) DEFAULT NULL,
  `to_emails` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-24 19:24:42
