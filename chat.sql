-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 16, 2022 at 12:44 PM
-- Server version: 10.4.22-MariaDB
-- PHP Version: 8.1.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `chat`
--
DROP IF EXISTS `chat`;
CREATE DATABASE `chat`;
USE `chat`;
-- --------------------------------------------------------

--
-- Table structure for table `chats`
--

CREATE TABLE `chats` (
  `chat_id` varchar(50) NOT NULL,
  `chat_name` varchar(50) NOT NULL,
  `isprivate` tinyint(1) DEFAULT 0,
  `createdAt` date NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `chats`
--

INSERT INTO `chats` (`chat_id`, `chat_name`, `isprivate`, `createdAt`) VALUES
('~g~16449790394580.5237940592237313', 'Pipi', 0, '2022-02-16'),
('~g~16449790835200.5852782296881223', 'What page', 0, '2022-02-16'),
('~g~16449798210750.30664269651839104', 'happy now?', 0, '2022-02-16'),
('~g~16449799224310.1436191912994833', 'sd', 0, '2022-02-16'),
('~g~16449805136320.5162582510397301', 'mimimi', 0, '2022-02-16'),
('~g~16449807364310.40890725859395005', 'Reset?', 0, '2022-02-16'),
('~g~16449809431120.24681564130214795', 'ds', 0, '2022-02-16'),
('~g~16449829927880.3124994793556486', 'morEEEE', 0, '2022-02-16'),
('~g~16449970690550.6707724876814241', 'Work motherfucker', 0, '2022-02-16'),
('~g~16450003528320.8504750376358581', 'Shit', 0, '2022-02-16'),
('~g~16450018741560.32043893298135484', 'nene', 0, '2022-02-16'),
('~g~16450022482810.5073675315442879', 'new', 0, '2022-02-16'),
('~g~16450024037390.5736777327516094', 'pollas en vinagre', 0, '2022-02-16'),
('~g~16450027767590.700096696566999', 'nene', 0, '2022-02-16'),
('~g~16450040911660.07277480189385122', 'pollas', 0, '2022-02-16'),
('~g~16450044155940.9037104628571765', 'coños', 0, '2022-02-16'),
('~g~16450045398790.37231298546810576', 'cartero', 0, '2022-02-16'),
('~g~16450045610360.08494017895794648', 'medico', 0, '2022-02-16'),
('~g~16450047505360.2635928688189667', 'cosas chungas', 0, '2022-02-16');

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE `messages` (
  `msg_id` int(11) NOT NULL,
  `chat_id` varchar(50) NOT NULL,
  `message` varchar(500) DEFAULT NULL,
  `timestamp` date NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `participants`
--

CREATE TABLE `participants` (
  `user_id` varchar(50) NOT NULL,
  `chat_id` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `participants`
--

INSERT INTO `participants` (`user_id`, `chat_id`) VALUES
('w', '~g~16450040911660.07277480189385122'),
('s', '~g~16450040911660.07277480189385122'),
('s', '~g~16450044155940.9037104628571765'),
('w', '~g~16450044155940.9037104628571765'),
('s', '~g~16450045398790.37231298546810576'),
('w', '~g~16450045398790.37231298546810576'),
('w', '~g~16450045610360.08494017895794648'),
('s', '~g~16450047505360.2635928688189667'),
('w', '~g~16450047505360.2635928688189667');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(300) NOT NULL,
  `createdAt` date NOT NULL DEFAULT current_timestamp(),
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
  `surname` varchar(50) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
  `last_ip` varchar(15) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
  `genre` varchar(15) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
  `image` varchar(50) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `password`, `createdAt`, `email`, `name`, `surname`, `last_ip`, `genre`, `image`) VALUES
(6, 's', 's', '2022-02-16', 's', 's', 's', '192.168.1.57', 'Masculino', 'null'),
(7, 'w', 'w', '2022-02-16', 'w', 'w', 'w', '192.168.1.57', 'Masculino', 'null');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `chats`
--
ALTER TABLE `chats`
  ADD PRIMARY KEY (`chat_id`),
  ADD UNIQUE KEY `chat_id` (`chat_id`);

--
-- Indexes for table `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`msg_id`),
  ADD KEY `user_id` (`chat_id`),
  ADD KEY `fk_msg_chat` (`chat_id`);

--
-- Indexes for table `participants`
--
ALTER TABLE `participants`
  ADD KEY `chat_key` (`chat_id`) USING BTREE,
  ADD KEY `user_key` (`user_id`) USING BTREE;

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username_2` (`username`),
  ADD KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `messages`
--
ALTER TABLE `messages`
  MODIFY `msg_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `fk_msg_chat` FOREIGN KEY (`chat_id`) REFERENCES `chats` (`chat_id`);

--
-- Constraints for table `participants`
--
ALTER TABLE `participants`
  ADD CONSTRAINT `fk_chat_key` FOREIGN KEY (`chat_id`) REFERENCES `chats` (`chat_id`),
  ADD CONSTRAINT `fk_user_key` FOREIGN KEY (`user_id`) REFERENCES `users` (`username`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
