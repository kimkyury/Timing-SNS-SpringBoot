DROP DATABASE IF EXISTS `timing`;
CREATE DATABASE IF NOT EXISTS `timing`
USE `timing`;

DROP TABLE IF EXISTS `challenges`;

CREATE TABLE IF NOT EXISTS `challenges` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `member_id` int(11) NOT NULL,
    `started_at` date NOT NULL,
    `ended_at` date NOT NULL,
    `goal_contents` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_challenges_member_id_from_member_id` (`member_id`),
    CONSTRAINT `fk_challenges_member_id_from_member_id` FOREIGN KEY (`member_id`) REFERENCES `members` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `challenge_hash_tags`;
CREATE TABLE IF NOT EXISTS `challenge_hash_tags` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `hash_tag_options_id` int(11) NOT NULL,
    `challenge_id` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_time_lapse_ahsh_tags_hash_tags_option_id_from_hash_tags__idx` (`hash_tag_options_id`),
    KEY `fk_time_lapse_hash_tags_challenge_id_from_challenge_id_idx` (`challenge_id`),
    KEY `fk_time_laps_tags_option_id_from_tag_option_id_idx` (`hash_tag_options_id`),
    KEY `fk_time_laps_tags_challenge_id_from_challenge_id_idx` (`challenge_id`),
    CONSTRAINT `fk_challenge_tags_challenge_id_from_challenge_id` FOREIGN KEY (`challenge_id`) REFERENCES `challenges` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_challenge_tags_option_id_from_tag_option_id` FOREIGN KEY (`hash_tag_options_id`) REFERENCES `hash_tag_options` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `comments`;
CREATE TABLE IF NOT EXISTS `comments` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `feed_id` bigint(20) NOT NULL,
    `member_id` int(11) NOT NULL,
    `content` varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_comment_feed_id_from_feed_id_idx` (`feed_id`),
    KEY `fk_comment_member_id_from_member_id_idx` (`member_id`),
    CONSTRAINT `fk_comment_feed_id_from_feed_id` FOREIGN KEY (`feed_id`) REFERENCES `feeds` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_comment_member_id_from_member_id` FOREIGN KEY (`member_id`) REFERENCES `members` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `feeds`;
CREATE TABLE IF NOT EXISTS `feeds` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `member_id` int(11) NOT NULL,
    `parent_id` bigint(20) DEFAULT NULL,
    `root_id` bigint(20) DEFAULT NULL,
    `started_at` date NOT NULL,
    `ended_at` date NOT NULL,
    `goal_contents` varchar(255) DEFAULT NULL,
    `thumbnail_url` varchar(255) DEFAULT NULL,
    `timelapse_url` varchar(255) NOT NULL,
    `is_private` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0:public, 1:private',
    PRIMARY KEY (`id`),
    KEY `fk_root_id_from_feed_id_idx` (`root_id`),
    KEY `fk_parent_id_from_feed_id_idx` (`parent_id`),
    KEY `fk_member_id_from_member_id` (`member_id`),
    CONSTRAINT `fk_member_id_from_member_id` FOREIGN KEY (`member_id`) REFERENCES `members` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_parent_id_from_feed_id` FOREIGN KEY (`parent_id`) REFERENCES `feeds` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT `fk_root_id_from_feed_id` FOREIGN KEY (`root_id`) REFERENCES `feeds` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `feed_hash_tags`;
CREATE TABLE IF NOT EXISTS `feed_hash_tags` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `hash_tag_options_id` int(11) NOT NULL,
    `feed_id` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_feed_hash_tags_from_hash_tag_options_id_idx` (`hash_tag_options_id`),
    KEY `fk_feed_hash_tags_feed_id_from_feed_id_idx` (`feed_id`),
    CONSTRAINT `fk_feed_hash_tags_feed_id_from_feed_id` FOREIGN KEY (`feed_id`) REFERENCES `feeds` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_feed_hash_tags_hash_tag_options_id_from_hash_tag_options_id` FOREIGN KEY (`hash_tag_options_id`) REFERENCES `hash_tag_options` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `hash_tag_options`;
CREATE TABLE IF NOT EXISTS `hash_tag_options` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `content` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `likes`;
CREATE TABLE IF NOT EXISTS `likes` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `feed_id` bigint(20) NOT NULL,
    `member_id` int(11) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_like_feed_id_from_feed_id_idx` (`feed_id`),
    KEY `fk_like_member_id_from_member_id_idx` (`member_id`),
    CONSTRAINT `fk_like_feed_id_from_feed_id` FOREIGN KEY (`feed_id`) REFERENCES `feeds` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_like_member_id_from_member_id` FOREIGN KEY (`member_id`) REFERENCES `members` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `members`;
CREATE TABLE IF NOT EXISTS `members` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `email` varchar(255) NOT NULL,
    `profile_image_url` varchar(255) DEFAULT NULL,
    `nickname` varchar(255) DEFAULT NULL,
    `birthyear` int(11) DEFAULT NULL COMMENT 'YYYY',
    `gender` tinyint(1) DEFAULT NULL COMMENT '0:male, 1:female',
    PRIMARY KEY (`id`),
    UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `snapshots`;
CREATE TABLE IF NOT EXISTS `snapshots` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `challenge_id` bigint(20) NOT NULL,
    `image_url` varchar(255) NOT NULL,
    `created_at` date NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_snapshots_from_challenge_id` (`challenge_id`),
    CONSTRAINT `fk_snapshots_from_challenge_id` FOREIGN KEY (`challenge_id`) REFERENCES `challenges` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

