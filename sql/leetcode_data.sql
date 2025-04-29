-- MySQL dump
-- Database: leetcode

DROP TABLE IF EXISTS `leetcode_problems`;
CREATE TABLE `leetcode_problems` (
  `id` int NOT NULL AUTO_INCREMENT,
  `comments` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `problem_id` int NOT NULL,
  `scheduled_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table `leetcode_problems`

LOCK TABLES `leetcode_problems` WRITE;
INSERT INTO `leetcode_problems` VALUES (29,'periodic','periodic',500,'2025-05-18 09:13:41.687797');
INSERT INTO `leetcode_problems` VALUES (30,'periodic','periodic',500,'2025-05-02 09:13:41.687797');
INSERT INTO `leetcode_problems` VALUES (31,'periodic','periodic',500,'2025-08-27 09:13:41.687797');
INSERT INTO `leetcode_problems` VALUES (32,'periodic','periodic',500,'2025-05-08 09:13:41.687797');
INSERT INTO `leetcode_problems` VALUES (33,'periodic','periodic',500,'2025-12-05 09:13:41.687797');
INSERT INTO `leetcode_problems` VALUES (34,'periodic','periodic',500,'2025-05-28 09:13:41.687797');
INSERT INTO `leetcode_problems` VALUES (35,'periodic','periodic',500,'2025-06-13 09:13:41.687797');
UNLOCK TABLES;
