-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 11, 2022 at 09:49 AM
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

-- --------------------------------------------------------

--
-- Table structure for table `chats`
--

CREATE TABLE `chats` (
  `chat_id` int(11) NOT NULL,
  `chat_name` varchar(50) NOT NULL,
  `isprivate` tinyint(1) DEFAULT 0,
  `createdAt` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE `messages` (
  `msg_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `chat_id` int(11) NOT NULL,
  `message` varchar(500) NOT NULL,
  `timestamp` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `participants`
--

CREATE TABLE `participants` (
  `user_id` int(11) NOT NULL,
  `chat_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
(1, 'Paco', 'paco.psw1', '0000-00-00', 'chocolate', 'Paco', 'Chocolate', '', '', ''),
(2, 'Paco', 'paco.psw1', '0000-00-00', 'chocolate', 'Paco', 'Chocolate', 'n', 'n', 'n'),
(3, 'q', 'q', '0000-00-00', 'q', 'q', 'q', '', 'q', 'null'),
(4, 'Sufrimiento', 'noooo@dolor.dead', '2022-02-10', 'Muerte', 'Bastarda', 'f', '', 'Otros', 'null'),
(5, 'PreparedStm', 's', '2022-02-10', 's', 's', 's', '', 'Femenino', 'null');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `chats`
--
ALTER TABLE `chats`
  ADD PRIMARY KEY (`chat_id`);

--
-- Indexes for table `messages`
--
ALTER TABLE `messages`
  ADD KEY `user_id` (`user_id`,`chat_id`),
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
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `chats`
--
ALTER TABLE `chats`
  MODIFY `chat_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `fk_msg_chat` FOREIGN KEY (`chat_id`) REFERENCES `chats` (`chat_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_msg_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON UPDATE CASCADE;

--
-- Constraints for table `participants`
--
ALTER TABLE `participants`
  ADD CONSTRAINT `fk_chat_key` FOREIGN KEY (`chat_id`) REFERENCES `chats` (`chat_id`),
  ADD CONSTRAINT `fk_user_key` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
