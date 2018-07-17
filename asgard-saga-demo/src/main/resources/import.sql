DROP TABLE IF EXISTS `asgard_user`;

CREATE TABLE `asgard_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(11) NOT NULL DEFAULT '',
  `password` varchar(11) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
