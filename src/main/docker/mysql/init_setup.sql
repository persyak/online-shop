CREATE DATABASE items;
USE items;

CREATE TABLE `item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `price` double NOT NULL,
  `creationDate` datetime NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `items`.`item` (`name`, `price`, `creationDate`, `description`)
VALUES ("vacuum-cleaner", 300, '2020-11-29 01:04:00', "automated vaccum-cleaner Samsung");
INSERT INTO `items`.`item` (`name`, `price`, `creationDate`, `description`)
VALUES ("iphone 6", 400, '2016-10-11 01:04:00', "metallic color");
INSERT INTO `items`.`item` (`name`, `price`, `creationDate`, `description`)
VALUES ("macbook", 400, '2021-09-11 01:04:00', "silver macbook air");
INSERT INTO `items`.`item` (`name`, `price`, `creationDate`, `description`)
VALUES ("inverter", 1600, '2020-10-01 01:04:00', "huawei inverter");

CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `login` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `firstName` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `lastName` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO `items`.`users`(`login`,`password`,`firstName`,`lastName`)
VALUES("torlov", "torlov","Taras","Orlov");
INSERT INTO `items`.`users`(`login`,`password`,`firstName`,`lastName`)
VALUES("atrubin", "atrubin","Tolik","Trubin");
INSERT INTO `items`.`users`(`login`,`password`,`firstName`,`lastName`)
VALUES("oohorodnik", "oohorodnik","Oleksandr","Ohorodnik");