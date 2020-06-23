-- MySQL dump 10.13  Distrib 5.7.30, for Linux (x86_64)
--
-- Host: localhost    Database: sso_oauth2
-- ------------------------------------------------------
-- Server version	5.7.30-0ubuntu0.18.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `oauth2_authorization_code`
--

DROP TABLE IF EXISTS `oauth2_authorization_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oauth2_authorization_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(512) NOT NULL,
  `consumer_id` int(11) DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `state` varchar(10) DEFAULT NULL,
  `time_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `validity_period` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `consumer_id` (`consumer_id`),
  CONSTRAINT `oauth2_authorization_code_ibfk_1` FOREIGN KEY (`consumer_id`) REFERENCES `oauth2_consumer_apps` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oauth2_authorization_code`
--

LOCK TABLES `oauth2_authorization_code` WRITE;
/*!40000 ALTER TABLE `oauth2_authorization_code` DISABLE KEYS */;
INSERT INTO `oauth2_authorization_code` VALUES (1,'0751e80b-84e4-3515-8537-2762f89c7143',8,'xulygopybaoloi','ACTIVE','2020-05-13 03:12:46',30000),(2,'1743c2f3-4dc1-3842-b6c4-e0c107a1623b',8,'xulygopybaoloi','ACTIVE','2020-05-13 03:14:29',30000),(4,'1129dc2f-b64f-3614-8cc1-2de997a181e8',7,'xulygopybaoloi','ACTIVE','2020-05-13 06:20:13',30000),(5,'8f32a516-44aa-3818-b1a3-bc88fdee5165',8,'xulygopybaoloi','ACTIVE','2020-05-13 06:23:46',30000),(6,'8bbbd661-f3e5-3cbe-9b93-9319245d3206',8,'xulygopybaoloi','ACTIVE','2020-05-13 06:29:50',30000),(7,'f9192b00-74e2-3198-b8ba-b070d445c300',8,'xulygopybaoloi','ACTIVE','2020-05-13 06:32:01',30000),(8,'e91e3c18-9359-33e9-bacc-06799099ad60',8,'xulygopybaoloi','ACTIVE','2020-05-13 06:43:44',30000),(10,'bad2beeb-0e2a-3159-8f04-fdff9c361668',8,'xulygopybaoloi','ACTIVE','2020-05-13 06:55:55',30000),(11,'1844696c-2a12-3d73-aaf7-d3ff65150307',7,'xulygopybaoloi','ACTIVE','2020-06-18 10:47:17',30000),(12,'8be24495-0750-3c15-a084-de7a0291542d',7,'xulygopybaoloi','ACTIVE','2020-06-19 10:17:22',30000),(13,'ee972ba0-2c87-352a-98b0-8b6646a64ab3',7,'ntvnguyen.sgddt','ACTIVE','2020-06-20 03:56:43',30000),(14,'be2ea0f5-e4be-3bc7-9437-d4c5ac71b71e',7,'ntvnguyen.sgddt','ACTIVE','2020-06-20 03:58:35',30000),(17,'a6e97b92-cca1-3b7a-a335-cb7af7eebf24',7,'ntvnguyen.sgddt','ACTIVE','2020-06-20 04:13:43',30000),(20,'77ab7159-a540-38d0-a84e-8adbc2797a32',7,'ntvnguyen.sgddt','ACTIVE','2020-06-20 04:34:34',30000),(21,'4b424f7c-187f-393d-8366-8a11fc3ebc0b',7,'ntvnguyen.sgddt','ACTIVE','2020-06-20 04:41:07',30000),(22,'8e7a7610-71f1-3ef4-b416-9f40baeb2294',7,'ntvnguyen.sgddt','ACTIVE','2020-06-20 04:42:25',30000),(23,'ba1cae9c-3172-35d9-b3bb-56347b32cd6c',7,'ntvnguyen.sgddt','ACTIVE','2020-06-20 04:54:28',30000),(24,'22f743ab-f23d-37fe-9c70-dff22bc2c2b5',7,'ntvnguyen.sgddt','ACTIVE','2020-06-20 04:54:42',30000),(25,'78760476-ca9a-3ac9-9a34-f24434c58b06',7,'ntvnguyen.sgddt','ACTIVE','2020-06-20 04:54:42',30000);
/*!40000 ALTER TABLE `oauth2_authorization_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oauth2_authorization_token`
--

DROP TABLE IF EXISTS `oauth2_authorization_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oauth2_authorization_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `token` varchar(1024) NOT NULL,
  `consumer_id` int(11) DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `state` varchar(10) DEFAULT NULL,
  `time_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `token_expire_time` bigint(20) DEFAULT NULL,
  `refresh_token_expire_time` bigint(20) DEFAULT NULL,
  `access_token` varchar(512) DEFAULT NULL,
  `refresh_token` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `consumer_id` (`consumer_id`),
  CONSTRAINT `oauth2_authorization_token_ibfk_1` FOREIGN KEY (`consumer_id`) REFERENCES `oauth2_consumer_apps` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oauth2_authorization_token`
