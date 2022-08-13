DROP TABLE IF EXISTS `t_file`;

CREATE TABLE `t_file` (
  `id` VARCHAR(255) NOT NULL,
  `firstname` VARCHAR(255) NOT NULL,
  `lastname` VARCHAR(255) NOT NULL,
  `phone` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `street1` VARCHAR(255) NOT NULL,
  `street2` VARCHAR(255) NOT NULL,
  `city` VARCHAR(255) NOT NULL,
  `state` VARCHAR(255) NOT NULL,
  `zipcode` VARCHAR(255) NOT NULL,
  `country` VARCHAR(255) NOT NULL,
  `security_code` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

INSERT INTO `t_file` VALUES ('D001', 'John', 'Smith', '0123456789', 'john.smith@doctors.com', '1 baker street', '', 'London', '', '99999', 'United Kingdom', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG');
INSERT INTO `t_file` VALUES ('P001', 'jean', 'Martin', '0101010101', 'jean.martin@free.fr', '1 rue de la Paix', '', 'Paris', '', '75001', 'France', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG');
INSERT INTO `t_file` VALUES ('D002', 'Jean', 'Dupont', '9999999999', 'jean.dupont@docteurs.fr', '15 rue de Vaugirard', '', 'Paris', '', '75015', 'France', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG');
INSERT INTO `t_file` VALUES ('D004', 'Leah', 'Little', '061-074-7258', 'leah.little@example.com', '2727 The Avenue', 'street 2', 'Nenagh', '', '47362', 'Ireland', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG');
INSERT INTO `t_file` VALUES ('D005', 'Vivienne', 'Roy', '077 793 50 38', 'vivienne.roy@example.com', '1661 Rue du Village', 'street 2', 'Lichtensteig', '', '2126', 'Switzerland', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG');
INSERT INTO `t_file` VALUES ('D006', 'Vukašin', 'Adamović', '016-3028-125', 'vukasin.adamovic@example.com', '6351 Bresjačka', 'street 2', 'Senta', '', '95114', 'Serbia', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG');
INSERT INTO `t_file` VALUES ('D007', 'Eugenio', 'Olvera', '(615) 831 6362', 'eugenio.olvera@example.com', '3234 Circunvalación Bhután', 'street 2', 'Comalcalco', '', '47519', 'Mexico', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG');
INSERT INTO `t_file` VALUES ('D008', 'Darko', 'RatkovićRistić', '025-7372-402', 'darko.ratkovicristic@example.com', '1607 Dragiše Kašikovića', 'street 2', 'Boljevac', '', '14541', 'Serbia', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG');
INSERT INTO `t_file` VALUES ('D009', 'Peppi', 'Seppanen', '09-720-887', 'peppi.seppanen@example.com', '7237 Hämeenkatu', 'street 2', 'Lempäälä', '', '34397', 'Finland', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG');
INSERT INTO `t_file` VALUES ('D010', 'Rachel', 'Richards', '013873 97383', 'rachel.richards@example.com', '6294 St. John’S Road', 'street 2', 'Cambridge', '', 'WJ1 8LF', 'United Kingdom', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG');
INSERT INTO `t_file` VALUES ('D011', 'Bill', 'Carlson', '071-524-4134', 'bill.carlson@example.com', '2581 Mill Lane', 'street 2', 'Trim', '', '15049', 'Ireland', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG');
INSERT INTO `t_file` VALUES ('D012', 'Melquisedeque', 'Nascimento', '(21) 5214-4747', 'melquisedeque.nascimento@example.com', '2155 Rua Santa Luzia ', 'street 2', 'Olinda', '', '51415', 'Brazil', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG');
INSERT INTO `t_file` VALUES ('D013', 'Luke', 'Hansen', '061-855-6303', 'luke.hansen@example.com', '1201 Grange Road', 'street 2', 'Gorey', '', '71364', 'Ireland', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG');
INSERT INTO `t_file` VALUES ('P004', 'Roelf', 'Verwijmeren', '(089) 8518423', 'roelf.verwijmeren@example.com', '7481 Keerderstraat', 'street 2', 'Spijk Gld', '', '4951 XC', 'Netherlands', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG');
INSERT INTO `t_file` VALUES ('P005', 'Martha', 'Welch', '00-1044-2420', 'martha.welch@example.com', '2186 Blossom Hill Rd', 'street 2', 'Dubbo', '', '1178', 'Australia', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG');
INSERT INTO `t_file` VALUES ('P006', 'Lyubomila', 'Kalnickiy', '(098) A93-0296', 'lyubomila.kalnickiy@example.com', '1406 Mikoli Gastello', 'street 2', 'Irpin', '', '45431', 'Ukraine', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG');
INSERT INTO `t_file` VALUES ('P007', 'Phoebe', 'Clarke', '(183)-791-1522', 'phoebe.clarke@example.com', '5100 Weymouth Road', 'street 2', 'Wellington', '', '54665', 'New Zealand', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG');
INSERT INTO `t_file` VALUES ('P008', 'Sami', 'Mjønes', '32959974', 'sami.mjones@example.com', '7467 Midtoddveien', 'street 2', 'Vear', '', '9365', 'Norway', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG');
INSERT INTO `t_file` VALUES ('P009', 'Alvaro', 'Canales', '(666) 146 7433', 'alvaro.canales@example.com', '1413 Circunvalación Tamaulipas', 'street 2', 'Morelos Cañada', '', '85598', 'Mexico', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG');
INSERT INTO `t_file` VALUES ('P010', 'Susan', 'Edwards', '031-909-2200', 'susan.edwards@example.com', '4577 Patrick Street', 'street 2', 'Wexford', '', '11051', 'Ireland', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG');
INSERT INTO `t_file` VALUES ('P011', 'Marie-Louise', 'Leroux', '077 177 95 66', 'marie-louise.leroux@example.com', '2556 Rue Dumenge', 'street 2', 'Savigny', '', '8618', 'Switzerland', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG');
INSERT INTO `t_file` VALUES ('P012', 'Julio', 'Bernard', '077 202 55 32', 'julio.bernard@example.com', '1492 Place des 44 Enfants D Izieu', 'street 2', 'Kernenried', '', '8314', 'Switzerland', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG');
INSERT INTO `t_file` VALUES ('P013', 'Magnus', 'Jensen', '19776685', 'magnus.jensen@example.com', '165 Bakkevænget', 'street 2', 'Stenderup', '', '19960', 'Denmark', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG');
INSERT INTO `t_file` VALUES ('P014', 'Fatih', 'Toraman', '(608)-401-2304', 'fatih.toraman@example.com', '2051 Şehitler Cd', 'street 2', 'Bingöl', '', '63992', 'Turkey', '$2a$12$8QR0h6V/yp78HDbENpF2welnrf9mXlhsIfNPoXxREKoN.geg8d/YG');

DROP TABLE IF EXISTS `t_doctor`;

CREATE TABLE `t_doctor` (
  `id` VARCHAR(255) NOT NULL,
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

DROP TABLE IF EXISTS `t_doctor_specialty`;

CREATE TABLE `t_doctor_specialty` (
  `doctor_id` VARCHAR(255) NOT NULL,
  `specialty_id` VARCHAR(255) NOT NULL,
  KEY `FK_specialty_id` (`specialty_id`),
  KEY `FK_doctor_id` (`doctor_id`),
  CONSTRAINT `FK_doctor_doctor_specialty` FOREIGN KEY (`doctor_id`) REFERENCES `t_doctor` (`id`),
  CONSTRAINT `FK_specialty_doctor_specialty` FOREIGN KEY (`specialty_id`) REFERENCES `t_specialty` (`id`)
) ENGINE=InnoDB;

INSERT INTO t_doctor_specialty(doctor_id, specialty_id) values ('D001', 'S001');
INSERT INTO t_doctor_specialty(doctor_id, specialty_id) values ('D001', 'S024');
INSERT INTO t_doctor_specialty(doctor_id, specialty_id) values ('D002', 'S012');
INSERT INTO t_doctor_specialty(doctor_id, specialty_id) values ('D002', 'S013');
INSERT INTO t_doctor_specialty(doctor_id, specialty_id) values ('D004', 'S017');
INSERT INTO t_doctor_specialty(doctor_id, specialty_id) values ('D005', 'S045');
INSERT INTO t_doctor_specialty(doctor_id, specialty_id) values ('D006', 'S032');
INSERT INTO t_doctor_specialty(doctor_id, specialty_id) values ('D007', 'S027');
INSERT INTO t_doctor_specialty(doctor_id, specialty_id) values ('D007', 'S028');
INSERT INTO t_doctor_specialty(doctor_id, specialty_id) values ('D008', 'S002');
INSERT INTO t_doctor_specialty(doctor_id, specialty_id) values ('D009', 'S044');
INSERT INTO t_doctor_specialty(doctor_id, specialty_id) values ('D010', 'S039');
INSERT INTO t_doctor_specialty(doctor_id, specialty_id) values ('D011', 'S035');
INSERT INTO t_doctor_specialty(doctor_id, specialty_id) values ('D012', 'S011');
INSERT INTO t_doctor_specialty(doctor_id, specialty_id) values ('D012', 'S012');
INSERT INTO t_doctor_specialty(doctor_id, specialty_id) values ('D013', 'S012');

DROP TABLE IF EXISTS `t_patient_file`;

CREATE TABLE `t_patient_file` (
  `id` VARCHAR(255) NOT NULL,
  `date_of_birth` DATE NOT NULL,
  `referring_doctor_id` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_referring_doctor_id` (`referring_doctor_id`),
  CONSTRAINT `FK_doctor_patient_file` FOREIGN KEY (`referring_doctor_id`) REFERENCES `t_doctor` (`id`),
  CONSTRAINT `FK_file_patient_file` FOREIGN KEY (`id`) REFERENCES `t_file` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

INSERT INTO `t_patient_file` VALUES ('P001', '1995-05-15', 'D001');
INSERT INTO `t_patient_file` VALUES ('P004', '1978-02-26', 'D004');
INSERT INTO `t_patient_file` VALUES ('P005', '1974-07-16', 'D001');
INSERT INTO `t_patient_file` VALUES ('P006', '1994-05-05', 'D004');
INSERT INTO `t_patient_file` VALUES ('P007', '1991-07-28', 'D005');
INSERT INTO `t_patient_file` VALUES ('P008', '1977-12-14', 'D004');
INSERT INTO `t_patient_file` VALUES ('P009', '2000-01-01', 'D005');
INSERT INTO `t_patient_file` VALUES ('P010', '1946-02-17', 'D004');
INSERT INTO `t_patient_file` VALUES ('P011', '2000-11-30', 'D005');
INSERT INTO `t_patient_file` VALUES ('P012', '1962-06-29', 'D004');
INSERT INTO `t_patient_file` VALUES ('P013', '1986-02-27', 'D005');
INSERT INTO `t_patient_file` VALUES ('P014', '1999-05-22', 'D001');
