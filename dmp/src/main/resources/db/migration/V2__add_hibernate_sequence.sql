DROP TABLE IF EXISTS `hibernate_sequence`;

CREATE TABLE `hibernate_sequence` (
	next_val bigint
) ENGINE=InnoDB;

INSERT INTO `hibernate_sequence` values (1);
