DROP TABLE IF EXISTS `msg_record`;

CREATE TABLE `msg_record` (
  `uuid` varchar(50) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `repertory_tb`;

CREATE TABLE `repertory_tb` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item_type` varchar(80) NOT NULL,
  `num` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;