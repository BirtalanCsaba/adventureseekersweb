DROP DATABASE IF EXISTS `web-adventureseekers-romania`;

CREATE DATABASE IF NOT EXISTS `web-adventureseekers-romania`;
USE `web-adventureseekers-romania`;

--
-- Create the `user_detail` table
--
DROP TABLE IF EXISTS `user_detail`;

CREATE TABLE `user_detail` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
    `description` varchar(200) DEFAULT NULL,
    `country` varchar(60) DEFAULT NULL,
    `county` varchar(60) DEFAULT NULL,
    `city` varchar(60) DEFAULT NULL,
    `profile_image` mediumblob DEFAULT NULL,
    
    primary key (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Create the `user` table
--

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
	`id` varchar(36) NOT NULL,
    `username` varchar(50) NOT NULL,
    `password` char(80) NOT NULL,
    `email` varchar(50) NOT NULL,
    `first_name` varchar(50) NOT NULL,
    `last_name` varchar(50) NOT NULL,
    `birth_date` DATE NOT NULL,
    `enabled` boolean DEFAULT false,
    `user_detail_id` int(11) DEFAULT NULL,
    
    primary key (`id`),
    
    KEY `FK_DETAIL_idx` (`user_detail_id`),
	CONSTRAINT `FK_DETAIL` 
    FOREIGN KEY (`user_detail_id`) 
    REFERENCES `user_detail` (`id`),
    
    unique (`username`),
    unique (`email`)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;

SET @user_id := UUID();
INSERT INTO user (`id`, `username`, `password`, `email`, `first_name`, `last_name`, `birth_date`, `enabled`)
VALUES (@user_id, 'csababirtalan', '$2a$12$D1Mqul7Fwc.06RrhAMo/Pek5iM8dDLL1Zb//RBITfuemigEJM4ip.', 'csababirtalan144@gmail.com', 'Csaba', 'Birtalan', DATE('2001-12-14'), true);

--
-- Create the `role` table
--

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `roles`
--

INSERT INTO `role` (name)
VALUES 
('ROLE_ADMIN'),('ROLE_HELPER'),('ROLE_MANAGER'), ('ROLE_STANDARD');

--
-- Create the `users_roles` join table
--

DROP TABLE IF EXISTS `users_roles`;

CREATE TABLE `users_roles` (
	`user_id` varchar(36) NOT NULL,
    `role_id` int(11) NOT NULL,
    
    PRIMARY KEY (`user_id`,`role_id`),
    
    KEY `FK_ROLE_idx` (`role_id`),
    
    CONSTRAINT `FK_USER_05` FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`)
    ON DELETE CASCADE,
    
    CONSTRAINT `FK_ROLE` FOREIGN KEY (`role_id`)
    REFERENCES `role` (`id`)
    ON DELETE NO ACTION ON UPDATE NO ACTION
)ENGINE=InnoDB DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;

--
-- Dumping data for table `users_roles`
--
INSERT INTO `users_roles` (user_id,role_id)
VALUES (@user_id, 4);

DROP TABLE IF EXISTS `confirmation_token`;
CREATE TABLE `confirmation_token` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
    `confirmed_at` DATETIME,
    `created_at` DATETIME NOT NULL,
    `expired_at` DATETIME NOT NULL,
    `token` varchar(36) NOT NULL,
    
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `pending_email` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`user_id` varchar(36) NOT NULL,
    `confirmation_token_id` int(11) NOT NULL,
    `email` varchar(50) NOT NULL,
    
    PRIMARY KEY (`id`),
    
    UNIQUE(`user_id`, `confirmation_token_id`),
    
    KEY `FK_CONFIRMATION_TOKEN_idx` (`confirmation_token_id`),
    
    CONSTRAINT `FK_USER_10` FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`)
    ON DELETE CASCADE,
    
    CONSTRAINT `FK_CONFIRMATION_TOKEN` FOREIGN KEY (`confirmation_token_id`)
    REFERENCES `confirmation_token` (`id`)
    ON DELETE NO ACTION ON UPDATE NO ACTION
)ENGINE=InnoDB DEFAULT CHARSET=latin1;














