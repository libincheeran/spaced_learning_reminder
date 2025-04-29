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
INSERT INTO `leetcode_problems` VALUES (50,'https://leetcode.com/problems/diameter-of-binary-tree/description/?envType=company&envId=facebook&favoriteSlug=facebook-thirty-days','Diameter of Binary Tree',543,'2025-05-02 09:24:26.66998');
INSERT INTO `leetcode_problems` VALUES (51,'https://leetcode.com/problems/diameter-of-binary-tree/description/?envType=company&envId=facebook&favoriteSlug=facebook-thirty-days','Diameter of Binary Tree',543,'2025-05-18 09:24:26.66998');
INSERT INTO `leetcode_problems` VALUES (52,'https://leetcode.com/problems/diameter-of-binary-tree/description/?envType=company&envId=facebook&favoriteSlug=facebook-thirty-days','Diameter of Binary Tree',543,'2025-08-27 09:24:26.66998');
INSERT INTO `leetcode_problems` VALUES (53,'https://leetcode.com/problems/diameter-of-binary-tree/description/?envType=company&envId=facebook&favoriteSlug=facebook-thirty-days','Diameter of Binary Tree',543,'2025-05-08 09:24:26.66998');
INSERT INTO `leetcode_problems` VALUES (54,'https://leetcode.com/problems/diameter-of-binary-tree/description/?envType=company&envId=facebook&favoriteSlug=facebook-thirty-days','Diameter of Binary Tree',543,'2025-12-05 09:24:26.66998');
INSERT INTO `leetcode_problems` VALUES (55,'https://leetcode.com/problems/diameter-of-binary-tree/description/?envType=company&envId=facebook&favoriteSlug=facebook-thirty-days','Diameter of Binary Tree',543,'2025-06-13 09:24:26.66998');
INSERT INTO `leetcode_problems` VALUES (56,'https://leetcode.com/problems/diameter-of-binary-tree/description/?envType=company&envId=facebook&favoriteSlug=facebook-thirty-days','Diameter of Binary Tree',543,'2025-05-28 09:24:26.66998');
UNLOCK TABLES;
