DROP TABLE IF EXISTS `order_tb`;
CREATE TABLE `order_tb` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(60) NOT NULL,
  `name` varchar(60) NOT NULL,
   PRIMARY KEY (`id`)
);