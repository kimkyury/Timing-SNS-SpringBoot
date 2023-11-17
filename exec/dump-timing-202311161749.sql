-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: k9e203.p.ssafy.io    Database: timing
-- ------------------------------------------------------
-- Server version	5.5.5-10.3.38-MariaDB-0ubuntu0.20.04.1

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
-- Table structure for table `challenge_hash_tags`
--

DROP TABLE IF EXISTS `challenge_hash_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `challenge_hash_tags` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `hash_tag_option_id` int(11) NOT NULL,
  `challenge_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_challenge_hash_tags_challenge_id_from_challenge_id` (`challenge_id`),
  KEY `fk_challenge_hash_tags_tag_option_id_from_tag_option_id` (`hash_tag_option_id`),
  CONSTRAINT `fk_challenge_hash_tags_challenge_id_from_challenge_id` FOREIGN KEY (`challenge_id`) REFERENCES `challenges` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_challenge_hash_tags_tag_option_id_from_tag_option_id` FOREIGN KEY (`hash_tag_option_id`) REFERENCES `hash_tag_options` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=251 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `challenge_hash_tags`
--

LOCK TABLES `challenge_hash_tags` WRITE;
/*!40000 ALTER TABLE `challenge_hash_tags` DISABLE KEYS */;
INSERT INTO `challenge_hash_tags` VALUES (26,1,18),(27,2,18),(76,109,40),(80,113,42),(81,112,42),(82,118,43),(83,119,43),(84,116,43),(85,117,43),(86,115,43),(87,114,43),(91,120,45),(92,111,46),(93,107,46),(94,110,46),(95,111,47),(96,107,47),(97,110,47),(98,111,48),(99,107,48),(100,110,48),(101,121,49),(102,121,50),(103,121,51),(104,121,52),(105,121,53),(106,121,54),(107,121,55),(108,121,56),(112,111,58),(113,107,58),(114,110,58),(115,111,59),(116,107,59),(117,110,59),(118,1,64),(131,124,70),(132,125,70),(133,123,70),(136,1,72),(137,1,73),(143,129,77),(144,128,77),(145,3,77),(146,129,78),(147,128,78),(148,3,78),(210,1,103),(219,145,106),(220,146,106),(221,147,106),(222,137,106),(223,139,106),(230,124,109),(231,125,109),(232,123,109);
/*!40000 ALTER TABLE `challenge_hash_tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `challenges`
--

DROP TABLE IF EXISTS `challenges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `challenges` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `member_id` int(11) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `started_at` date NOT NULL,
  `ended_at` date NOT NULL,
  `goal_content` varchar(255) DEFAULT NULL,
  `object_url` varchar(255) DEFAULT NULL,
  `thumbnail_url` varchar(255) DEFAULT '/default_thumbnail.png',
  `polygon_url` varchar(255) DEFAULT NULL,
  `is_process` int(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `fk_challenges_member_id_from_member_id` (`member_id`),
  KEY `fk_challenges_parent_id_from_feed_id` (`parent_id`),
  CONSTRAINT `fk_challenges_member_id_from_member_id` FOREIGN KEY (`member_id`) REFERENCES `members` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_challenges_parent_id_from_feed_id` FOREIGN KEY (`parent_id`) REFERENCES `feeds` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `challenges`
--

LOCK TABLES `challenges` WRITE;
/*!40000 ALTER TABLE `challenges` DISABLE KEYS */;
INSERT INTO `challenges` VALUES (18,8,NULL,'2023-11-11','2023-12-02',NULL,NULL,'/default_thumbnail.png',NULL,0),(20,9,NULL,'2023-11-15','2023-12-06',NULL,'/c8af1b14-b666-423f-8e2d-4ce230ac85d2_blob','/ec09f97d-4d25-4e27-8b80-e8a1cb28beae_blob','/c850c834-8bee-4c24-8e51-00337c2aacf1_polygon',0),(21,10,NULL,'2023-11-13','2023-12-04',NULL,NULL,'/default_thumbnail.png',NULL,0),(22,11,NULL,'2023-11-13','2023-12-04',NULL,'/84a9d375-3216-4a6b-9d6d-833e392f09b0_blob','/8911aaed-bc41-4626-a907-a71f9d5c35bd_blob','/8fed30a9-0d84-425c-8d85-e1669196b72a_polygon',0),(40,39,NULL,'2023-11-14','2023-12-05',NULL,'/99f31e33-d96d-4ad9-91af-e981c8f003c1_blob','/5e6d69c9-4428-47f6-94b6-5bfd6b819a42_blob','/20d3c9b2-7259-415d-8450-6ee46f9cfdce_polygon',0),(42,42,NULL,'2023-11-14','2023-12-05',NULL,NULL,'/default_thumbnail.png',NULL,0),(43,42,NULL,'2023-11-14','2023-12-05',NULL,NULL,'/default_thumbnail.png',NULL,0),(45,43,NULL,'2023-12-01','2023-12-22',NULL,NULL,'/default_thumbnail.png',NULL,0),(46,8,24,'2023-09-10','2023-10-01','달성','/2d5a53bf-0686-4634-8584-e4fd608096da_blob','/default_thumbnail.png','/a62bbddb-c181-4a18-8249-e0801ce8e2e5_polygon',0),(47,8,24,'2023-09-10','2023-10-01',NULL,'/53ab07a1-992f-49fc-8bcb-471b30201d97_blob','/default_thumbnail.png','/317f85ae-e7b4-4a12-a001-87037c755b68_polygon',0),(48,8,24,'2023-11-14','2023-12-05','재밌겠당',NULL,'/default_thumbnail.png',NULL,0),(49,44,NULL,'2023-09-10','2023-10-01',NULL,'/84740796-f9b7-4f45-9d60-d49a1b08b150_blob','/default_thumbnail.png','/cb8d8336-38f1-4c12-8313-7e74b187c32f_polygon',0),(50,44,NULL,'2023-09-10','2023-10-01',NULL,'/034e436e-4b85-4357-96a6-c061791264ae_blob','/default_thumbnail.png','/8cf237bb-c9af-4966-a951-b22082e7d5ce_polygon',0),(51,44,NULL,'2023-09-10','2023-10-01',NULL,'/85ddd7a9-bb50-4cfd-a651-effbbf5a8409_blob','/default_thumbnail.png','/0af8d0d6-6453-4be7-9a99-ca064a4bdfc4_polygon',0),(52,44,NULL,'2023-09-10','2023-10-01',NULL,'/b0c804f6-fac2-4ab0-bf66-97a41bc8e258_blob','/default_thumbnail.png','/1ba7bc99-8966-48fd-9b6a-beeb71b7cbab_polygon',0),(53,44,NULL,'2023-09-10','2023-10-01',NULL,'/f11238c8-4257-4373-a617-dbd886221282_blob','/default_thumbnail.png','/2997f1ed-fc6d-45cd-bb9f-a0756ac142e5_polygon',0),(54,44,NULL,'2023-09-10','2023-10-01',NULL,'/0762ccbd-e89f-41bb-adc5-4c6733c865f3_blob','/default_thumbnail.png','/f942f900-f3c6-4032-a5fd-9d00b04880b3_polygon',0),(55,44,NULL,'2023-09-10','2023-10-01',NULL,'/b3b42abb-748a-4987-ae9e-5b76d7728f5f_blob','/default_thumbnail.png','/4666f720-07ca-4a07-b7a0-10be47c064af_polygon',0),(56,44,NULL,'2023-09-10','2023-10-01',NULL,'/2e53319a-c4d0-4137-a07a-3b7a660da99c_blob','/default_thumbnail.png','/6f57ee10-634c-4707-8a78-ed2d64281df9_polygon',0),(58,8,24,'2023-08-15','2023-09-05','별 거 아니겠죠','/4dd22ffe-0989-4543-b1dd-dc7c71490165_watch9_object.png','/4ec6354b-e6e3-4fdf-a57a-3c54c0ae323c_1.jpg','/0ef69a57-0b53-4aad-abbe-6378bd4ea6d6_polygon',0),(59,40,36,'2023-11-14','2023-12-05','',NULL,'/23da851a-d38b-44de-badb-df9374b73d08_blob',NULL,0),(64,40,NULL,'2023-11-14','2023-12-05',NULL,NULL,'/default_thumbnail.png',NULL,0),(65,39,NULL,'2023-11-14','2023-12-05',NULL,NULL,'/cfd92b0b-9fd0-4b01-9bff-968ee9e2b708_blob',NULL,0),(70,19,41,'2023-09-10','2023-10-01',NULL,'/3d5396ca-73dc-411c-bff3-9f19fe413b86_blob','/27a73329-ba8a-427b-affc-05067953f297_1_-_복사본.jpg','/d8146e70-749b-4f37-a43c-4ef11b724223_polygon',0),(72,10,28,'2023-08-29','2023-09-19','냠냠','/b16815a2-126e-42da-b454-6554a508925b_blob','/7d001a78-8e1d-4025-9d90-9dfbbaddf2a6_1.jpg','/bb37ad91-5773-4181-8a8b-1f7b115f6aaf_polygon',0),(73,10,28,'2023-08-29','2023-09-19','냠냠','/5edcd176-938f-4938-9c8e-175908dc2d6f_blob','/f8238ff8-83ea-4acd-94bd-98d19370ad6f_1.jpg','/8c34b2d9-a73d-47dd-b74a-3b4f0732a464_polygon',0),(77,24,NULL,'2023-08-24','2023-09-14',NULL,'/3d5664ff-c247-4274-9547-599fce92efb1_blob','/17f2bf42-8622-421b-b5c2-7905d17cf810_1_-_복사본.jpg','/e29373e5-791a-4d0e-ae2f-8e6b8166a565_polygon',0),(78,24,NULL,'2023-08-24','2023-09-14',NULL,'/98f931fb-2979-4ca9-b2ed-a6a286e84b86_blob','/b8b5cb48-532c-4a48-8f89-33eb76f4b3b7_1_-_복사본.jpg','/f8c8eddc-aac8-4d76-8e11-238af95d248d_polygon',0),(103,40,NULL,'2023-11-16','2023-12-07',NULL,NULL,'/default_thumbnail.png',NULL,0),(106,8,NULL,'2023-09-10','2023-10-01','지식 쌓기','/923f827a-015b-4a91-9e6d-18153c4a8613_blob','/5b87e516-fbc5-4f55-8762-a57576b5ea11_KakaoTalk_20231116_090827182.jpg','/d7c3448e-50aa-4573-b0c3-f2a06d9fe9e4_polygon',0),(109,40,41,'2023-11-16','2023-12-07','',NULL,'/default_thumbnail.png',NULL,0);
/*!40000 ALTER TABLE `challenges` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) NOT NULL,
  `member_id` int(11) NOT NULL,
  `content` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `fk_comment_feed_id_from_feed_id_idx` (`feed_id`),
  KEY `fk_comment_member_id_from_member_id_idx` (`member_id`),
  CONSTRAINT `fk_comment_feed_id_from_feed_id` FOREIGN KEY (`feed_id`) REFERENCES `feeds` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_comment_member_id_from_member_id` FOREIGN KEY (`member_id`) REFERENCES `members` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=165 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
INSERT INTO `comments` VALUES (101,20,40,'1','2023-11-13 02:11:48'),(102,20,40,'2','2023-11-13 02:11:51'),(103,20,40,'3','2023-11-13 02:11:52'),(104,20,40,'4','2023-11-13 02:11:54'),(105,20,40,'5','2023-11-13 02:11:56'),(106,20,40,'6','2023-11-13 02:11:57'),(107,20,40,'7','2023-11-13 02:11:58'),(108,20,40,'8','2023-11-13 02:12:01'),(109,20,40,'9','2023-11-13 02:12:02'),(110,20,40,'10','2023-11-13 02:12:05'),(111,20,40,'11','2023-11-13 02:12:07'),(112,20,40,'12','2023-11-13 02:12:15'),(113,20,40,'13','2023-11-13 02:12:18'),(114,20,40,'14','2023-11-13 02:12:22'),(115,20,40,'15','2023-11-13 02:12:24'),(116,20,40,'16','2023-11-13 02:12:26'),(117,20,40,'17','2023-11-13 02:12:28'),(118,20,40,'18','2023-11-13 02:12:30'),(119,20,40,'19','2023-11-13 02:12:32'),(120,20,40,'20','2023-11-13 02:12:34'),(121,20,40,'21','2023-11-13 02:12:35'),(122,20,40,'22','2023-11-13 02:12:37'),(123,20,40,'23','2023-11-13 02:12:38'),(124,20,40,'24','2023-11-13 02:12:46'),(125,20,40,'25','2023-11-13 02:12:48'),(126,20,40,'26','2023-11-13 02:12:49'),(127,20,40,'27','2023-11-13 02:12:51'),(128,20,40,'28','2023-11-13 02:12:53'),(129,20,40,'29','2023-11-13 02:12:54'),(130,20,40,'30','2023-11-13 02:12:58'),(131,20,40,'31','2023-11-13 02:13:00'),(132,20,40,'32','2023-11-13 02:13:01'),(133,20,40,'33','2023-11-13 02:13:17'),(134,20,40,'34','2023-11-13 02:53:26'),(135,20,40,'35','2023-11-13 02:53:28'),(136,20,40,'36','2023-11-13 02:53:29'),(137,20,40,'37','2023-11-13 02:53:31'),(138,20,40,'38','2023-11-13 02:53:32'),(139,20,40,'39','2023-11-13 02:53:34'),(140,20,40,'40','2023-11-13 02:53:36'),(141,20,40,'41','2023-11-13 02:53:38'),(142,20,40,'42','2023-11-13 02:53:49'),(143,20,40,'43','2023-11-13 03:21:12'),(144,20,40,'44','2023-11-13 05:21:09'),(145,20,39,'zeze','2023-11-14 00:29:50'),(146,23,39,'헤헤헤','2023-11-14 01:37:13'),(147,20,39,'오홍홍','2023-11-14 01:45:42'),(148,20,39,'안뇽','2023-11-14 01:45:59'),(149,23,41,'헤헤헤','2023-11-14 01:47:34'),(150,26,43,'토토로 커여','2023-11-14 02:30:11'),(151,34,39,'안뇽','2023-11-14 05:17:03'),(152,31,39,'안녕','2023-11-14 05:51:50'),(153,52,39,'ㅎㅇ 염','2023-11-15 07:39:53'),(154,51,39,'안뇽하세요','2023-11-15 08:06:32'),(155,51,39,'ㅎㅅㅎ','2023-11-15 08:06:34'),(156,48,39,'gg','2023-11-15 10:27:33'),(157,41,39,'ㅎㅎ','2023-11-16 00:34:23'),(158,41,40,'Gg','2023-11-16 01:35:59'),(159,52,39,'꽃은 어디에??','2023-11-16 05:15:23'),(160,51,40,'Aa','2023-11-16 06:21:45'),(161,37,40,'ㅇ','2023-11-16 06:22:44'),(162,52,40,'ㅁ','2023-11-16 07:25:07'),(163,20,40,'댓글 달아보기','2023-11-16 07:29:19'),(164,52,40,'A','2023-11-16 07:35:41');
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feed_hash_tags`
--

DROP TABLE IF EXISTS `feed_hash_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feed_hash_tags` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `hash_tag_option_id` int(11) NOT NULL,
  `feed_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_feed_hash_tags_feed_id_from_feed_id_idx` (`feed_id`),
  KEY `fk_feed_hash-tags_option_id_from_hash_tag_option_id` (`hash_tag_option_id`),
  CONSTRAINT `fk_feed_hash-tags_option_id_from_hash_tag_option_id` FOREIGN KEY (`hash_tag_option_id`) REFERENCES `hash_tag_options` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_feed_hash_tags_feed_id_from_feed_id` FOREIGN KEY (`feed_id`) REFERENCES `feeds` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feed_hash_tags`
--

LOCK TABLES `feed_hash_tags` WRITE;
/*!40000 ALTER TABLE `feed_hash_tags` DISABLE KEYS */;
INSERT INTO `feed_hash_tags` VALUES (6,1,20),(7,2,20),(8,1,24),(9,25,20),(10,1,20),(11,1,20),(12,1,23),(13,111,24),(14,107,24),(15,110,24),(16,1,25),(17,1,26),(18,1,27),(19,1,23),(20,1,29),(21,1,30),(22,1,31),(23,1,32),(24,1,33),(25,1,34),(29,111,36),(30,107,36),(31,110,36),(32,111,37),(33,107,37),(34,110,37),(35,1,28),(36,111,38),(37,107,38),(38,110,38),(42,111,40),(43,107,40),(44,110,40),(45,124,41),(46,125,41),(47,123,41),(48,127,42),(49,126,42),(52,111,45),(53,107,45),(54,110,45),(55,131,49),(56,130,49),(57,1,49),(58,132,50),(59,1,51),(60,133,51),(61,129,52),(62,126,52),(63,3,52),(64,1,52),(68,145,54),(69,61,54),(70,152,54),(71,153,54),(72,139,54),(73,135,54);
/*!40000 ALTER TABLE `feed_hash_tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feeds`
--

DROP TABLE IF EXISTS `feeds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feeds` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `member_id` int(11) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `root_id` bigint(20) DEFAULT NULL,
  `started_at` date NOT NULL,
  `ended_at` date NOT NULL,
  `goal_content` varchar(255) DEFAULT NULL,
  `thumbnail_url` varchar(255) NOT NULL,
  `timelapse_url` varchar(255) NOT NULL,
  `is_private` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0:public, 1:private',
  `is_delete` tinyint(1) NOT NULL DEFAULT 0,
  `review` varchar(255) DEFAULT NULL,
  `updated_at` datetime NOT NULL DEFAULT current_timestamp(),
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `fk_feed_member_id_from_member_id` (`member_id`),
  KEY `fk_feed_parent_id_from_feed_id` (`parent_id`),
  KEY `fk_feed_root_id_from_feed_id` (`root_id`),
  CONSTRAINT `fk_feed_member_id_from_member_id` FOREIGN KEY (`member_id`) REFERENCES `members` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_feed_parent_id_from_feed_id` FOREIGN KEY (`parent_id`) REFERENCES `feeds` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `fk_feed_root_id_from_feed_id` FOREIGN KEY (`root_id`) REFERENCES `feeds` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feeds`
--

LOCK TABLES `feeds` WRITE;
/*!40000 ALTER TABLE `feeds` DISABLE KEYS */;
INSERT INTO `feeds` VALUES (20,40,NULL,20,'2023-11-12','2023-11-13','테스트 달성 목표','/test/test_thumbnail.png','/test/test_mp4.mp4',0,0,'테스트 리뷰','2023-11-16 07:51:47','2023-11-13 02:10:19'),(23,40,NULL,20,'2023-11-12','2023-11-13','테스트 달성 목표','/test/test_thumbnail.png','/test/test_mp4.mp4',0,0,'테스트 리뷰??','2023-11-16 07:32:02','2023-11-13 02:10:19'),(24,8,NULL,24,'2023-06-01','2023-06-22','와치 챌린지 개최합니다','/dd7c29f5-7479-4a68-a873-1fff22557add_1.jpg','/60cdf080-d981-4eb8-b4da-2fc17f31016f_video',0,0,NULL,'2023-11-14 02:56:24','2023-11-14 02:07:10'),(25,40,NULL,20,'2023-11-12','2023-11-13','테스트 달성 목표','/test/test_thumbnail.png','/test/test_mp4.mp4',0,1,'테스트 리뷰','2023-11-16 07:46:40','2023-11-13 02:10:19'),(26,40,NULL,20,'2023-11-12','2023-11-13','테스트 달성 목표','/test/test_thumbnail.png','/test/test_mp4.mp4',0,0,'테스트 리뷰??','2023-11-16 07:57:11','2023-11-13 02:10:19'),(27,40,NULL,20,'2023-11-12','2023-11-13','테스트 달성 목표','/test/test_thumbnail.png','/test/test_mp4.mp4',0,0,'테스트 리뷰','2023-11-16 07:52:03','2023-11-13 02:10:19'),(28,40,NULL,20,'2023-11-12','2023-11-13','테스트 달성 목표','/test/test_thumbnail.png','/test/test_mp4.mp4',0,0,'테스트 리뷰??','2023-11-16 07:25:51','2023-11-13 02:10:19'),(29,40,NULL,20,'2023-11-12','2023-11-13','테스트 달성 목표','/test/test_thumbnail.png','/test/test_mp4.mp4',0,0,'테스트 리뷰??','2023-11-14 02:11:24','2023-11-13 02:10:19'),(30,40,NULL,20,'2023-11-12','2023-11-13','테스트 달성 목표','/test/test_thumbnail.png','/test/test_mp4.mp4',0,0,'테스트 리뷰??','2023-11-14 02:11:24','2023-11-13 02:10:19'),(31,40,NULL,20,'2023-11-12','2023-11-13','테스트 달성 목표','/test/test_thumbnail.png','/test/test_mp4.mp4',0,0,'테스트 리뷰','2023-11-16 07:35:57','2023-11-13 02:10:19'),(32,40,NULL,20,'2023-11-12','2023-11-13','테스트 달성 목표','/test/test_thumbnail.png','/test/test_mp4.mp4',0,0,'테스트 리뷰??','2023-11-14 02:11:24','2023-11-13 02:10:19'),(33,40,NULL,20,'2023-11-12','2023-11-13','테스트 달성 목표','/test/test_thumbnail.png','/test/test_mp4.mp4',1,0,'테스트 리뷰??ㅁㅁㅁ','2023-11-16 07:26:41','2023-11-13 02:10:19'),(34,40,NULL,20,'2023-11-12','2023-11-13','테스트 달성 목표','/test/test_thumbnail.png','/test/test_mp4.mp4',0,0,'테스트 리뷰??','2023-11-16 07:14:31','2023-11-13 02:10:19'),(36,24,24,24,'2023-07-01','2023-07-22','I Will Do That!!!!!','/04a72de3-1c67-41bc-8a5a-2780915fb63c_1.jpg','/d0addda1-d68a-4dd6-8ffc-6d6bc4c45dd9_video',0,0,'Complete, It\'s Me ','2023-11-14 02:52:21','2023-11-14 02:52:21'),(37,14,36,24,'2023-08-01','2023-08-22','나도함니다','/watch6_thumbnail.jpg','/6d3747e7-bd03-4e00-abc1-bcb21d0184cc_video',0,0,NULL,'2023-11-14 05:07:56','2023-11-14 05:07:55'),(38,18,36,24,'2023-07-26','2023-08-16','힘들어도, 뿌듯하겠죠','/cda44292-475a-4a77-9d35-afea180ee814_1.jpg','/670cd0f2-a870-40e8-8350-19c63b097f30_video',0,0,NULL,'2023-11-14 05:54:48','2023-11-14 05:54:47'),(40,44,24,24,'2023-10-07','2023-10-28',NULL,'/ed89996c-a9e0-4c02-9252-9b059560b728_1.jpg','/c37afba8-063b-4262-904e-17fe1f4b3c0a_video',0,0,NULL,'2023-11-15 02:40:07','2023-11-15 02:40:06'),(41,8,NULL,41,'2023-10-25','2023-11-15','집밥먹기챌린지','/e77c31b7-8708-4a54-8b64-fd707d42b2da_q.jpg','/b3eaa1cd-6958-4567-a7f8-052a34df29eb_video',0,0,NULL,'2023-11-15 03:12:46','2023-11-15 03:12:46'),(42,19,NULL,42,'2023-09-10','2023-10-01',NULL,'/99e2ffee-228e-4d6e-835c-c6238bd1717a_1.jpg','/c03b2fcc-0a04-4c24-8d1c-35438cc79a5e_video',0,0,NULL,'2023-11-15 03:27:01','2023-11-15 03:27:01'),(45,20,24,24,'2023-07-07','2023-07-28','시작이 반이니까','/06ae39eb-4872-45b1-af14-fe7616f0bda9_1.jpg','/79c9d3b4-c441-496b-ae06-c27fa0afe427_video',1,0,NULL,'2023-11-15 06:18:25','2023-11-15 06:18:24'),(46,19,NULL,42,'2023-09-10','2023-10-01',NULL,'/99e2ffee-228e-4d6e-835c-c6238bd1717a_1.jpg','/c03b2fcc-0a04-4c24-8d1c-35438cc79a5e_video',0,0,NULL,'2023-11-15 03:27:01','2023-11-15 03:27:01'),(47,19,NULL,42,'2023-09-10','2023-10-01',NULL,'/99e2ffee-228e-4d6e-835c-c6238bd1717a_1.jpg','/c03b2fcc-0a04-4c24-8d1c-35438cc79a5e_video',0,0,NULL,'2023-11-15 03:27:01','2023-11-15 03:27:01'),(48,19,NULL,42,'2023-09-10','2023-10-01',NULL,'/99e2ffee-228e-4d6e-835c-c6238bd1717a_1.jpg','/c03b2fcc-0a04-4c24-8d1c-35438cc79a5e_video',0,0,NULL,'2023-11-15 03:27:01','2023-11-15 03:27:01'),(49,24,NULL,49,'2023-08-24','2023-09-14',NULL,'/3be0192d-46d6-4f1c-850b-959fd11cda5d_9.jpg','/bbeb14ad-22ee-45dc-9641-53ba7088a101_video',1,0,NULL,'2023-11-15 06:41:47','2023-11-15 06:41:46'),(50,11,NULL,50,'2023-09-10','2023-10-01','일합니다','/059b088f-396d-438b-8172-6ef997674fa5_1.jpg','/397430e6-9aa0-4d5f-b1a6-ed5c4377af76_video',0,0,NULL,'2023-11-15 06:49:57','2023-11-15 06:49:56'),(51,21,NULL,51,'2023-09-10','2023-10-01',NULL,'/26617ed0-ff31-458c-855a-b44f4a24f8b1_39.jpg','/b3e620b6-5328-4976-8171-25ffa42cd945_video',0,0,NULL,'2023-11-15 06:58:43','2023-11-15 06:58:43'),(52,24,NULL,52,'2023-09-13','2023-10-04','포토샵 마스터하기','/0c75d76d-4c10-431c-ae61-6568a2ff0569_1.jpg','/70ffe76a-7029-4bc7-9751-66eb79a38617_video',0,0,NULL,'2023-11-15 07:18:02','2023-11-15 07:18:01'),(54,8,NULL,54,'2023-09-10','2023-10-01','헌책만들기 챌린지','/12b1cbc3-68bf-4fa3-8719-855313cef74d_KakaoTalk_20231116_090827182_03.jpg','/30f054b8-c0ec-4cad-acdf-d0f0c8b10954_video',0,0,NULL,'2023-11-16 08:42:12','2023-11-16 08:42:12');
/*!40000 ALTER TABLE `feeds` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hash_tag_options`
--

DROP TABLE IF EXISTS `hash_tag_options`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hash_tag_options` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `content` (`content`)
) ENGINE=InnoDB AUTO_INCREMENT=154 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hash_tag_options`
--

LOCK TABLES `hash_tag_options` WRITE;
/*!40000 ALTER TABLE `hash_tag_options` DISABLE KEYS */;
INSERT INTO `hash_tag_options` VALUES (79,'21Days'),(78,'21일'),(145,'book'),(80,'Daily'),(120,'gg'),(141,'LOL'),(82,'My_Working'),(138,'string'),(149,'test'),(81,'Today'),(106,'Watch'),(111,'Watch_Chalelnge'),(5,'강아지'),(34,'강아지는_귀여워'),(16,'개발'),(134,'개화'),(59,'건강'),(72,'고슴도치'),(36,'고양이는_귀여워'),(35,'고양이도_귀여워'),(61,'공부'),(97,'괜찮아_괜찮다고'),(131,'구이'),(64,'귀요미'),(94,'그림'),(13,'그림그리기'),(40,'금연'),(39,'금주'),(118,'기브투미'),(119,'기브투미허'),(95,'기상'),(9,'기상_미션'),(152,'김규리'),(129,'꽃'),(27,'꿈을_꾸자'),(26,'끈기'),(84,'나_잘하지'),(104,'나는_매일_먹어'),(116,'날좀'),(98,'눈_아파'),(38,'다시는'),(8,'다이어트'),(10,'달리기'),(37,'대쉬보드'),(65,'덤벨'),(33,'도료뇽'),(24,'도전'),(102,'도주'),(62,'독서'),(58,'돈까스'),(30,'돌아오는_길은_멀지만'),(88,'랩'),(87,'랩퍼'),(51,'럭비'),(142,'롤'),(140,'리그오브레전드'),(103,'맛있겠지'),(28,'미래는_온다'),(128,'민들레'),(70,'바리스타'),(89,'발음교정'),(127,'벌레'),(90,'복부운동'),(101,'비행기'),(45,'사이클링'),(153,'새_책'),(130,'생선'),(7,'생화'),(32,'선착순'),(14,'성장'),(67,'스케치'),(136,'스터디'),(19,'스트레스'),(66,'스트레칭'),(105,'시계'),(108,'시계_챌린지'),(107,'시계는_와치'),(57,'아침'),(117,'안아줘'),(22,'알람'),(54,'야경'),(18,'업무'),(41,'역도'),(71,'연예인'),(99,'영양제_내놔'),(63,'오늘도_똑똑이'),(49,'오늘도_질주'),(11,'오늘도_해낸다'),(68,'오드아이'),(113,'오운완'),(86,'오키도키'),(121,'와치'),(110,'와치챌린지'),(92,'완전_멋있음'),(1,'요리'),(25,'요리는_못해도'),(112,'운동'),(2,'음식'),(76,'이번_가을은'),(77,'이번_설날은'),(75,'이번_여름은'),(74,'이번엔_기필코'),(44,'인바디'),(23,'일찍_일어나는_새가'),(143,'자바의정석'),(12,'자신감'),(85,'자신감_뿜뿜'),(83,'잘했죠'),(56,'저녁'),(47,'전력_질주'),(46,'전력질주'),(55,'점심'),(4,'정신력_키우기'),(115,'좀더가까이'),(91,'진짜_멋있음'),(48,'질주'),(124,'집밥'),(125,'집밥_챌린지'),(146,'책'),(147,'챌린지'),(60,'체육'),(50,'축구'),(96,'출근'),(144,'취뽀'),(148,'친구'),(150,'카페인'),(151,'카페인_줄이기'),(69,'커피'),(31,'컴백'),(15,'코딩'),(123,'쿠킹'),(126,'키우기'),(137,'타임랩스'),(42,'테니스'),(73,'텔레토비'),(17,'포토샵'),(53,'풍경'),(43,'하키'),(93,'해볼래?'),(100,'해외'),(52,'핸드볼'),(114,'허쉬허쉬허쉬베이베'),(139,'헌_책'),(135,'헌책'),(109,'헤헤'),(29,'현재는_간다'),(133,'홍차'),(3,'화초'),(6,'화초_키우기'),(132,'회의');
/*!40000 ALTER TABLE `hash_tag_options` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `likes`
--

DROP TABLE IF EXISTS `likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `likes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) NOT NULL,
  `member_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_like_feed_id_from_feed_id_idx` (`feed_id`),
  KEY `fk_like_member_id_from_member_id_idx` (`member_id`),
  CONSTRAINT `fk_like_feed_id_from_feed_id` FOREIGN KEY (`feed_id`) REFERENCES `feeds` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_like_member_id_from_member_id` FOREIGN KEY (`member_id`) REFERENCES `members` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `likes`
--

LOCK TABLES `likes` WRITE;
/*!40000 ALTER TABLE `likes` DISABLE KEYS */;
INSERT INTO `likes` VALUES (75,36,40),(77,52,39),(78,51,39),(79,42,39),(81,41,40),(82,51,40),(83,24,40),(84,52,40),(85,50,40),(86,48,40),(87,54,40);
/*!40000 ALTER TABLE `likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `members`
--

DROP TABLE IF EXISTS `members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `members` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `profile_image_url` varchar(255) NOT NULL,
  `nickname` varchar(255) NOT NULL,
  `is_delete` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `members`
--

LOCK TABLES `members` WRITE;
/*!40000 ALTER TABLE `members` DISABLE KEYS */;
INSERT INTO `members` VALUES (8,'aeae2323@kakao.com','http://k.kakaocdn.net/dn/ba8EwB/btsrAQ5HxYP/yz7T8qhedmmMQAWKkur861/img_640x640.jpg','김규리',0),(9,'qpwvwwax@example.com','/test/profile1.jpg','바다구름',0),(10,'mozwtte@example.com','/test/profile2.jpg','다솜푸름',0),(11,'mljzxvhbvl@example.com','/test/profile3.jpg','달빛마루',0),(12,'faehhptidg@example.com','/test/profile4.jpg','여울무지개',0),(13,'xfwtjpdm@example.com','/test/profile5.jpg','늘봄달빛',0),(14,'wdekz@example.com','/test/profile6.jpg','라임오월',0),(15,'xeuihwa@example.com','/test/profile7.jpg','달빛여울',0),(16,'kdifuk@example.com','/test/profile8.jpg','번개별하',0),(17,'ybsjtnshsx@example.com','/test/profile9.jpg','라임물결',0),(18,'jduqdg@example.com','/test/profile10.jpg','마루하랑',0),(19,'itpdphfvwq@example.com','/test/profile11.jpg','꽃잎천둥',0),(20,'kgrlgm@example.com','/test/profile12.jpg','달빛산새',0),(21,'hgbyihcwb@example.com','/test/profile13.jpg','나래여울',0),(22,'zjayev@example.com','/test/profile14.jpg','햇살햇살',0),(23,'uvwwyz@example.com','/test/profile15.jpg','가람마루',0),(24,'ayuot@example.com','/test/profile16.jpg','햇살숲속',0),(25,'xsqlbaqiu@example.com','/test/profile17.jpg','희망라임',0),(26,'yycmix@example.com','/test/profile18.jpg','꽃잎라임',0),(27,'zkmyhsdo@example.com','/test/profile19.jpg','별하바다',0),(28,'usuqwzz@example.com','/test/profile20.jpg','마루별빛',0),(39,'spor1998@naver.com','http://k.kakaocdn.net/dn/Iuj9B/btslhdxlcBX/xlWBzMNMd35BcHGyF1O7k1/img_640x640.jpg','정희',0),(40,'tgx963@naver.com','http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg','신현탁',0),(41,'rudgns8285@naver.com','http://k.kakaocdn.net/dn/cftATs/btswPV6TdmP/b1lfO8heRGP7VUBxYZoRn1/img_640x640.jpg','경후니',0),(42,'cat10830@naver.com','http://k.kakaocdn.net/dn/b0cG5g/btsxtlk7Bdl/93B1MF9x9V5SVNTZfB6Y81/img_640x640.jpg','정인모',0),(43,'samsee@kakao.com','http://k.kakaocdn.net/dn/7reHE/btq54TAN2ok/9orKPkgUN5hbtUsFcrTb40/img_640x640.jpg','이승윤',0),(44,'u__nit@naver.com','http://k.kakaocdn.net/dn/D1nor/btsrCwZEDrO/2srFQ7GLzCDJwBKvnNoSKK/img_640x640.jpg','이동규',0),(45,'gktjdgh98@naver.com','http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg','하성호',0),(46,'jybin96@naver.com','http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg','정영빈',0);
/*!40000 ALTER TABLE `members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `snapshots`
--

DROP TABLE IF EXISTS `snapshots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `snapshots` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `challenge_id` bigint(20) NOT NULL,
  `image_url` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `fk_snapshots_from_challenge_id` (`challenge_id`),
  CONSTRAINT `fk_snapshots_from_challenge_id` FOREIGN KEY (`challenge_id`) REFERENCES `challenges` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1139 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `snapshots`
--

LOCK TABLES `snapshots` WRITE;
/*!40000 ALTER TABLE `snapshots` DISABLE KEYS */;
INSERT INTO `snapshots` VALUES (11,20,'/ec09f97d-4d25-4e27-8b80-e8a1cb28beae_blob','2023-11-13 06:04:04'),(12,20,'/a9e4e20c-3528-41fe-bbe7-9631ddaec2b4_blob','2023-11-13 06:04:23'),(13,20,'/f8a5a4a8-4219-487c-a089-04b2f7429719_blob','2023-11-13 06:05:33'),(326,58,'/4ec6354b-e6e3-4fdf-a57a-3c54c0ae323c_1.jpg','2023-11-14 05:27:43'),(327,58,'/dbd17053-1a5a-47f5-bb5a-586d9a810e89_2.jpg','2023-11-14 05:28:27'),(412,70,'/27a73329-ba8a-427b-affc-05067953f297_1_-_복사본.jpg','2023-11-15 03:15:39'),(413,70,'/22b08bc6-022b-4294-89f4-dd6e9a5c388f_19_-_복사본.jpg','2023-11-15 03:15:39'),(414,70,'/ae242b2f-c617-400c-a220-765723516c52_76_-_복사본.jpg','2023-11-15 03:15:39'),(415,70,'/961f077c-79c5-4b36-8fc6-4eb7a8d4e4bf_78_-_복사본.jpg','2023-11-15 03:15:39'),(416,70,'/f9885056-4e2b-46dc-a097-036ff5be016b_79_-_복사본.jpg','2023-11-15 03:15:39'),(417,70,'/23c124fe-69c2-4837-8eff-46681793e453_80_-_복사본.jpg','2023-11-15 03:15:40'),(418,70,'/1cf81b7a-2948-41a4-974b-9505720cadd5_89_-_복사본.jpg','2023-11-15 03:15:40'),(419,70,'/fd298b67-c25b-44ab-bb76-314b7f14b56d_96_-_복사본.jpg','2023-11-15 03:15:40'),(420,70,'/d62415a3-346c-4b4b-9860-e67331cbe447_69_-_복사본.jpg','2023-11-15 03:15:40'),(421,70,'/33a3ad54-d854-4097-9eb5-e3af7afbca01_56_-_복사본.jpg','2023-11-15 03:15:40'),(422,70,'/51755700-1871-4927-b042-a577335cdd75_61_-_복사본.jpg','2023-11-15 03:15:40'),(423,70,'/1c8e2f2b-b51e-42b3-b45a-a895047dd134_59_-_복사본.jpg','2023-11-15 03:15:40'),(424,70,'/e2560671-a31b-4caa-a99d-68a507932b1d_50_-_복사본.jpg','2023-11-15 03:15:40'),(425,70,'/4b57b243-e3af-4b49-9881-b42935148d53_43_-_복사본.jpg','2023-11-15 03:15:40'),(426,70,'/bfb8a8ea-cab3-4906-bd45-6ba9ea9807d5_7_-_복사본.jpg','2023-11-15 03:15:40'),(427,70,'/0d33f5c1-96cd-4e79-89a9-a6f57e06f565_18_-_복사본.jpg','2023-11-15 03:15:40'),(428,70,'/ccda30fb-9e1a-4d42-a533-0c5b22db5475_6_-_복사본.jpg','2023-11-15 03:15:40'),(429,70,'/86435f00-da04-445b-8c4a-98fc8ee01647_48_-_복사본.jpg','2023-11-15 03:15:40'),(430,70,'/9c662fc4-ed02-48ec-919f-7842928d0181_24_-_복사본.jpg','2023-11-15 03:15:40'),(431,70,'/750d6dc5-9549-4c46-acfd-1b7417f59ea0_53_-_복사본.jpg','2023-11-15 03:15:40'),(432,70,'/bef1c70c-5635-41c7-a5b8-82b044c9fb74_62_-_복사본.jpg','2023-11-15 03:15:41'),(454,72,'/7d001a78-8e1d-4025-9d90-9dfbbaddf2a6_1.jpg','2023-11-15 03:38:20'),(455,72,'/a235b42e-1b64-43dd-b280-7b545eb5fb70_3.jpg','2023-11-15 03:38:20'),(456,72,'/b7a3b8f6-3545-4aef-ae5b-e466e2c61f8e_11.jpg','2023-11-15 03:38:20'),(457,72,'/9c76100b-737a-40de-899a-510f47a1b55e_14.jpg','2023-11-15 03:38:20'),(458,72,'/8a4dfadd-b9a7-4887-9c1a-6727650bda2d_28.jpg','2023-11-15 03:38:20'),(459,72,'/81e316e8-6f18-4e08-9984-3f4671d9c233_33.jpg','2023-11-15 03:38:20'),(460,72,'/1f08ad3e-03b4-4221-96f3-28600a496b3a_146.jpg','2023-11-15 03:38:20'),(461,72,'/51e23865-8f24-4f70-94be-5250c0e8715f_171.jpg','2023-11-15 03:38:20'),(462,72,'/977de3e9-5f10-4275-b6ef-d391fbe67abc_230.jpg','2023-11-15 03:38:20'),(463,72,'/492efec0-0969-411b-94de-651d5122e53d_247.jpg','2023-11-15 03:38:20'),(464,72,'/cfdea2e4-8681-4880-8491-acbc21829717_256.jpg','2023-11-15 03:38:20'),(465,72,'/d677acbf-27b4-40f6-a5d4-bbd8316836ed_259.jpg','2023-11-15 03:38:20'),(466,72,'/b5573205-80ac-4af4-869c-a917fd96f719_268.jpg','2023-11-15 03:38:20'),(467,72,'/fd64e7a9-ce86-44b2-8060-3d4c924c61b6_284.jpg','2023-11-15 03:38:20'),(468,72,'/ac392da2-2266-4f38-8b1b-5082eb69a4e0_290.jpg','2023-11-15 03:38:20'),(469,72,'/a7dfa465-2064-4bc8-a1f7-70ae40826c6c_286.jpg','2023-11-15 03:38:20'),(470,72,'/1b681d3f-f1bf-492e-a338-3e4146ddfcf5_310.jpg','2023-11-15 03:38:20'),(471,72,'/a76173d9-2b7a-4eac-bbff-b0c36df97943_320.jpg','2023-11-15 03:38:20'),(472,72,'/8fcc6041-39b6-436a-bc08-568917f9624d_334.jpg','2023-11-15 03:38:21'),(473,72,'/28f49c4d-8d5f-425a-8921-3c61fc8508e2_345.jpg','2023-11-15 03:38:21'),(474,72,'/8d3f7225-1cfe-4eb9-ac04-be4f5a91b511_352.jpg','2023-11-15 03:38:21'),(475,73,'/f8238ff8-83ea-4acd-94bd-98d19370ad6f_1.jpg','2023-11-15 03:42:36'),(476,73,'/ce469851-4b26-4e9d-b6b2-d8eba444c7cd_2.jpg','2023-11-15 03:42:36'),(477,73,'/d5425849-dfec-4b26-a139-b67a9e8dcd74_3.jpg','2023-11-15 03:42:36'),(478,73,'/457c4ede-92ab-49f4-a7c6-db445f833acf_7.jpg','2023-11-15 03:42:36'),(479,73,'/0581ea50-777a-4917-8603-5e78562c5f2a_8.jpg','2023-11-15 03:42:36'),(480,73,'/5312234a-ec83-497c-bd21-524c00fc30a6_14.jpg','2023-11-15 03:42:36'),(481,73,'/10919a7e-23bb-48c8-969c-363bbcc2af44_17.jpg','2023-11-15 03:42:36'),(482,73,'/2a52df0c-459a-4918-bcee-5732d5c87be2_18.jpg','2023-11-15 03:42:36'),(483,73,'/776ab963-4b76-489f-8e7f-ab2f230dc9dd_21.jpg','2023-11-15 03:42:36'),(484,73,'/fb58fc46-8663-4ac5-8e80-9ec0feae8678_41.jpg','2023-11-15 03:42:36'),(485,73,'/1e992852-b946-43f8-9812-edb2437f7b3c_34.jpg','2023-11-15 03:42:36'),(486,73,'/41f881ca-ee31-4ac6-976c-418269e06ddc_50_-_복사본.jpg','2023-11-15 03:42:36'),(487,73,'/995ee905-83df-4b63-a3ab-e1b4b7a866e9_67.jpg','2023-11-15 03:42:36'),(488,73,'/d4fbbf94-6dcc-43a8-a468-644ba96bd33d_68.jpg','2023-11-15 03:42:36'),(489,73,'/c5785d3a-7e83-4807-86cb-68aa23d5fbac_51.jpg','2023-11-15 03:42:36'),(490,73,'/f604ce1d-8932-45c8-8cc3-264cb802ecb8_50.jpg','2023-11-15 03:42:37'),(491,73,'/17b3a8f0-81c6-423f-9c1c-d11daaa11245_120.jpg','2023-11-15 03:42:37'),(492,73,'/f2190a4c-a24d-45b7-b6b8-798a4e657c0c_133.jpg','2023-11-15 03:42:37'),(493,73,'/c45949da-2fe5-4330-8c31-65f6998b107b_134.jpg','2023-11-15 03:42:37'),(494,73,'/d4a1743c-ff1e-4516-aef4-9baa5422f45b_113.jpg','2023-11-15 03:42:37'),(495,73,'/1d386489-f8f0-4f8f-9df3-a42efa3f2d2e_19.jpg','2023-11-15 03:42:37'),(559,40,'/5e6d69c9-4428-47f6-94b6-5bfd6b819a42_blob','2023-11-15 06:18:20'),(560,77,'/17f2bf42-8622-421b-b5c2-7905d17cf810_1_-_복사본.jpg','2023-11-15 06:25:14'),(561,77,'/c1692d85-8e24-4e30-be40-315a373d2208_3_-_복사본.jpg','2023-11-15 06:25:14'),(562,77,'/a06340d9-8eae-44db-b0d4-45b9717f08e2_6_-_복사본.jpg','2023-11-15 06:25:14'),(563,77,'/92d16256-fa80-40d2-b878-311704f1a14f_13_-_복사본.jpg','2023-11-15 06:25:14'),(564,77,'/782351c4-1edd-4b08-915b-e70cbc113424_15_-_복사본.jpg','2023-11-15 06:25:14'),(565,77,'/770c95da-cf2f-47ad-8dbf-7c13ffa5c8a2_18_-_복사본.jpg','2023-11-15 06:25:14'),(566,77,'/7521ee86-358f-4a9e-a17e-954308f64342_25_-_복사본.jpg','2023-11-15 06:25:14'),(567,77,'/78aa00e3-f63a-4c6c-9158-9c923d72f03f_43_-_복사본.jpg','2023-11-15 06:25:14'),(568,77,'/3073f2ce-246a-4389-bf0e-bd80f3899cf1_45_-_복사본.jpg','2023-11-15 06:25:14'),(569,77,'/d89d8a7d-2bd8-4d88-9e44-c267f4935523_48_-_복사본.jpg','2023-11-15 06:25:14'),(570,77,'/e7689110-74e3-4297-b3df-2f1e6b23b55b_55_-_복사본.jpg','2023-11-15 06:25:14'),(571,77,'/181c9109-2ae4-4beb-928f-4e0e0d95dd18_57_-_복사본.jpg','2023-11-15 06:25:14'),(572,77,'/f37d16b8-2cfa-47ca-a1aa-064df4e5e255_60_-_복사본.jpg','2023-11-15 06:25:14'),(573,77,'/b16357e4-5d76-48f4-a555-0ab26c47c875_63_-_복사본.jpg','2023-11-15 06:25:14'),(574,77,'/227e2958-cc2e-4b15-90dc-d2fdde0e7243_61_-_복사본.jpg','2023-11-15 06:25:15'),(575,77,'/76363f39-ef67-4dc2-9e14-5d6cf448db23_66_-_복사본.jpg','2023-11-15 06:25:15'),(576,77,'/962ee446-66c1-405e-9a7e-917a9f6fd5b0_73.jpg','2023-11-15 06:25:15'),(577,77,'/b691bd8c-c4ce-402e-b8d3-907dab180a56_76.jpg','2023-11-15 06:25:15'),(578,77,'/c6659275-fe2d-4d25-ac53-c2bf4958f601_77.jpg','2023-11-15 06:25:15'),(579,77,'/33a901af-47ac-4876-999c-80aa63db3553_30_-_복사본.jpg','2023-11-15 06:25:15'),(580,77,'/3669c786-c79d-41e9-b2fc-1c8fb6915516_27_-_복사본.jpg','2023-11-15 06:25:15'),(581,78,'/b8b5cb48-532c-4a48-8f89-33eb76f4b3b7_1_-_복사본.jpg','2023-11-15 06:27:25'),(582,78,'/eec3918f-dfb0-441b-b10c-02dae1c11498_3_-_복사본.jpg','2023-11-15 06:27:25'),(583,78,'/10956771-9d79-4a84-9c47-2175d9c01e08_6_-_복사본.jpg','2023-11-15 06:27:25'),(584,78,'/4e745bf1-8c3a-47c7-a78c-6c92a4870b87_13_-_복사본.jpg','2023-11-15 06:27:25'),(585,78,'/37c2a437-9d05-44d7-b9c0-ce653f87561b_15_-_복사본.jpg','2023-11-15 06:27:25'),(586,78,'/542e69d9-dc16-44e3-be64-60a3359243c9_18_-_복사본.jpg','2023-11-15 06:27:25'),(587,78,'/ba1d764c-d4e7-446f-8bef-309e38c6cc5a_25_-_복사본.jpg','2023-11-15 06:27:25'),(588,78,'/a2c4e02f-c45e-4b8b-9d00-adaf6c2110a0_43_-_복사본.jpg','2023-11-15 06:27:25'),(589,78,'/d91c0619-3f59-444f-91e1-2ebf31d1e998_45_-_복사본.jpg','2023-11-15 06:27:25'),(590,78,'/7e1b92d7-e4be-4db3-8f11-17b3fe3e50e3_48_-_복사본.jpg','2023-11-15 06:27:25'),(591,78,'/daea8812-fa15-427c-80c3-b31c3ad439fa_55_-_복사본.jpg','2023-11-15 06:27:26'),(592,78,'/c3e6a362-08c6-4ba9-a534-2382516a07a9_57_-_복사본.jpg','2023-11-15 06:27:26'),(593,78,'/700ef94e-0c59-4d39-9cab-ac258933772d_60_-_복사본.jpg','2023-11-15 06:27:26'),(594,78,'/edb3d840-5c78-4be4-8fd1-3443fdcb7c03_63_-_복사본.jpg','2023-11-15 06:27:26'),(595,78,'/a54888b9-3ff1-4a9a-abb7-1bcc1ce75ced_61_-_복사본.jpg','2023-11-15 06:27:26'),(596,78,'/bcf892bf-6fa7-423d-9375-d2d691b80769_66_-_복사본.jpg','2023-11-15 06:27:26'),(597,78,'/dd92d65e-d4e6-4ff1-b2b6-7c38292de612_73.jpg','2023-11-15 06:27:26'),(598,78,'/b1806112-4abd-4d29-9219-7acf00fefd54_76.jpg','2023-11-15 06:27:26'),(599,78,'/78b5275f-bc9c-4c68-a39e-c80f8fe3291a_77.jpg','2023-11-15 06:27:26'),(600,78,'/7e22254e-0f38-4bca-83af-a1632758ac2e_30_-_복사본.jpg','2023-11-15 06:27:26'),(601,78,'/6a49707d-aad1-42cc-88b7-5f7f09280606_27_-_복사본.jpg','2023-11-15 06:27:26'),(707,65,'/cfd92b0b-9fd0-4b01-9bff-968ee9e2b708_blob','2023-11-15 07:20:03'),(969,106,'/5b87e516-fbc5-4f55-8762-a57576b5ea11_KakaoTalk_20231116_090827182.jpg','2023-11-16 07:51:48'),(970,106,'/166fb955-783d-414e-92d7-1f5b3f761033_KakaoTalk_20231116_090827182_03.jpg','2023-11-16 07:51:48'),(971,106,'/9f8674fb-7f50-4e58-9401-52a5b03a5b07_KakaoTalk_20231116_090827182_05.jpg','2023-11-16 07:51:48'),(972,106,'/b915615c-3e36-4da4-a9ee-a0519a24ab84_KakaoTalk_20231116_090827182_06.jpg','2023-11-16 07:51:48'),(973,106,'/7e46f87f-5ca3-4fb4-88b5-77c4491c4aea_KakaoTalk_20231116_090827182_07.jpg','2023-11-16 07:51:48'),(974,106,'/850c06a2-d7d1-4387-b435-fb8aaad2f66b_KakaoTalk_20231116_090827182_09.jpg','2023-11-16 07:51:48'),(975,106,'/23fb9102-fdc1-4346-96ff-57f65c645020_KakaoTalk_20231116_090827182_11.jpg','2023-11-16 07:51:48'),(976,106,'/0015617f-a2cc-4d4c-9df4-c82c8488d420_KakaoTalk_20231116_090827182_12.jpg','2023-11-16 07:51:48'),(977,106,'/902817aa-a6cc-4a63-ba59-6e054d9d19db_KakaoTalk_20231116_090827182_10.jpg','2023-11-16 07:51:48'),(978,106,'/c4857aa6-dedd-41f9-96b4-d875b63f16e0_KakaoTalk_20231116_090827182_13.jpg','2023-11-16 07:51:48'),(979,106,'/a783e858-d5b5-4863-9e07-809fa8409ee9_KakaoTalk_20231116_090827182_14.jpg','2023-11-16 07:51:48'),(980,106,'/ba8d1f9e-ccc2-4112-a416-9b855f329614_KakaoTalk_20231116_090827182_15.jpg','2023-11-16 07:51:48'),(981,106,'/82589e61-4c94-4624-98b0-e72c00a20d21_KakaoTalk_20231116_090827182_16.jpg','2023-11-16 07:51:49'),(982,106,'/08a78186-2eff-47b2-9196-c4a0feae4f71_KakaoTalk_20231116_090827182_19.jpg','2023-11-16 07:51:49'),(983,106,'/909f5f9c-14c0-408e-b2ed-a59c9a824a78_KakaoTalk_20231116_090827182_21.jpg','2023-11-16 07:51:49'),(984,106,'/119a92c2-e256-4a80-8233-0b07d3dfc11f_KakaoTalk_20231116_090827182_18.jpg','2023-11-16 07:51:49'),(985,106,'/06b621b2-e341-4ab3-862b-29244b7b0da3_KakaoTalk_20231116_090827182_17.jpg','2023-11-16 07:51:49'),(986,106,'/1b140c9c-46d0-469e-b31a-208dbaeb25ae_KakaoTalk_20231116_090827182_20.jpg','2023-11-16 07:51:49'),(987,106,'/00877ea6-f658-4d93-8ee9-1dd9c45b9f20_KakaoTalk_20231116_090827182_22.jpg','2023-11-16 07:51:49'),(988,106,'/f84c2720-5761-4870-831f-07d51877ea66_KakaoTalk_20231116_090827182_24.jpg','2023-11-16 07:51:49'),(989,106,'/76db125b-04da-40ba-bda3-d8da8963c048_KakaoTalk_20231116_090827182_23.jpg','2023-11-16 07:51:49'),(1074,65,'/6586d628-856e-48df-87f9-12cec8c64541_blob','2023-11-16 08:31:59'),(1075,59,'/23da851a-d38b-44de-badb-df9374b73d08_blob','2023-11-16 08:37:22');
/*!40000 ALTER TABLE `snapshots` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'timing'
--
/*!50003 DROP FUNCTION IF EXISTS `CalculateFeedScore` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`59.20.195.127` FUNCTION `CalculateFeedScore`(member_id INT, feed_id BIGINT) RETURNS double
BEGIN
    DECLARE score DOUBLE DEFAULT 0;
    DECLARE today DATE DEFAULT CURRENT_DATE();
    DECLARE days_diff INT;
    
    -- 1. 내가 생성한 피드의 해시태그와 같은게 있으면 +
    IF EXISTS (SELECT 1 FROM feed_hash_tags WHERE feed_id = feed_id AND hash_tag_option_id IN
               (SELECT hash_tag_option_id FROM feed_hash_tags WHERE feed_id IN
               (SELECT id FROM feeds WHERE member_id = member_id))) THEN
        SET score = score + 2;
    END IF;

    -- 2. 내가 생성한 Challenge와 해시태그가 같은게 있으면 +
    IF EXISTS (SELECT 1 FROM feed_hash_tags WHERE feed_id = feed_id AND hash_tag_option_id IN
               (SELECT hash_tag_option_id FROM challenge_hash_tags WHERE challenge_id IN
               (SELECT id FROM challenges WHERE member_id = member_id))) THEN
        SET score = score + 2;
    END IF;

    -- 3. 내가 좋아요 한 피드들과 해시태그가 같은게 있으면 +
    IF EXISTS (SELECT 1 FROM feed_hash_tags WHERE feed_id = feed_id AND hash_tag_option_id IN
               (SELECT hash_tag_option_id FROM feed_hash_tags WHERE feed_id IN
               (SELECT feed_id FROM likes WHERE member_id = member_id))) THEN
        SET score = score + 2;
    END IF;

    -- 4. 내가 댓글 단 피드들과 해시태그가 같은게 있으면 +
    IF EXISTS (SELECT 1 FROM feed_hash_tags WHERE feed_id = feed_id AND hash_tag_option_id IN
               (SELECT hash_tag_option_id FROM feed_hash_tags WHERE feed_id IN
               (SELECT feed_id FROM comments WHERE member_id = member_id))) THEN
        SET score = score + 2;
    END IF;

    -- 5. 피드의 ended_at 컬럼이 오늘보다 이전이면 일당 -
    SELECT DATEDIFF(today, ended_at) INTO days_diff FROM feeds WHERE id = feed_id;
    IF days_diff > 0 THEN
        SET score = score - 0.5 * days_diff;
    END IF;

    RETURN score;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-16 17:49:55
