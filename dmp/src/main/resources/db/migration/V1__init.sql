-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: dmp
-- ------------------------------------------------------
-- Server version	8.0.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_doctor`
--

DROP TABLE IF EXISTS `t_doctor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_doctor` (
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKaw58as6iygkn722ejq138u3w3` FOREIGN KEY (`id`) REFERENCES `t_file` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_doctor`
--

LOCK TABLES `t_doctor` WRITE;
/*!40000 ALTER TABLE `t_doctor` DISABLE KEYS */;
INSERT INTO `t_doctor` VALUES ('D001'),('D002');
/*!40000 ALTER TABLE `t_doctor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_doctor_specialty`
--

DROP TABLE IF EXISTS `t_doctor_specialty`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_doctor_specialty` (
  `doctor_id` varchar(255) NOT NULL,
  `specialty_id` varchar(255) NOT NULL,
  KEY `FKkc7j7tjo7bivshwf6auwy76us` (`specialty_id`),
  KEY `FKk83mvqsahth60iw6b0ud9ndi6` (`doctor_id`),
  CONSTRAINT `FKk83mvqsahth60iw6b0ud9ndi6` FOREIGN KEY (`doctor_id`) REFERENCES `t_doctor` (`id`),
  CONSTRAINT `FKkc7j7tjo7bivshwf6auwy76us` FOREIGN KEY (`specialty_id`) REFERENCES `t_specialty` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_doctor_specialty`
--

LOCK TABLES `t_doctor_specialty` WRITE;
/*!40000 ALTER TABLE `t_doctor_specialty` DISABLE KEYS */;
INSERT INTO `t_doctor_specialty` VALUES ('D001','S001'),('D001','S002'),('D001','S003'),('D002','S002');
/*!40000 ALTER TABLE `t_doctor_specialty` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_file`
--

DROP TABLE IF EXISTS `t_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_file`
--

LOCK TABLES `t_file` WRITE;
/*!40000 ALTER TABLE `t_file` DISABLE KEYS */;
INSERT INTO `t_file` VALUES ('D001','London','United Kingdom','','1 baker street','','99999','john.smith@doctors.com','John','Smith','0123456789','45'),('D002','Paris','France','','15 rue de Vaugirard','','75015','jean.dupont@docteurs.fr','Jean','Dupont','9999999999','999'),('P001','Paris','France','','1 rue de la Paix','','75001','jean.martin@free.fr','Jean','Martin','0101010101','0000'),('P002','Paris','France','','11 rue de la Convention','','75015','paul.dubois@orange.fr','Paul','Dubois','5555555555','1111');
/*!40000 ALTER TABLE `t_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_patient_file`
--

DROP TABLE IF EXISTS `t_patient_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_patient_file` (
  `id` varchar(255) NOT NULL,
  `referring_doctor_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK45nd8ypvq2j86bql7l78w89r7` (`referring_doctor_id`),
  CONSTRAINT `FK45nd8ypvq2j86bql7l78w89r7` FOREIGN KEY (`referring_doctor_id`) REFERENCES `t_doctor` (`id`),
  CONSTRAINT `FKh8lgs6yjrmu6jy40gmhx5n06w` FOREIGN KEY (`id`) REFERENCES `t_file` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_patient_file`
--

LOCK TABLES `t_patient_file` WRITE;
/*!40000 ALTER TABLE `t_patient_file` DISABLE KEYS */;
INSERT INTO `t_patient_file` VALUES ('P001','D001'),('P002','D002');
/*!40000 ALTER TABLE `t_patient_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_specialty`
--

DROP TABLE IF EXISTS `t_specialty`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_specialty` (
  `id` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_specialty`
--

LOCK TABLES `t_specialty` WRITE;
/*!40000 ALTER TABLE `t_specialty` DISABLE KEYS */;
INSERT INTO `t_specialty` VALUES ('S001','Specialty 1'),('S002','Specialty 2'),('S003','Specialty 3');
/*!40000 ALTER TABLE `t_specialty` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_user` (
  `id` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) NOT NULL,
  `security_code` varchar(255) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_jhib4legehrm4yscx9t3lirqi` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user`
--

LOCK TABLES `t_user` WRITE;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` VALUES ('A001','$2a$12$yjbMztwlMm0yEIVbM8ybu.nJ6kQPipI6ViV/8GbU8TWgIwSvRbAQa','ROLE_ADMIN','','admin'),('D001','$2a$12$7/n1myGRPalYXRCUbsrXz.vhtJCYTfi8j1dBlX1m/ECFosXD6jcMa','ROLE_DOCTOR','','user'),('D002','$2a$12$rbh2MVQ3zMmeE4a.vHQcJOlBQ7uLLE9fpcy3G.l.vfT96WhlF/51m','ROLE_DOCTOR','','doc'),('P001','$2a$12$nGajrCywpBYm9xLtu.lAbuTTNCFX3rrHaisz87P.fw2BXCF8E/gD2','ROLE_PATIENT','','jean'),('P002','$2a$12$tClFvDjq0BaRSxi8/iird.BdMGV99a88Bun39z1yc29A9Qg0u40bm','ROLE_PATIENT','','utilisateur');
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-07-19 11:14:51
