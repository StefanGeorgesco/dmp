DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `id` VARCHAR(255) NOT NULL,
  `username` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `role` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_username_user` (`username`)
) ENGINE=InnoDB;

INSERT INTO `t_user` VALUES ('A001','admin','$2a$12$yjbMztwlMm0yEIVbM8ybu.nJ6kQPipI6ViV/8GbU8TWgIwSvRbAQa','ROLE_ADMIN');
INSERT INTO `t_user` VALUES ('D001','user','$2a$12$7/n1myGRPalYXRCUbsrXz.vhtJCYTfi8j1dBlX1m/ECFosXD6jcMa','ROLE_DOCTOR');
INSERT INTO `t_user` VALUES ('D002','doc','$2a$12$rbh2MVQ3zMmeE4a.vHQcJOlBQ7uLLE9fpcy3G.l.vfT96WhlF/51m','ROLE_DOCTOR');
INSERT INTO `t_user` VALUES ('P001','jean','$2a$12$nGajrCywpBYm9xLtu.lAbuTTNCFX3rrHaisz87P.fw2BXCF8E/gD2','ROLE_PATIENT');
