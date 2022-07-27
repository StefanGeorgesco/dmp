DROP TABLE IF EXISTS `t_file`;

CREATE TABLE `t_file` (
  `id` varchar(255) NOT NULL,
  `firstname` varchar(255) NOT NULL,
  `lastname` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `street1` varchar(255) NOT NULL,
  `street2` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `state` varchar(255) NOT NULL,
  `zipcode` varchar(255) NOT NULL,
  `country` varchar(255) NOT NULL,
  `security_code` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

INSERT INTO `t_file` VALUES ('D001', 'John', 'Smith', '0123456789', 'john.smith@doctors.com', '1 baker street', '', 'London', '', '99999', 'United Kingdom', '45');
INSERT INTO `t_file` VALUES ('P001', 'Eric', 'Martin', '0101010101', 'eric.martin@free.fr', '1 rue de la Paix', '', 'Paris', '', '75001', 'France', '0000');
INSERT INTO `t_file` VALUES ('D002', 'Jean', 'Dupont', '9999999999', 'jean.dupont@docteurs.fr', '15 rue de Vaugirard', '', 'Paris', '', '75015', 'France', '999');
INSERT INTO `t_file` VALUES ('D004', 'Leah', 'Little', '061-074-7258', 'leah.little@example.com', '2727 The Avenue', 'street 2', 'Nenagh', '', '47362', 'Ireland', '000');
INSERT INTO `t_file` VALUES ('D005', 'Vivienne', 'Roy', '077 793 50 38', 'vivienne.roy@example.com', '1661 Rue du Village', 'street 2', 'Lichtensteig', '', '2126', 'Switzerland', '000');
INSERT INTO `t_file` VALUES ('D006', 'Vukašin', 'Adamović', '016-3028-125', 'vukasin.adamovic@example.com', '6351 Bresjačka', 'street 2', 'Senta', '', '95114', 'Serbia', '000');
INSERT INTO `t_file` VALUES ('D007', 'Eugenio', 'Olvera', '(615) 831 6362', 'eugenio.olvera@example.com', '3234 Circunvalación Bhután', 'street 2', 'Comalcalco', '', '47519', 'Mexico', '000');
INSERT INTO `t_file` VALUES ('D008', 'Darko', 'RatkovićRistić', '025-7372-402', 'darko.ratkovicristic@example.com', '1607 Dragiše Kašikovića', 'street 2', 'Boljevac', '', '14541', 'Serbia', '000');
INSERT INTO `t_file` VALUES ('D009', 'Peppi', 'Seppanen', '09-720-887', 'peppi.seppanen@example.com', '7237 Hämeenkatu', 'street 2', 'Lempäälä', '', '34397', 'Finland', '000');
INSERT INTO `t_file` VALUES ('D010', 'Rachel', 'Richards', '013873 97383', 'rachel.richards@example.com', '6294 St. John’S Road', 'street 2', 'Cambridge', '', 'WJ1 8LF', 'United Kingdom', '000');
INSERT INTO `t_file` VALUES ('D011', 'Bill', 'Carlson', '071-524-4134', 'bill.carlson@example.com', '2581 Mill Lane', 'street 2', 'Trim', '', '15049', 'Ireland', '000');
INSERT INTO `t_file` VALUES ('D012', 'Melquisedeque', 'Nascimento', '(21) 5214-4747', 'melquisedeque.nascimento@example.com', '2155 Rua Santa Luzia ', 'street 2', 'Olinda', '', '51415', 'Brazil', '000');
INSERT INTO `t_file` VALUES ('D013', 'Luke', 'Hansen', '061-855-6303', 'luke.hansen@example.com', '1201 Grange Road', 'street 2', 'Gorey', '', '71364', 'Ireland', '000');
INSERT INTO `t_file` VALUES ('P004', 'Roelf', 'Verwijmeren', '(089) 8518423', 'roelf.verwijmeren@example.com', '7481 Keerderstraat', 'street 2', 'Spijk Gld', '', '4951 XC', 'Netherlands', '000');
INSERT INTO `t_file` VALUES ('P005', 'Martha', 'Welch', '00-1044-2420', 'martha.welch@example.com', '2186 Blossom Hill Rd', 'street 2', 'Dubbo', '', '1178', 'Australia', '000');
INSERT INTO `t_file` VALUES ('P006', 'Lyubomila', 'Kalnickiy', '(098) A93-0296', 'lyubomila.kalnickiy@example.com', '1406 Mikoli Gastello', 'street 2', 'Irpin', '', '45431', 'Ukraine', '000');
INSERT INTO `t_file` VALUES ('P007', 'Phoebe', 'Clarke', '(183)-791-1522', 'phoebe.clarke@example.com', '5100 Weymouth Road', 'street 2', 'Wellington', '', '54665', 'New Zealand', '000');
INSERT INTO `t_file` VALUES ('P008', 'Sami', 'Mjønes', '32959974', 'sami.mjones@example.com', '7467 Midtoddveien', 'street 2', 'Vear', '', '9365', 'Norway', '000');
INSERT INTO `t_file` VALUES ('P009', 'Alvaro', 'Canales', '(666) 146 7433', 'alvaro.canales@example.com', '1413 Circunvalación Tamaulipas', 'street 2', 'Morelos Cañada', '', '85598', 'Mexico', '000');
INSERT INTO `t_file` VALUES ('P010', 'Susan', 'Edwards', '031-909-2200', 'susan.edwards@example.com', '4577 Patrick Street', 'street 2', 'Wexford', '', '11051', 'Ireland', '000');
INSERT INTO `t_file` VALUES ('P011', 'Marie-Louise', 'Leroux', '077 177 95 66', 'marie-louise.leroux@example.com', '2556 Rue Dumenge', 'street 2', 'Savigny', '', '8618', 'Switzerland', '000');
INSERT INTO `t_file` VALUES ('P012', 'Julio', 'Bernard', '077 202 55 32', 'julio.bernard@example.com', '1492 Place des 44 Enfants D Izieu', 'street 2', 'Kernenried', '', '8314', 'Switzerland', '000');
INSERT INTO `t_file` VALUES ('P013', 'Magnus', 'Jensen', '19776685', 'magnus.jensen@example.com', '165 Bakkevænget', 'street 2', 'Stenderup', '', '19960', 'Denmark', '000');

DROP TABLE IF EXISTS `t_doctor`;

CREATE TABLE `t_doctor` (
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_file_doctor` FOREIGN KEY (`id`) REFERENCES `t_file` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

INSERT INTO `t_doctor` VALUES ('D001');
INSERT INTO `t_doctor` VALUES ('D002');
INSERT INTO `t_doctor` VALUES ('D004');
INSERT INTO `t_doctor` VALUES ('D005');
INSERT INTO `t_doctor` VALUES ('D006');
INSERT INTO `t_doctor` VALUES ('D007');
INSERT INTO `t_doctor` VALUES ('D008');
INSERT INTO `t_doctor` VALUES ('D009');
INSERT INTO `t_doctor` VALUES ('D010');
INSERT INTO `t_doctor` VALUES ('D011');
INSERT INTO `t_doctor` VALUES ('D012');
INSERT INTO `t_doctor` VALUES ('D013');

DROP TABLE IF EXISTS `t_specialty`;

CREATE TABLE `t_specialty` (
  `id` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

INSERT INTO `t_specialty` VALUES ('S001', 'Specialty 1');
INSERT INTO `t_specialty` VALUES ('S002', 'Specialty 2');

DROP TABLE IF EXISTS `t_doctor_specialty`;

CREATE TABLE `t_doctor_specialty` (
  `doctor_id` varchar(255) NOT NULL,
  `specialty_id` varchar(255) NOT NULL,
  KEY `FK_specialty_id` (`specialty_id`),
  KEY `FK_doctor_id` (`doctor_id`),
  CONSTRAINT `FK_doctor_doctor_specialty` FOREIGN KEY (`doctor_id`) REFERENCES `t_doctor` (`id`),
  CONSTRAINT `FK_specialty_doctor_specialty` FOREIGN KEY (`specialty_id`) REFERENCES `t_specialty` (`id`)
) ENGINE=InnoDB;

INSERT INTO `t_doctor_specialty` VALUES ('D001', 'S001');
INSERT INTO `t_doctor_specialty` VALUES ('D001', 'S002');
INSERT INTO `t_doctor_specialty` VALUES ('D002', 'S001');
INSERT INTO `t_doctor_specialty` VALUES ('D002', 'S002');
INSERT INTO `t_doctor_specialty` VALUES ('D004', 'S002');
INSERT INTO `t_doctor_specialty` VALUES ('D005', 'S001');
INSERT INTO `t_doctor_specialty` VALUES ('D006', 'S002');
INSERT INTO `t_doctor_specialty` VALUES ('D007', 'S001');
INSERT INTO `t_doctor_specialty` VALUES ('D008', 'S002');
INSERT INTO `t_doctor_specialty` VALUES ('D009', 'S001');
INSERT INTO `t_doctor_specialty` VALUES ('D010', 'S002');
INSERT INTO `t_doctor_specialty` VALUES ('D011', 'S001');
INSERT INTO `t_doctor_specialty` VALUES ('D012', 'S002');
INSERT INTO `t_doctor_specialty` VALUES ('D013', 'S001');
DROP TABLE IF EXISTS `t_patient_file`;

CREATE TABLE `t_patient_file` (
  `id` varchar(255) NOT NULL,
  `date_of_birth` DATE NOT NULL,
  `referring_doctor_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_referring_doctor_id` (`referring_doctor_id`),
  CONSTRAINT `FK_doctor_patient_file` FOREIGN KEY (`referring_doctor_id`) REFERENCES `t_doctor` (`id`),
  CONSTRAINT `FK_file_patient_file` FOREIGN KEY (`id`) REFERENCES `t_file` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

INSERT INTO `t_patient_file` VALUES ('P001', '1995-05-15', 'D001');
INSERT INTO `t_patient_file` VALUES ('P004', '1978-02-26', 'D004');
INSERT INTO `t_patient_file` VALUES ('P005', '1974-07-16', 'D005');
INSERT INTO `t_patient_file` VALUES ('P006', '1994-05-05', 'D004');
INSERT INTO `t_patient_file` VALUES ('P007', '1991-07-28', 'D005');
INSERT INTO `t_patient_file` VALUES ('P008', '1977-12-14', 'D004');
INSERT INTO `t_patient_file` VALUES ('P009', '2000-01-01', 'D005');
INSERT INTO `t_patient_file` VALUES ('P010', '1946-02-17', 'D004');
INSERT INTO `t_patient_file` VALUES ('P011', '2000-11-30', 'D005');
INSERT INTO `t_patient_file` VALUES ('P012', '1962-06-29', 'D004');
INSERT INTO `t_patient_file` VALUES ('P013', '1986-02-27', 'D005');

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `id` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  `security_code` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_username_user` (`username`)
) ENGINE=InnoDB;

INSERT INTO `t_user` VALUES ('A001','admin','$2a$12$yjbMztwlMm0yEIVbM8ybu.nJ6kQPipI6ViV/8GbU8TWgIwSvRbAQa','ROLE_ADMIN','');
INSERT INTO `t_user` VALUES ('D001','user','$2a$12$7/n1myGRPalYXRCUbsrXz.vhtJCYTfi8j1dBlX1m/ECFosXD6jcMa','ROLE_DOCTOR','');
INSERT INTO `t_user` VALUES ('D002','doc','$2a$12$rbh2MVQ3zMmeE4a.vHQcJOlBQ7uLLE9fpcy3G.l.vfT96WhlF/51m','ROLE_DOCTOR','');
INSERT INTO `t_user` VALUES ('P001','jean','$2a$12$nGajrCywpBYm9xLtu.lAbuTTNCFX3rrHaisz87P.fw2BXCF8E/gD2','ROLE_PATIENT','');
INSERT INTO `t_user` VALUES ('P002','utilisateur','$2a$12$tClFvDjq0BaRSxi8/iird.BdMGV99a88Bun39z1yc29A9Qg0u40bm','ROLE_PATIENT','');
