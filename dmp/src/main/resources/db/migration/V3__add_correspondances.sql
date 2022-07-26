DROP TABLE IF EXISTS `t_correspondance`;

CREATE TABLE `t_correspondance` (
  `id` bigint NOT NULL,
  `dateUntil` date NOT NULL,
  `doctor_id` varchar(255) NOT NULL,
  `patient_file_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_doctor_id` (`doctor_id`),
  KEY `FK_patient_file_id` (`patient_file_id`),
  CONSTRAINT `FK_patient_file_correspondance` FOREIGN KEY (`patient_file_id`) REFERENCES `t_patient_file` (`id`),
  CONSTRAINT `FK_doctor_correspondance` FOREIGN KEY (`doctor_id`) REFERENCES `t_doctor` (`id`)
) ENGINE=InnoDB;
