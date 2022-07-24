DROP TABLE IF EXISTS `t_file`;

CREATE TABLE `t_file` (
  `id` varchar(255) NOT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `street1` varchar(255) DEFAULT NULL,
  `street2` varchar(255) DEFAULT NULL,
  `zipcode` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `security_code` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

INSERT INTO `t_file` VALUES ('D001','London','United Kingdom','','1 baker street','','99999','john.smith@doctors.com','John','Smith','0123456789','45'),('D002','Paris','France','','15 rue de Vaugirard','','75015','jean.dupont@docteurs.fr','Jean','Dupont','9999999999','999'),('P001','Paris','France','','1 rue de la Paix','','75001','jean.martin@free.fr','Jean','Martin','0101010101','0000'),('P002','Paris','France','','11 rue de la Convention','','75015','paul.dubois@orange.fr','Paul','Dubois','5555555555','1111');

DROP TABLE IF EXISTS `t_doctor`;

CREATE TABLE `t_doctor` (
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKaw58as6iygkn722ejq138u3w3` FOREIGN KEY (`id`) REFERENCES `t_file` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

INSERT INTO `t_doctor` VALUES ('D001'),('D002');

DROP TABLE IF EXISTS `t_specialty`;

CREATE TABLE `t_specialty` (
  `id` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

INSERT INTO `t_specialty` VALUES ('S001','Specialty 1'),('S002','Specialty 2'),('S003','Specialty 3');

DROP TABLE IF EXISTS `t_doctor_specialty`;

CREATE TABLE `t_doctor_specialty` (
  `doctor_id` varchar(255) NOT NULL,
  `specialty_id` varchar(255) NOT NULL,
  KEY `FKkc7j7tjo7bivshwf6auwy76us` (`specialty_id`),
  KEY `FKk83mvqsahth60iw6b0ud9ndi6` (`doctor_id`),
  CONSTRAINT `FKk83mvqsahth60iw6b0ud9ndi6` FOREIGN KEY (`doctor_id`) REFERENCES `t_doctor` (`id`),
  CONSTRAINT `FKkc7j7tjo7bivshwf6auwy76us` FOREIGN KEY (`specialty_id`) REFERENCES `t_specialty` (`id`)
) ENGINE=InnoDB;

INSERT INTO `t_doctor_specialty` VALUES ('D001','S001'),('D001','S002'),('D001','S003'),('D002','S002');

DROP TABLE IF EXISTS `t_patient_file`;

CREATE TABLE `t_patient_file` (
  `id` varchar(255) NOT NULL,
  `date_of_birth` DATE NOT NULL,
  `referring_doctor_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK45nd8ypvq2j86bql7l78w89r7` (`referring_doctor_id`),
  CONSTRAINT `FK45nd8ypvq2j86bql7l78w89r7` FOREIGN KEY (`referring_doctor_id`) REFERENCES `t_doctor` (`id`),
  CONSTRAINT `FKh8lgs6yjrmu6jy40gmhx5n06w` FOREIGN KEY (`id`) REFERENCES `t_file` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

INSERT INTO `t_patient_file` VALUES ('P001','1995-03-13', 'D001'),('P002','1968-08-11', 'D002');

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `id` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) NOT NULL,
  `security_code` varchar(255) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_jhib4legehrm4yscx9t3lirqi` (`username`)
) ENGINE=InnoDB;

INSERT INTO `t_user` VALUES ('A001','$2a$12$yjbMztwlMm0yEIVbM8ybu.nJ6kQPipI6ViV/8GbU8TWgIwSvRbAQa','ROLE_ADMIN','','admin'),('D001','$2a$12$7/n1myGRPalYXRCUbsrXz.vhtJCYTfi8j1dBlX1m/ECFosXD6jcMa','ROLE_DOCTOR','','user'),('D002','$2a$12$rbh2MVQ3zMmeE4a.vHQcJOlBQ7uLLE9fpcy3G.l.vfT96WhlF/51m','ROLE_DOCTOR','','doc'),('P001','$2a$12$nGajrCywpBYm9xLtu.lAbuTTNCFX3rrHaisz87P.fw2BXCF8E/gD2','ROLE_PATIENT','','jean'),('P002','$2a$12$tClFvDjq0BaRSxi8/iird.BdMGV99a88Bun39z1yc29A9Qg0u40bm','ROLE_PATIENT','','utilisateur');
