DROP TABLE IF EXISTS `t_correspondance`;

CREATE TABLE `t_correspondance` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date_until` date NOT NULL,
  `doctor_id` varchar(255) NOT NULL,
  `patient_file_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_doctor_id` (`doctor_id`),
  KEY `FK_patient_file_id` (`patient_file_id`),
  CONSTRAINT `FK_patient_file_correspondance` FOREIGN KEY (`patient_file_id`) REFERENCES `t_patient_file` (`id`),
  CONSTRAINT `FK_doctor_correspondance` FOREIGN KEY (`doctor_id`) REFERENCES `t_doctor` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16;

INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES(1, '2023-05-02', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES(2, '2024-01-05', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES(3, '2022-04-12', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES(4, '2022-11-30', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES(5, '2023-10-31', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES(6, '2023-01-01', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES(7, '2026-03-22', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES(8, '2023-06-15', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES(9, '2022-07-12', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES(10, '2023-04-24', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES(11, '2023-08-14', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES(12, '2024-02-07', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES(13, '2022-11-03', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES(14, '2025-12-12', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES(15, '2027-05-07', 'D001', 'P001');
