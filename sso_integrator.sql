/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50730
 Source Host           : localhost:3306
 Source Schema         : sso_integrator

 Target Server Type    : MySQL
 Target Server Version : 50730
 File Encoding         : 65001

 Date: 11/07/2020 09:49:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oauth2_authorization_code
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_authorization_code`;
CREATE TABLE `oauth2_authorization_code`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(512) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `consumer_id` int(11) NULL DEFAULT NULL,
  `username` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `state` varchar(10) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `time_created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `validity_period` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `consumer_id`(`consumer_id`) USING BTREE,
  CONSTRAINT `oauth2_authorization_code_ibfk_1` FOREIGN KEY (`consumer_id`) REFERENCES `oauth2_consumer_apps` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oauth2_authorization_code
-- ----------------------------
INSERT INTO `oauth2_authorization_code` VALUES (1, '0751e80b-84e4-3515-8537-2762f89c7143', 8, 'xulygopybaoloi', 'ACTIVE', '2020-05-13 10:12:46', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (2, '1743c2f3-4dc1-3842-b6c4-e0c107a1623b', 8, 'xulygopybaoloi', 'ACTIVE', '2020-05-13 10:14:29', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (4, '1129dc2f-b64f-3614-8cc1-2de997a181e8', 7, 'xulygopybaoloi', 'ACTIVE', '2020-05-13 13:20:13', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (5, '8f32a516-44aa-3818-b1a3-bc88fdee5165', 8, 'xulygopybaoloi', 'ACTIVE', '2020-05-13 13:23:46', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (6, '8bbbd661-f3e5-3cbe-9b93-9319245d3206', 8, 'xulygopybaoloi', 'ACTIVE', '2020-05-13 13:29:50', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (7, 'f9192b00-74e2-3198-b8ba-b070d445c300', 8, 'xulygopybaoloi', 'ACTIVE', '2020-05-13 13:32:01', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (8, 'e91e3c18-9359-33e9-bacc-06799099ad60', 8, 'xulygopybaoloi', 'ACTIVE', '2020-05-13 13:43:44', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (10, 'bad2beeb-0e2a-3159-8f04-fdff9c361668', 8, 'xulygopybaoloi', 'ACTIVE', '2020-05-13 13:55:55', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (11, '1844696c-2a12-3d73-aaf7-d3ff65150307', 7, 'xulygopybaoloi', 'ACTIVE', '2020-06-18 17:47:17', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (12, '8be24495-0750-3c15-a084-de7a0291542d', 7, 'xulygopybaoloi', 'ACTIVE', '2020-06-19 17:17:22', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (13, 'ee972ba0-2c87-352a-98b0-8b6646a64ab3', 7, 'ntvnguyen.sgddt', 'ACTIVE', '2020-06-20 10:56:43', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (14, 'be2ea0f5-e4be-3bc7-9437-d4c5ac71b71e', 7, 'ntvnguyen.sgddt', 'ACTIVE', '2020-06-20 10:58:35', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (17, 'a6e97b92-cca1-3b7a-a335-cb7af7eebf24', 7, 'ntvnguyen.sgddt', 'ACTIVE', '2020-06-20 11:13:43', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (20, '77ab7159-a540-38d0-a84e-8adbc2797a32', 7, 'ntvnguyen.sgddt', 'ACTIVE', '2020-06-20 11:34:34', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (21, '4b424f7c-187f-393d-8366-8a11fc3ebc0b', 7, 'ntvnguyen.sgddt', 'ACTIVE', '2020-06-20 11:41:07', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (22, '8e7a7610-71f1-3ef4-b416-9f40baeb2294', 7, 'ntvnguyen.sgddt', 'ACTIVE', '2020-06-20 11:42:25', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (23, 'ba1cae9c-3172-35d9-b3bb-56347b32cd6c', 7, 'ntvnguyen.sgddt', 'ACTIVE', '2020-06-20 11:54:28', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (24, '22f743ab-f23d-37fe-9c70-dff22bc2c2b5', 7, 'ntvnguyen.sgddt', 'ACTIVE', '2020-06-20 11:54:42', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (25, '78760476-ca9a-3ac9-9a34-f24434c58b06', 7, 'ntvnguyen.sgddt', 'ACTIVE', '2020-06-20 11:54:42', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (26, '35584362-74d3-35ef-98eb-55f57169b2b8', 7, 'ntvnguyen.sgddt', 'ACTIVE', '2020-06-22 10:27:43', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (27, '623303a4-0e31-37ba-97c3-a433b8a53902', 7, 'ntvnguyen.sgddt', 'ACTIVE', '2020-06-22 10:31:39', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (28, '3f51bb67-40b4-35fd-b24b-265d4cacbb06', 7, 'ntvnguyen.sgddt', 'ACTIVE', '2020-06-22 10:50:05', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (29, 'b684bf51-9eea-3396-9568-3ab903e81efc', 7, 'ntvnguyen.sgddt', 'ACTIVE', '2020-06-22 10:51:50', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (30, '7bdd3b15-87ef-3c43-9962-273c641e3cde', 7, 'ntvnguyen.sgddt', 'ACTIVE', '2020-06-22 10:53:25', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (31, '332bfe11-147b-336f-96d6-c162500f85be', 7, 'ntvnguyen.sgddt', 'ACTIVE', '2020-06-22 10:54:49', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (32, '3af5668f-cae2-3498-a2d2-9385363b721f', 7, 'ntvnguyen.sgddt', 'ACTIVE', '2020-06-22 11:09:15', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (33, '3d702072-5720-3e1a-a109-6dcdd7e7a669', 7, 'ntvnguyen.sgddt', 'ACTIVE', '2020-06-22 15:46:41', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (34, 'fa1812bd-c0c0-3ee3-b337-36e658ce50aa', 7, 'ntvnguyen.sgddt', 'ACTIVE', '2020-06-22 15:48:27', 30000);
INSERT INTO `oauth2_authorization_code` VALUES (35, 'aa15113c-522e-3791-a3a8-d35e1ac9a04e', 7, 'admin_ioc', 'ACTIVE', '2020-07-03 18:35:07', 100000000);

-- ----------------------------
-- Table structure for oauth2_authorization_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_authorization_token`;
CREATE TABLE `oauth2_authorization_token`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `token` varchar(1024) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `consumer_id` int(11) NULL DEFAULT NULL,
  `username` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `state` varchar(10) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `time_created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `token_expire_time` bigint(20) NULL DEFAULT NULL,
  `refresh_token_expire_time` bigint(20) NULL DEFAULT NULL,
  `access_token` varchar(512) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `refresh_token` varchar(512) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `consumer_id`(`consumer_id`) USING BTREE,
  CONSTRAINT `oauth2_authorization_token_ibfk_1` FOREIGN KEY (`consumer_id`) REFERENCES `oauth2_consumer_apps` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oauth2_authorization_token
-- ----------------------------
INSERT INTO `oauth2_authorization_token` VALUES (3, '19ccb55d-27c6-400e-8968-8f1e7ee1ddd1', 7, 'xulygopybaoloi', 'INACTIVE', '2020-05-12 18:49:06', 86400000, 86400000, 'd3834c89-fc85-3139-8b40-c6e0631b4548', '9b803103-3cc8-3227-8fbc-ff4ba2638081');
INSERT INTO `oauth2_authorization_token` VALUES (4, 'eyJ0eXBlIjoiand0IiwiYWxnIjoiUlM1MTIifQ.eyJleHAiOjE1ODkzNzA3ODcsImlhdCI6MTU4OTI4NDM4NywidXNlcm5hbWUiOiIifQ.cW03Tgo7JOSLw0Ogs09AudPwAgQ4g7gVQYfnWPfkX3qmRnLz4dzhVhqrIxlJz5nRMImJtVKzmcYYhxNRJx-wmkpVinkqs0ke65hCowq7eIb15jqWqXAG0ANZklbLNfzb6Eh_d1LKC5LFa3eXeQVqyhAJm0DRfmB-nWZSyLZA_nZQ4kG5hszNn0_5l1LLkCyYNEECyqiHAGUE3TWk9JEmChh4ny7N-IXaPkbpMf4mPEUhdA7L4_QmJCltEjV8e7GsdkAhRo4c07fnmAr_XvbXtq_zuBMnPQYyO5uU8Ln0tj5IweSxp-hdktpf6JKIgy8FTx467YK22gYtvJko7rrKJQ', 7, '', 'ACTIVE', '2020-05-12 18:53:08', 86400000, 86400000, 'bb95ef91-07ee-3e94-ba0d-fa9c94d1c294', '71c34770-7be0-38a4-8b76-7736005f0bad');
INSERT INTO `oauth2_authorization_token` VALUES (5, 'eyJ0eXBlIjoiand0IiwiYWxnIjoiUlM1MTIifQ.eyJleHAiOjE1ODkzNzA4NTUsImlhdCI6MTU4OTI4NDQ1NSwidXNlcm5hbWUiOiIifQ.nrIZT6btuA9GmCBVtXj-Db3pKJ17fD7rJ15DeFBjCS1GjW_Ho_1hUjeqQLKRTZ5jJbp7mngOVqQyxhrbsi8epCxCK9gtiygvj-mnMNufih7rv-auyWrKE_csdX00t6-15w6wYAk2AcE16AcRpwJJEx4poIRDrsEdCBAfAdJIHxCCuSmvh6oIiaFbl2j43QUWr8wzR_cZRG19lPRPI0ZBg8o0fZ-YXrgO3Q3RGbD3aj6F_RdCRR6HvlHW10JYZQOYyzjZ_qiY-0T9tBQrerKUGMkbd-WyNtGnFTHcDN9imp2Itmyv5EBJ5yLWWr1mGvo2PWUOzr2Ck7fFP4y89_3jWA', 7, '', 'ACTIVE', '2020-05-12 18:54:15', 86400000, 86400000, 'ffe22147-58e6-31d5-8baf-990a92170b13', '6ffece1d-65cc-3204-84ae-a39c4c6693ae');
INSERT INTO `oauth2_authorization_token` VALUES (6, 'eyJ0eXBlIjoiand0IiwiYWxnIjoiUlM1MTIifQ.eyJjb21wYW55IjoiYmthdiIsImV4cCI6MTU4OTQyNjMwNywiaWF0IjoxNTg5MzM5OTA3LCJ1c2VybmFtZSI6Inh1bHlnb3B5YmFvbG9pIn0.o4wa-R1_FTHDRphOzR8SrzhCd_ji-lPrut8xy3ng8MOSmma3ZkU5vbmQge67psVekrqtbN8tYkodKmaQFIUmFCSRNtVmE4vcoD5zcY4W3zzQsueypsjB2Sb1VYAwIuS7-1SAkY6aQtQiOvdw5-GEefS-f9hD-eT9UkCbmTBEtlCZHqy1uT0RrBLEaCaAO4tHKcedNPH9tsEkXYxKGD4RpE7uoV6O27a8VDiwD0TjFoNedOV49IEZdiexgldybAL6BJFsuMLoJgo89Wxq5me66eZu51gJC4DyieJDhhv1lNfcpLghvDMgyAzvu45Td-f8Nbw974aplbuky9Fi6PCnxQ', 8, 'xulygopybaoloi', 'ACTIVE', '2020-05-13 10:18:27', 86400000, 86400000, '94e946ff-398c-3c07-b9e0-6e5c2a7ea2eb', '8f055362-5264-360c-b97a-5e421042f4ac');
INSERT INTO `oauth2_authorization_token` VALUES (8, 'eyJ0eXBlIjoiand0IiwiYWxnIjoiUlM1MTIifQ.eyJjb21wYW55IjoiYmthdiIsImV4cCI6MTU4OTQzODg2NywiaWF0IjoxNTg5MzUyNDY3LCJ1c2VybmFtZSI6Inh1bHlnb3B5YmFvbG9pIn0.TFoH3JpSku9QkWO9SgiiImZWMXBav1NVTxCPZTsPnW4AeVOY_IKX1nrZdl5Q0cnkuAbVf60t9qAym8xIeDal2kRqjr8oBuu36aK2XduAgj3kgOjzGeICdXfWRGIcMcTY14dRyxZVCKf3vKaE87IPktVAgGbM2Gfaahtg84OSMaK6MGAJEA_RySoLf5i2KOelaolzAdc9c0ILaJC3cZlgVbcXBKdtUuK0JCg7ObO_-Ib68boz2lUxRmVl7dEaBdxHs7ashviAhpGkcNYqfdHpYcgWI60ZjwnM_2oeqzJ04rzt6-KhL9Qu2_Tm1brTPStmen0Q35r5ICobgUVF4ZVInw', 8, 'xulygopybaoloi', 'ACTIVE', '2020-05-13 13:47:48', 86400000, 86400000, '049122a1-967a-3bcd-8d0a-cbcd24c9418b', 'bf9ebbfa-585f-3220-bfa6-936b508c0499');
INSERT INTO `oauth2_authorization_token` VALUES (11, 'eyJ0eXBlIjoiand0IiwiYWxnIjoiUlM1MTIifQ.eyJjb21wYW55IjoiYmthdiIsImV4cCI6MTU4OTQzOTQ5MCwiaWF0IjoxNTg5MzUzMDkwLCJ1c2VybmFtZSI6Inh1bHlnb3B5YmFvbG9pIn0.iBForoyrcwiesMSLxUDqEPigqNFY9uanqtr49q1k9XsFzzzbTpkboPyNN0xP97EJt_qwtNBuZOfortToJBirE0C-lOL0v6BH1GE-KRO7ClPfkK8SjUr2eHDMHSogWLqzCzSj9vVImleB-gTv670vgRP-MHLQvGLo-6RB2nU9AVcd0UUcQzPecK4cEeONfMU65oo15W4w4IUBMZKt_fe_iZBf3y1jwhh-n4UGyWjqzjZinjltTIdRnfUoVCdVEFzRDKdGN8Dr4TdnNjp4byAtaeIluJ4rIF3safORZ9ZpnXoxQ0Z36wC1yxRHKtf1P3LF91U4DlLMU2WOPA1HOrxUtQ', 8, 'xulygopybaoloi', 'ACTIVE', '2020-05-13 13:58:11', 86400000, 86400000, '3f01c048-db2c-3f93-ab6e-dd771e2b14af', '896f95aa-7171-35c4-97d1-f51bc4b583d1');
INSERT INTO `oauth2_authorization_token` VALUES (13, 'eyJ0eXBlIjoiand0IiwiYWxnIjoiUlM1MTIifQ.eyJJUCI6IkFwYWNoZS1IdHRwQXN5bmNDbGllbnQvNC4xLjQgKEphdmEvMS44LjBfMjUyKSIsIklEIjoiRWVkUmE5RVUyRWcrREsrOS9WWFNTNCtWRGI1ZWMyWE4iLCJleHAiOjE1OTI3MTMzODAsIlRpbWVTdGFtcHQiOiIyMDIwLTA2LTIwVDExOjE4OjMwLjE2NyIsImlzQWN0aXZlIjoidHJ1ZSIsImlhdCI6MTU5MjYyNjk4MCwiU2Nob29sSWQiOiI3OTAwMDAwMyIsIkFjY291bnRUeXBlIjoiUFZQIiwiU3lzdGVtVXNpbmciOiJJQ09TU08iLCJ1c2VybmFtZSI6Im50dm5ndXllbi5zZ2RkdCJ9.3O9YXsXHCcTYJQNr8pAr0MZ7GIS3HD2yyDsTzXvTuQ4f_ib-pmcC1AwpbiHYXlpGv6Zm-4bYoXP5h1RRwhWOAJwofRS8WLk5nNOCV1bVgvxGsQEDW3zEZbAq9ZiXb_DjZ-IJXc2fNWqkKJi_Ag6tkTY1P7i7aIuerBSj-QaaRHZzovbYoD8FKAoe30kQi6CLqHMmJzm3OtiOvwDwOjxMpZEuqzDKO-J6okW9AIE9zUvDN159zUjhQuHIq-S8x4jrcphHnUpkQysTOwzEOdof2IcDzZTf21u5RCDAP6eMnVxjkYBny6CiRI8VLz0Z_gzSsTB2FSi9XHlf_cR2bJYL3g', 7, 'ntvnguyen.sgddt', 'ACTIVE', '2020-06-20 11:23:01', 86400000, 86400000, '3d274ca4-9849-306b-9407-70b5c2a3886d', '20001161-a1a9-3360-9afa-20e21038ac52');
INSERT INTO `oauth2_authorization_token` VALUES (14, 'eyJ0eXBlIjoiand0IiwiYWxnIjoiUlM1MTIifQ.eyJzdWIiOiJudHZuZ3V5ZW4uc2dkZHQiLCJJUCI6IkFwYWNoZS1IdHRwQXN5bmNDbGllbnQvNC4xLjQgKEphdmEvMS44LjBfMjUyKSIsIklEIjoicnB4eEpKc1cyRWdYU0krTXdDN25SSmc0K0VMTkFBZGEiLCJleHAiOjE1OTI5MDk5MTMsIlRpbWVTdGFtcHQiOiIyMDIwLTA2LTIyVDE3OjU1OjAyLjQ3MyIsImlzQWN0aXZlIjoidHJ1ZSIsImlhdCI6MTU5MjgyMzUxMywiU2Nob29sSWQiOiI3OTAwMDAwMyIsIkFjY291bnRUeXBlIjoiUFZQIiwiU3lzdGVtVXNpbmciOiI2In0.hYGH5UHX0gm5O7OB8OyVx53lnkfS1CU20cPTtbcOZUZsP1Cie23pzuXvDuo_KgYjnh1gSTxFXXq70g5sgGqhNGCvGO9vHJbdP_ERMUoGYMnVYJ6QsraHcT6iqdo4WQAz_7UJTCZLLAW1s0rsu8KC1yh8bhCi3QRaF1TyPqQ37nSCLQIi-wq6EitcvY-kow1WxqkU_BAUOgCh-7ivw34NAWS9bhCb4JfigXYUqWoZT9izIL1d5SK_3y4UFj4MPGqXWLUAR5OG9fwpjGFlCA8LGaxfj94qR5he3wqbfO0tW_roNDHZ-NEehnEXyxwAgS6MFwA4FtQtV5gfr1HDjCq83g', 7, 'ntvnguyen.sgddt', 'ACTIVE', '2020-06-22 17:58:34', 86400000, 86400000, '62e78084-6501-3964-af9c-9f5c83249673', '24acea1b-9ed9-3fa8-aea7-26369357b397');
INSERT INTO `oauth2_authorization_token` VALUES (15, 'eyJ0eXBlIjoiand0IiwiYWxnIjoiUlM1MTIifQ.eyJzdWIiOiJudHZuZ3V5ZW4uc2dkZHQiLCJhdWQiOiJzc28taW50ZWdyYXRvciIsIklQIjoiQXBhY2hlLUh0dHBBc3luY0NsaWVudC80LjEuNCAoSmF2YS8xMS4wLjcpIiwiSUQiOiJ3U2QzdUx3WTJFZzVMSVJxVFZGa1I2V3JidjBWQTFXRyIsImV4cCI6MTU5MzE0NDI3NCwiVGltZVN0YW1wdCI6IjIwMjAtMDYtMjVUMTE6MDA6MDYuOTIiLCJpc0FjdGl2ZSI6InRydWUiLCJpYXQiOjE1OTMwNTc4NzQsIlNjaG9vbElkIjoiNzkwMDAwMDMiLCJBY2NvdW50VHlwZSI6IlBWUCIsIlN5c3RlbVVzaW5nIjoiNiJ9.MAQUiE7ijjhb5giO75O7-iZKOSamrJ5zOjcZUJ05_KrzLmgnLsqrZg4xu_GJIzc8F0XWkEl0MXQH4q0yvkWQT-LBtSz4pIlLnz_NkEOhzTJpBO3UrIv0d6FQY2NUPgdTHtMdAwAp-XW4M9Ocowu-QUguZr6bsx95W_hsyXS35AKgmFKyXl0i15VHkr0THasvm0OLBqPvmbCTFw2o9yO7Wj8d143k30yIIfxZou2bEhllr_P7f0auQmU7MYNSZLlWGkePV2XtfyRZjtd3LeUYwY8L-1hN2wXYx9Dfv_Jgr7dUc_dqWD1TbbpdN8iA_egnbYLeXT0VWd2-QI8EiBWyXA', 7, 'ntvnguyen.sgddt', 'ACTIVE', '2020-06-25 11:04:35', 86400000, 86400000, '106a364b-d15f-3542-af6e-dbfedb8c7b23', '1c4b9cf1-796c-3c40-98b4-d0ea8fc975b1');
INSERT INTO `oauth2_authorization_token` VALUES (16, 'eyJ0eXBlIjoiand0IiwiYWxnIjoiUlM1MTIifQ.eyJzdWIiOiJudHZuZ3V5ZW4uc2dkZHQiLCJhdWQiOiJzc28taW50ZWdyYXRvciIsIklQIjoiQXBhY2hlLUh0dHBBc3luY0NsaWVudC80LjEuNCAoSmF2YS8xMS4wLjcpIiwiSUQiOiJ3U2QzdUx3WTJFZzVMSVJxVFZGa1I2V3JidjBWQTFXRyIsImV4cCI6MTU5MzE1MTgyMSwiVGltZVN0YW1wdCI6IjIwMjAtMDYtMjVUMTE6MDA6MDYuOTIiLCJpc0FjdGl2ZSI6InRydWUiLCJpYXQiOjE1OTMwNjU0MjEsIlNjaG9vbElkIjoiNzkwMDAwMDMiLCJBY2NvdW50VHlwZSI6IlBWUCIsIlN5c3RlbVVzaW5nIjoiNiJ9.Rr8kPFX75HKQolWV02iyNlNldnwLYOFbjSvP18CdRH7vSIPqRF0sM_Spuboj4tdZCYNsNiyEp_EvJgsGs5q9rOdMQSCJrBto6EXq40n2DNQFyXzu7vvLnC_zL80xy-Nzouk8OhM6NDCB8uNhtO8LTVgS7Q5RvdGRJUOMaO653GHqGg7Sf4qHMMwraG7arJPHUn9ip01nEc0v0Iis566PgqhE-saFbyJKk7ygkeZEUKe29kUYvAuzKwpwsV4TrxFfOAbi0R7J1p4OsZsfgmjncssU7b-6tTXOlSUwzMLKvGTzIyQkVSj6wyL0BHEO982JTgpkmwi7zgjaP4gJlpZr8g', 7, 'ntvnguyen.sgddt', 'ACTIVE', '2020-06-25 13:10:22', 86400000, 86400000, '6aae21f7-8b83-3f63-8c6b-2a585bb0454e', '00c9123c-509a-3216-8176-0e59ada010e6');
INSERT INTO `oauth2_authorization_token` VALUES (17, 'eyJ0eXBlIjoiand0IiwiYWxnIjoiUlM1MTIifQ.eyJzdWIiOiJudHZuZ3V5ZW4uc2dkZHQiLCJhdWQiOiJzc28taW50ZWdyYXRvciIsIklQIjoiQXBhY2hlLUh0dHBBc3luY0NsaWVudC80LjEuNCAoSmF2YS8xMS4wLjcpIiwiSUQiOiJ3U2QzdUx3WTJFZzVMSVJxVFZGa1I2V3JidjBWQTFXRyIsImV4cCI6MTU5MzE1MjI5OSwiVGltZVN0YW1wdCI6IjIwMjAtMDYtMjVUMTE6MDA6MDYuOTIiLCJpc0FjdGl2ZSI6InRydWUiLCJpYXQiOjE1OTMwNjU4OTksIlNjaG9vbElkIjoiNzkwMDAwMDMiLCJBY2NvdW50VHlwZSI6IlBWUCIsIlN5c3RlbVVzaW5nIjoiNiJ9.YYvp1a36bG-8ghqgFa7p4C0y36gF5g6rVt6WwnwoSCgiv1zCF0ZleWfMG6QJgxKxC8GCUusOoRYLxD-ZURjRxcTAMSLgh6vqgcTxUw1kf5LMhAjxJJO89p2eOWdjiuG6Yu07fu0ez9lbpkAyGksXI1z5NQiEPm9PbCDRbSOVSjRRF_L71ymTZ48XVjg6GBPHJuu-3CzJ3bVdfTDRk_I0g3XLLS2sQbnN4hpsU5M-XzwEb5EEQT7rM9gWrBe6XWzsscqS69ukC64X2ox8NSD-seDG7lYbERIVJrWVC4UEEB8PdWCBJr5-boNWsYCTlNsrZbSNpvf4yY4uH06RrG8LkQ', 7, 'ntvnguyen.sgddt', 'ACTIVE', '2020-06-25 13:18:19', 86400000, 86400000, '96340a9b-6b19-3754-bdf0-be7c8b63caca', 'a1969bf1-6c16-3aa6-813e-2910063d40d9');

-- ----------------------------
-- Table structure for oauth2_consumer_apps
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_consumer_apps`;
CREATE TABLE `oauth2_consumer_apps`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `consumer_key` varchar(512) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `consumer_secret` varchar(512) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `app_name` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `app_state` varchar(10) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `callback_url` varchar(512) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `token_expire_time` bigint(20) NULL DEFAULT NULL,
  `refresh_token_expire_time` bigint(20) NULL DEFAULT NULL,
  `description` varchar(512) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oauth2_consumer_apps
-- ----------------------------
INSERT INTO `oauth2_consumer_apps` VALUES (7, 'C3hv2R69Tf1Vst73tzWkPH_3Q7oa', 'Cpffj4RRp66gPhkn5Qv06HAgBgca', 'Vala', 'ACTIVE', 'http://localhost:8081/callback', 86400000, 86400000, '');
INSERT INTO `oauth2_consumer_apps` VALUES (8, 'fWaVxz9ZK_IhHCTHkxJVeD57D1ka', 'ECViITfD8UnaXMyDq1Qw3trfibga', 'Egov', 'ACTIVE', 'regexp=http://localhost:8080/callback\\?ReturnURL=(.*)', 86400000, 86400000, 'Egov');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '202cb962ac5975b964b7152d234b70');

SET FOREIGN_KEY_CHECKS = 1;
