DROP TABLE IF EXISTS `t_correspondance`;

CREATE TABLE `t_correspondance` (
  `id` VARCHAR(255) NOT NULL,
  `date_until` DATE NOT NULL,
  `doctor_id` VARCHAR(255) NOT NULL,
  `patient_file_id` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_doctor_id` (`doctor_id`),
  KEY `FK_patient_file_id` (`patient_file_id`),
  CONSTRAINT `FK_patient_file_correspondance` FOREIGN KEY (`patient_file_id`) REFERENCES `t_patient_file` (`id`),
  CONSTRAINT `FK_doctor_correspondance` FOREIGN KEY (`doctor_id`) REFERENCES `t_doctor` (`id`)
) ENGINE=InnoDB;

INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES('5b17ffa7-81e2-43ac-9246-7cab5b2f0f6b', '2023-05-02', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES('a376a45f-17d3-4b75-ad08-6b1da02616b6', '2024-01-05', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES('531c2161-5507-485f-8a23-de65416b4644', '2022-04-12', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES('680e2054-b757-4d1e-94dc-1baeb0dbedf8', '2022-11-30', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES('119b2f84-80ee-4d83-adb1-d788d85c76fd', '2023-10-31', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES('e1eb3425-d257-4c5e-8600-b125731c458c', '2023-01-01', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES('2454a376-9f8c-4e01-ab07-87b3cb992ae3', '2026-03-22', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES('f549abff-1a11-4d9e-9cea-fcbef29eb35c', '2023-06-15', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES('77aaab4e-6b17-4c3a-88c0-b007122db4bc', '2022-07-12', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES('ef11c7e1-1694-474e-8d34-eb0a1677b140', '2023-04-24', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES('3d80bbeb-997e-4354-82d3-68cea80256d6', '2023-08-14', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES('1bd7028d-c850-4439-8cd3-8a38fb9365a2', '2024-02-07', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES('1e5670ac-3c72-4671-a22f-94a57ab72982', '2022-11-03', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES('7fd83511-1709-44b4-b60d-b100093402cb', '2025-12-12', 'D001', 'P001');
INSERT INTO t_correspondance(id, date_until, doctor_id, patient_file_id) VALUES('8ea37abc-052d-4bc3-9aa1-f9a47e366e11', '2027-05-07', 'D001', 'P001');
