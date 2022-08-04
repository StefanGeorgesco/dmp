DROP TABLE IF EXISTS `t_patient_file_item`;

CREATE TABLE `t_patient_file_item` (
  `id` varchar(255) NOT NULL,
  `date` date NOT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `authoring_doctor_id` varchar(255) NOT NULL,
  `patient_file_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_authoring_doctor_id` (`authoring_doctor_id`),
  KEY `FKmv9senx13a9vhmy2xhe7m6t1e` (`patient_file_id`),
  CONSTRAINT `FK_doctor_patient_file_item` FOREIGN KEY (`authoring_doctor_id`) REFERENCES `t_doctor` (`id`),
  CONSTRAINT `FK_patient_file_patient_file_item` FOREIGN KEY (`patient_file_id`) REFERENCES `t_patient_file` (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `t_act`;

CREATE TABLE `t_act` (
  `id` varchar(255) NOT NULL,
  `medical_act_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_medical_act_id` (`medical_act_id`),
  CONSTRAINT `FK_patient_file_item_act` FOREIGN KEY (`id`) REFERENCES `t_patient_file_item` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_medical_act_act` FOREIGN KEY (`medical_act_id`) REFERENCES `t_medical_act` (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `t_diagnosis`;

CREATE TABLE `t_diagnosis` (
  `id` varchar(255) NOT NULL,
  `disease_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_disease_id` (`disease_id`),
  CONSTRAINT `FK_patient_file_item_diagnosis` FOREIGN KEY (`id`) REFERENCES `t_patient_file_item` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_disease_diagnosis` FOREIGN KEY (`disease_id`) REFERENCES `t_disease` (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `t_mail`;

CREATE TABLE `t_mail` (
  `id` varchar(255) NOT NULL,
  `text` varchar(1000) DEFAULT NULL,
  `recipient_doctor_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_recipient_doctor_id` (`recipient_doctor_id`),
  CONSTRAINT `FK_patient_file_item_mail` FOREIGN KEY (`id`) REFERENCES `t_patient_file_item` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_doctor_mail` FOREIGN KEY (`recipient_doctor_id`) REFERENCES `t_doctor` (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `t_prescription`;

CREATE TABLE `t_prescription` (
  `id` varchar(255) NOT NULL,
  `description` varchar(800) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_patient_file_item_prescription` FOREIGN KEY (`id`) REFERENCES `t_patient_file_item` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `t_symptom`;

CREATE TABLE `t_symptom` (
  `id` varchar(255) NOT NULL,
  `description` varchar(800) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_patient_file_item_symptom` FOREIGN KEY (`id`) REFERENCES `t_patient_file_item` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;
