DROP TABLE IF EXISTS `mybatis_role`;
DROP TABLE IF EXISTS `mybatis_user`;
DROP TABLE IF EXISTS `mybatis_user_role`;

CREATE TABLE `mybatis_role` (
  `id`    INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name`  VARCHAR(255)     NOT NULL DEFAULT '',
  `level` VARCHAR(255)     NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `mybatis_user` (
  `id`       INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255)     NOT NULL DEFAULT '',
  `password` VARCHAR(255)     NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `mybatis_user_role` (
  `id`      INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` INT(11)          NOT NULL,
  `role_id` INT(11)          NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_user_role` (`user_id`, `role_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;