/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE DATABASE IF NOT EXISTS `timing` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `timing`;

DELIMITER //
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

END//
DELIMITER ;

CREATE TABLE IF NOT EXISTS `challenges` (
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

CREATE TABLE IF NOT EXISTS `challenge_hash_tags` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `hash_tag_option_id` int(11) NOT NULL,
  `challenge_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_challenge_hash_tags_challenge_id_from_challenge_id` (`challenge_id`),
  KEY `fk_challenge_hash_tags_tag_option_id_from_tag_option_id` (`hash_tag_option_id`),
  CONSTRAINT `fk_challenge_hash_tags_challenge_id_from_challenge_id` FOREIGN KEY (`challenge_id`) REFERENCES `challenges` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_challenge_hash_tags_tag_option_id_from_tag_option_id` FOREIGN KEY (`hash_tag_option_id`) REFERENCES `hash_tag_options` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=251 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `comments` (
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

CREATE TABLE IF NOT EXISTS `feeds` (
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

CREATE TABLE IF NOT EXISTS `feed_hash_tags` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `hash_tag_option_id` int(11) NOT NULL,
  `feed_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_feed_hash_tags_feed_id_from_feed_id_idx` (`feed_id`),
  KEY `fk_feed_hash-tags_option_id_from_hash_tag_option_id` (`hash_tag_option_id`),
  CONSTRAINT `fk_feed_hash-tags_option_id_from_hash_tag_option_id` FOREIGN KEY (`hash_tag_option_id`) REFERENCES `hash_tag_options` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_feed_hash_tags_feed_id_from_feed_id` FOREIGN KEY (`feed_id`) REFERENCES `feeds` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `hash_tag_options` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `content` (`content`)
) ENGINE=InnoDB AUTO_INCREMENT=154 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `likes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) NOT NULL,
  `member_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_like_feed_id_from_feed_id_idx` (`feed_id`),
  KEY `fk_like_member_id_from_member_id_idx` (`member_id`),
  CONSTRAINT `fk_like_feed_id_from_feed_id` FOREIGN KEY (`feed_id`) REFERENCES `feeds` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_like_member_id_from_member_id` FOREIGN KEY (`member_id`) REFERENCES `members` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `members` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `profile_image_url` varchar(255) NOT NULL,
  `nickname` varchar(255) NOT NULL,
  `is_delete` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `snapshots` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `challenge_id` bigint(20) NOT NULL,
  `image_url` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `fk_snapshots_from_challenge_id` (`challenge_id`),
  CONSTRAINT `fk_snapshots_from_challenge_id` FOREIGN KEY (`challenge_id`) REFERENCES `challenges` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1139 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
