DROP TABLE IF EXISTS `order_tb`;
CREATE TABLE `order_tb` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(60) NOT NULL,
  `name` varchar(60) NOT NULL,
   PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `event_producer_record`;

CREATE TABLE `event_producer_record` (
  `uuid` varchar(50) NOT NULL,
  `type` varchar(50) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
