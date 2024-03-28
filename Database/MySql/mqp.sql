-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Mar 28, 2024 at 10:56 AM
-- Server version: 8.0.21
-- PHP Version: 7.4.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mqp_db_`
--

-- --------------------------------------------------------

--
-- Table structure for table `files_tbl`
--

DROP TABLE IF EXISTS `files_tbl`;
CREATE TABLE IF NOT EXISTS `files_tbl` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(1024) CHARACTER SET utf8 COLLATE utf8_persian_ci NOT NULL,
  `type` varchar(24) CHARACTER SET utf8 COLLATE utf8_persian_ci NOT NULL,
  `hash` varchar(512) CHARACTER SET utf8 COLLATE utf8_persian_ci NOT NULL,
  `address` varchar(1024) CHARACTER SET utf8 COLLATE utf8_persian_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `mail_files_tbl`
--

DROP TABLE IF EXISTS `mail_files_tbl`;
CREATE TABLE IF NOT EXISTS `mail_files_tbl` (
  `id` int NOT NULL AUTO_INCREMENT,
  `file_id` int NOT NULL,
  `mail_id` int NOT NULL,
  `status` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `file_id` (`file_id`),
  KEY `mail_id` (`mail_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `mail_tbl`
--

DROP TABLE IF EXISTS `mail_tbl`;
CREATE TABLE IF NOT EXISTS `mail_tbl` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(1024) CHARACTER SET utf8 COLLATE utf8_persian_ci NOT NULL,
  `content` mediumtext CHARACTER SET utf8 COLLATE utf8_persian_ci NOT NULL,
  `users_id` int NOT NULL,
  `datetime` varchar(24) CHARACTER SET utf8 COLLATE utf8_persian_ci NOT NULL,
  `delete_flag` int NOT NULL,
  `from_user` varchar(512) CHARACTER SET utf8 COLLATE utf8_persian_ci NOT NULL,
  `seen` int NOT NULL,
  `From_Ip` varchar(255) NOT NULL,
  `to_user` varchar(1024) NOT NULL,
  `send_mail_id` int NOT NULL,
  `send` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `users_id` (`users_id`)
) ENGINE=MyISAM AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users_tbl`
--

DROP TABLE IF EXISTS `users_tbl`;
CREATE TABLE IF NOT EXISTS `users_tbl` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_persian_ci NOT NULL,
  `family` varchar(128) CHARACTER SET utf8 COLLATE utf8_persian_ci NOT NULL,
  `username` varchar(512) CHARACTER SET utf8 COLLATE utf8_persian_ci NOT NULL,
  `password` varchar(512) CHARACTER SET utf8 COLLATE utf8_persian_ci NOT NULL,
  `last_edit_date` varchar(24) CHARACTER SET utf32 COLLATE utf32_persian_ci NOT NULL,
  `login_token` varchar(512) CHARACTER SET utf8 COLLATE utf8_persian_ci NOT NULL,
  `fp_token` varchar(512) CHARACTER SET utf8 COLLATE utf8_persian_ci NOT NULL,
  `email` varchar(128) CHARACTER SET utf8 COLLATE utf8_persian_ci NOT NULL,
  `phone` varchar(24) CHARACTER SET utf8 COLLATE utf8_persian_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
