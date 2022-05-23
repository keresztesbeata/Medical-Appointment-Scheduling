-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: medical_appointment_scheduling_db
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounts` (
  `id` int NOT NULL,
  `account_type` varchar(255) NOT NULL,
  `password` varchar(100) NOT NULL,
  `username` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_k8h1bgqoplx0rkngj01pm1rgp` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES (1,'DOCTOR','$2a$10$EqLbI3PjXpAA8A8FWwHhCOXgrsC25Gn4ztBv7KCZym8/0lwjGjIha','DrDolittle123#'),(2,'PATIENT','$2a$10$tu4BbRtyKQExggNE8Uc/veiMeUsxX/pMrHmHPQY8CWQIbYg/.7l5O','Garfield123#'),(4,'RECEPTIONIST','$2a$10$NuAKd0hYV23tg0qq9xUV0.p141rIfwGjMnVto03fPKIGEtSLjG256','Receptionist123#'),(5,'DOCTOR','$2a$10$OC.x5pyQ2PC2yfs4ChleW.itSFY6KC5YF8AEyrGLq2FfYu7oNy8xi','DrStrange123#');
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointments`
--

DROP TABLE IF EXISTS `appointments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointments` (
  `id` int NOT NULL,
  `appointment_date` datetime(6) DEFAULT NULL,
  `status` int NOT NULL,
  `doctor_id` int NOT NULL,
  `medical_service_id` int NOT NULL,
  `patient_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKo690gyordobh8dp3ea367v871` (`doctor_id`),
  KEY `FKbld694rss80aj4rm8ubwwcyie` (`medical_service_id`),
  KEY `FKekqhokpngvgudsgud3vflkecv` (`patient_id`),
  CONSTRAINT `FKbld694rss80aj4rm8ubwwcyie` FOREIGN KEY (`medical_service_id`) REFERENCES `medical_services` (`id`),
  CONSTRAINT `FKekqhokpngvgudsgud3vflkecv` FOREIGN KEY (`patient_id`) REFERENCES `patient_profiles` (`id`),
  CONSTRAINT `FKo690gyordobh8dp3ea367v871` FOREIGN KEY (`doctor_id`) REFERENCES `doctor_profiles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointments`
--

LOCK TABLES `appointments` WRITE;
/*!40000 ALTER TABLE `appointments` DISABLE KEYS */;
INSERT INTO `appointments` VALUES (3,'2022-05-22 12:56:00.000000',3,1,2,2),(6,'2022-05-22 18:52:00.000000',3,5,5,2),(7,'2022-05-22 15:26:00.000000',3,1,2,2),(8,'2022-05-22 14:38:00.000000',5,5,4,2),(9,NULL,0,1,5,2),(10,'2022-06-22 15:32:00.000000',5,1,1,2),(11,NULL,0,1,5,2),(12,'2022-05-23 15:31:00.000000',4,5,4,2);
/*!40000 ALTER TABLE `appointments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctor_profiles`
--

DROP TABLE IF EXISTS `doctor_profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctor_profiles` (
  `id` int NOT NULL,
  `finish_time` time NOT NULL,
  `first_name` varchar(200) NOT NULL,
  `last_name` varchar(200) NOT NULL,
  `start_time` time NOT NULL,
  `specialty_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9j8uwaq3c6a5j4d67g91uadcx` (`specialty_id`),
  CONSTRAINT `FK9j8uwaq3c6a5j4d67g91uadcx` FOREIGN KEY (`specialty_id`) REFERENCES `specialties` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor_profiles`
--

LOCK TABLES `doctor_profiles` WRITE;
/*!40000 ALTER TABLE `doctor_profiles` DISABLE KEYS */;
INSERT INTO `doctor_profiles` VALUES (1,'16:00:00','John','Dolittle','10:00:00',1),(5,'14:00:00','Stephen','Strange','10:00:00',4);
/*!40000 ALTER TABLE `doctor_profiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (13);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medical_services`
--

DROP TABLE IF EXISTS `medical_services`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medical_services` (
  `id` int NOT NULL,
  `duration` int DEFAULT NULL,
  `name` varchar(200) NOT NULL,
  `specialty_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_l6my34gbav3wsaql0r8lr0ctl` (`name`),
  KEY `FKst689qoxk61bd6dn5ofnji0uv` (`specialty_id`),
  CONSTRAINT `FKst689qoxk61bd6dn5ofnji0uv` FOREIGN KEY (`specialty_id`) REFERENCES `specialties` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medical_services`
--

LOCK TABLES `medical_services` WRITE;
/*!40000 ALTER TABLE `medical_services` DISABLE KEYS */;
INSERT INTO `medical_services` VALUES (1,15,'Vaccination',1),(2,15,'Flu',1),(3,30,'Facial cleansing',4),(4,25,'Mole removal',4),(5,20,'General check-up',1),(6,45,'Asthma',1);
/*!40000 ALTER TABLE `medical_services` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient_profiles`
--

DROP TABLE IF EXISTS `patient_profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient_profiles` (
  `id` int NOT NULL,
  `allergies` varchar(300) DEFAULT NULL,
  `birth_date` date NOT NULL,
  `email` varchar(100) NOT NULL,
  `first_name` varchar(200) NOT NULL,
  `last_name` varchar(200) NOT NULL,
  `phone` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_8ca0woys6515l1frrihvt3e8i` (`email`),
  UNIQUE KEY `UK_ihi4o2dfgc4tmr62uubg7ou5j` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient_profiles`
--

LOCK TABLES `patient_profiles` WRITE;
/*!40000 ALTER TABLE `patient_profiles` DISABLE KEYS */;
INSERT INTO `patient_profiles` VALUES (2,'sport','2001-01-01','garfield.cat@email.com','Just','Garfield','0123456789');
/*!40000 ALTER TABLE `patient_profiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prescriptions`
--

DROP TABLE IF EXISTS `prescriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prescriptions` (
  `appointment_id` int NOT NULL,
  `indications` varchar(500) DEFAULT NULL,
  `medication` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`appointment_id`),
  CONSTRAINT `FKe2fpvlkkcgcd40k4ufyyju2al` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescriptions`
--

LOCK TABLES `prescriptions` WRITE;
/*!40000 ALTER TABLE `prescriptions` DISABLE KEYS */;
INSERT INTO `prescriptions` VALUES (3,'3 times a day before each meal','Aloe Vera drink'),(10,'Once per day to calm fever','Paracetamol 500g');
/*!40000 ALTER TABLE `prescriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `specialties`
--

DROP TABLE IF EXISTS `specialties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `specialties` (
  `id` int NOT NULL,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_bhb8s9o5hv30lkbidtod9cixc` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `specialties`
--

LOCK TABLES `specialties` WRITE;
/*!40000 ALTER TABLE `specialties` DISABLE KEYS */;
INSERT INTO `specialties` VALUES (5,'Cardiology'),(4,'Dermatology'),(3,'Endocrinology'),(2,'Gyneacology'),(1,'Pediatrics');
/*!40000 ALTER TABLE `specialties` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-05-23  9:48:58