--

LOCK TABLES `oauth2_authorization_token` WRITE;
/*!40000 ALTER TABLE `oauth2_authorization_token` DISABLE KEYS */;
INSERT INTO `oauth2_authorization_token` VALUES (3,'19ccb55d-27c6-400e-8968-8f1e7ee1ddd1',7,'xulygopybaoloi','INACTIVE','2020-05-12 11:49:06',86400000,86400000,'d3834c89-fc85-3139-8b40-c6e0631b4548','9b803103-3cc8-3227-8fbc-ff4ba2638081'),(4,'eyJ0eXBlIjoiand0IiwiYWxnIjoiUlM1MTIifQ.eyJleHAiOjE1ODkzNzA3ODcsImlhdCI6MTU4OTI4NDM4NywidXNlcm5hbWUiOiIifQ.cW03Tgo7JOSLw0Ogs09AudPwAgQ4g7gVQYfnWPfkX3qmRnLz4dzhVhqrIxlJz5nRMImJtVKzmcYYhxNRJx-wmkpVinkqs0ke65hCowq7eIb15jqWqXAG0ANZklbLNfzb6Eh_d1LKC5LFa3eXeQVqyhAJm0DRfmB-nWZSyLZA_nZQ4kG5hszNn0_5l1LLkCyYNEECyqiHAGUE3TWk9JEmChh4ny7N-IXaPkbpMf4mPEUhdA7L4_QmJCltEjV8e7GsdkAhRo4c07fnmAr_XvbXtq_zuBMnPQYyO5uU8Ln0tj5IweSxp-hdktpf6JKIgy8FTx467YK22gYtvJko7rrKJQ',7,'','ACTIVE','2020-05-12 11:53:08',86400000,86400000,'bb95ef91-07ee-3e94-ba0d-fa9c94d1c294','71c34770-7be0-38a4-8b76-7736005f0bad'),(5,'eyJ0eXBlIjoiand0IiwiYWxnIjoiUlM1MTIifQ.eyJleHAiOjE1ODkzNzA4NTUsImlhdCI6MTU4OTI4NDQ1NSwidXNlcm5hbWUiOiIifQ.nrIZT6btuA9GmCBVtXj-Db3pKJ17fD7rJ15DeFBjCS1GjW_Ho_1hUjeqQLKRTZ5jJbp7mngOVqQyxhrbsi8epCxCK9gtiygvj-mnMNufih7rv-auyWrKE_csdX00t6-15w6wYAk2AcE16AcRpwJJEx4poIRDrsEdCBAfAdJIHxCCuSmvh6oIiaFbl2j43QUWr8wzR_cZRG19lPRPI0ZBg8o0fZ-YXrgO3Q3RGbD3aj6F_RdCRR6HvlHW10JYZQOYyzjZ_qiY-0T9tBQrerKUGMkbd-WyNtGnFTHcDN9imp2Itmyv5EBJ5yLWWr1mGvo2PWUOzr2Ck7fFP4y89_3jWA',7,'','ACTIVE','2020-05-12 11:54:15',86400000,86400000,'ffe22147-58e6-31d5-8baf-990a92170b13','6ffece1d-65cc-3204-84ae-a39c4c6693ae'),(6,'eyJ0eXBlIjoiand0IiwiYWxnIjoiUlM1MTIifQ.eyJjb21wYW55IjoiYmthdiIsImV4cCI6MTU4OTQyNjMwNywiaWF0IjoxNTg5MzM5OTA3LCJ1c2VybmFtZSI6Inh1bHlnb3B5YmFvbG9pIn0.o4wa-R1_FTHDRphOzR8SrzhCd_ji-lPrut8xy3ng8MOSmma3ZkU5vbmQge67psVekrqtbN8tYkodKmaQFIUmFCSRNtVmE4vcoD5zcY4W3zzQsueypsjB2Sb1VYAwIuS7-1SAkY6aQtQiOvdw5-GEefS-f9hD-eT9UkCbmTBEtlCZHqy1uT0RrBLEaCaAO4tHKcedNPH9tsEkXYxKGD4RpE7uoV6O27a8VDiwD0TjFoNedOV49IEZdiexgldybAL6BJFsuMLoJgo89Wxq5me66eZu51gJC4DyieJDhhv1lNfcpLghvDMgyAzvu45Td-f8Nbw974aplbuky9Fi6PCnxQ',8,'xulygopybaoloi','ACTIVE','2020-05-13 03:18:27',86400000,86400000,'94e946ff-398c-3c07-b9e0-6e5c2a7ea2eb','8f055362-5264-360c-b97a-5e421042f4ac'),(8,'eyJ0eXBlIjoiand0IiwiYWxnIjoiUlM1MTIifQ.eyJjb21wYW55IjoiYmthdiIsImV4cCI6MTU4OTQzODg2NywiaWF0IjoxNTg5MzUyNDY3LCJ1c2VybmFtZSI6Inh1bHlnb3B5YmFvbG9pIn0.TFoH3JpSku9QkWO9SgiiImZWMXBav1NVTxCPZTsPnW4AeVOY_IKX1nrZdl5Q0cnkuAbVf60t9qAym8xIeDal2kRqjr8oBuu36aK2XduAgj3kgOjzGeICdXfWRGIcMcTY14dRyxZVCKf3vKaE87IPktVAgGbM2Gfaahtg84OSMaK6MGAJEA_RySoLf5i2KOelaolzAdc9c0ILaJC3cZlgVbcXBKdtUuK0JCg7ObO_-Ib68boz2lUxRmVl7dEaBdxHs7ashviAhpGkcNYqfdHpYcgWI60ZjwnM_2oeqzJ04rzt6-KhL9Qu2_Tm1brTPStmen0Q35r5ICobgUVF4ZVInw',8,'xulygopybaoloi','ACTIVE','2020-05-13 06:47:48',86400000,86400000,'049122a1-967a-3bcd-8d0a-cbcd24c9418b','bf9ebbfa-585f-3220-bfa6-936b508c0499'),(11,'eyJ0eXBlIjoiand0IiwiYWxnIjoiUlM1MTIifQ.eyJjb21wYW55IjoiYmthdiIsImV4cCI6MTU4OTQzOTQ5MCwiaWF0IjoxNTg5MzUzMDkwLCJ1c2VybmFtZSI6Inh1bHlnb3B5YmFvbG9pIn0.iBForoyrcwiesMSLxUDqEPigqNFY9uanqtr49q1k9XsFzzzbTpkboPyNN0xP97EJt_qwtNBuZOfortToJBirE0C-lOL0v6BH1GE-KRO7ClPfkK8SjUr2eHDMHSogWLqzCzSj9vVImleB-gTv670vgRP-MHLQvGLo-6RB2nU9AVcd0UUcQzPecK4cEeONfMU65oo15W4w4IUBMZKt_fe_iZBf3y1jwhh-n4UGyWjqzjZinjltTIdRnfUoVCdVEFzRDKdGN8Dr4TdnNjp4byAtaeIluJ4rIF3safORZ9ZpnXoxQ0Z36wC1yxRHKtf1P3LF91U4DlLMU2WOPA1HOrxUtQ',8,'xulygopybaoloi','ACTIVE','2020-05-13 06:58:11',86400000,86400000,'3f01c048-db2c-3f93-ab6e-dd771e2b14af','896f95aa-7171-35c4-97d1-f51bc4b583d1'),(13,'eyJ0eXBlIjoiand0IiwiYWxnIjoiUlM1MTIifQ.eyJJUCI6IkFwYWNoZS1IdHRwQXN5bmNDbGllbnQvNC4xLjQgKEphdmEvMS44LjBfMjUyKSIsIklEIjoiRWVkUmE5RVUyRWcrREsrOS9WWFNTNCtWRGI1ZWMyWE4iLCJleHAiOjE1OTI3MTMzODAsIlRpbWVTdGFtcHQiOiIyMDIwLTA2LTIwVDExOjE4OjMwLjE2NyIsImlzQWN0aXZlIjoidHJ1ZSIsImlhdCI6MTU5MjYyNjk4MCwiU2Nob29sSWQiOiI3OTAwMDAwMyIsIkFjY291bnRUeXBlIjoiUFZQIiwiU3lzdGVtVXNpbmciOiJJQ09TU08iLCJ1c2VybmFtZSI6Im50dm5ndXllbi5zZ2RkdCJ9.3O9YXsXHCcTYJQNr8pAr0MZ7GIS3HD2yyDsTzXvTuQ4f_ib-pmcC1AwpbiHYXlpGv6Zm-4bYoXP5h1RRwhWOAJwofRS8WLk5nNOCV1bVgvxGsQEDW3zEZbAq9ZiXb_DjZ-IJXc2fNWqkKJi_Ag6tkTY1P7i7aIuerBSj-QaaRHZzovbYoD8FKAoe30kQi6CLqHMmJzm3OtiOvwDwOjxMpZEuqzDKO-J6okW9AIE9zUvDN159zUjhQuHIq-S8x4jrcphHnUpkQysTOwzEOdof2IcDzZTf21u5RCDAP6eMnVxjkYBny6CiRI8VLz0Z_gzSsTB2FSi9XHlf_cR2bJYL3g',7,'ntvnguyen.sgddt','ACTIVE','2020-06-20 04:23:01',86400000,86400000,'3d274ca4-9849-306b-9407-70b5c2a3886d','20001161-a1a9-3360-9afa-20e21038ac52');
/*!40000 ALTER TABLE `oauth2_authorization_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oauth2_consumer_apps`
--

DROP TABLE IF EXISTS `oauth2_consumer_apps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oauth2_consumer_apps` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `consumer_key` varchar(512) NOT NULL,
  `consumer_secret` varchar(512) NOT NULL,
  `app_name` varchar(100) DEFAULT NULL,
  `app_state` varchar(10) DEFAULT NULL,
  `callback_url` varchar(512) DEFAULT NULL,
  `token_expire_time` bigint(20) DEFAULT NULL,
  `refresh_token_expire_time` bigint(20) DEFAULT NULL,
  `description` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oauth2_consumer_apps`
--

LOCK TABLES `oauth2_consumer_apps` WRITE;
/*!40000 ALTER TABLE `oauth2_consumer_apps` DISABLE KEYS */;
INSERT INTO `oauth2_consumer_apps` VALUES (7,'C3hv2R69Tf1Vst73tzWkPH_3Q7oa','Cpffj4RRp66gPhkn5Qv06HAgBgca','Vala','ACTIVE','http://localhost:8080/callback',86400000,86400000,''),(8,'fWaVxz9ZK_IhHCTHkxJVeD57D1ka','ECViITfD8UnaXMyDq1Qw3trfibga','Egov','ACTIVE','regexp=http://localhost:8080/callback\\?ReturnURL=(.*)',86400000,86400000,'Egov');
/*!40000 ALTER TABLE `oauth2_consumer_apps` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-06-20 12:20:05