DROP TABLE IF EXISTS `t_record`;

CREATE TABLE `t_record` (
  `id` VARCHAR(255) NOT NULL,
  `firstname` VARCHAR(255) NOT NULL,
  `lastname` VARCHAR(255) NOT NULL,
  `date_of_birth` DATE NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

INSERT INTO `t_record` VALUES ('P001', 'Eric', 'Martin', '1995-05-15');
INSERT INTO `t_record` VALUES ('P004', 'Roelf', 'Verwijmeren', '1978-02-26');
INSERT INTO `t_record` VALUES ('P005', 'Martha', 'Welch', '1974-07-16');
INSERT INTO `t_record` VALUES ('P006', 'Lyubomila', 'Kalnickiy', '1994-05-05');
INSERT INTO `t_record` VALUES ('P007', 'Phoebe', 'Clarke', '1991-07-28');
INSERT INTO `t_record` VALUES ('P008', 'Sami', 'Mjønes', '1977-12-14');
INSERT INTO `t_record` VALUES ('P009', 'Alvaro', 'Canales', '2000-01-01');
INSERT INTO `t_record` VALUES ('P010', 'Susan', 'Edwards', '1946-02-17');
INSERT INTO `t_record` VALUES ('P011', 'Marie-Louise', 'Leroux', '2000-11-30');
INSERT INTO `t_record` VALUES ('P012', 'Julio', 'Bernard', '1962-06-29');
INSERT INTO `t_record` VALUES ('P013', 'Magnus', 'Jensen', '1986-02-27');
INSERT INTO `t_record` VALUES ('P014', 'Fatih', 'Toraman', '1999-05-22');
INSERT INTO `t_record` VALUES ('P015', 'Marc', 'Dubois', '1975-06-12');
INSERT INTO `t_record` VALUES ('P016', 'Rosalyn', 'Flores', '1951-06-28');
INSERT INTO `t_record` VALUES ('P017', 'Monisha', 'Pai', '1974-07-12');
INSERT INTO `t_record` VALUES ('P018', 'Nimit', 'Uchil', '1949-01-01');
INSERT INTO `t_record` VALUES ('P019', 'Jonas', 'Mortensen', '1973-08-21');
INSERT INTO `t_record` VALUES ('P020', 'Jill', 'Carter', '1986-05-22');
INSERT INTO `t_record` VALUES ('P021', 'Aapo', 'Saari', '1999-12-16');
INSERT INTO `t_record` VALUES ('P022', 'Karl', 'Oliver', '1997-04-23');
INSERT INTO `t_record` VALUES ('P023', 'David', 'Lavoie', '1963-12-23');
INSERT INTO `t_record` VALUES ('P024', 'Hanna', 'Hannig', '1994-04-03');
INSERT INTO `t_record` VALUES ('P025', 'Erwin', 'Robert', '1977-12-18');
