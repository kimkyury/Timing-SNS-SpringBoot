DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS feed_hash_tags;
DROP TABLE IF EXISTS feeds;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS challenge_hash_tags;
DROP TABLE IF EXISTS challenges;
DROP TABLE IF EXISTS snapshots;
DROP TABLE IF EXISTS members;
DROP TABLE IF EXISTS hash_tag_options;

CREATE TABLE IF NOT EXISTS hash_tag_options (
                                                id int AUTO_INCREMENT,
                                                content varchar(255) NOT NULL,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS members (
                                       id int AUTO_INCREMENT,
                                       email varchar(255) NOT NULL,
    profile_image_url varchar(255) DEFAULT NULL,
    nickname varchar(255) DEFAULT NULL,
    birth_year int COMMENT 'YYYY',
    gender char(1) COMMENT 'M:male, F:female',
    PRIMARY KEY (id),
    CONSTRAINT UNIQUE_KEY_email UNIQUE (email)
    );

CREATE TABLE IF NOT EXISTS challenges (
                                          id bigint AUTO_INCREMENT,
                                          member_id int NOT NULL,
                                          started_at date NOT NULL,
                                          ended_at date NOT NULL,
                                          goal_contents varchar(255) DEFAULT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS snapshots (
                                         id bigint AUTO_INCREMENT,
                                         challenge_id bigint NOT NULL,
                                         image_url varchar(255) NOT NULL,
    created_at date NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (challenge_id) REFERENCES challenges(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS feeds (
                                     id bigint AUTO_INCREMENT,
                                     member_id int NOT NULL,
                                     parent_id bigint DEFAULT NULL,
                                     root_id bigint DEFAULT NULL,
                                     started_at date NOT NULL,
                                     ended_at date NOT NULL,
                                     goal_contents varchar(255) DEFAULT NULL,
    thumbnail_url varchar(255) DEFAULT NULL,
    timelapse_url varchar(255) NOT NULL,
    is_private tinyint NOT NULL DEFAULT 1 COMMENT '0:public, 1:private',
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_id) REFERENCES feeds(id) ON DELETE SET NULL,
    FOREIGN KEY (root_id) REFERENCES feeds(id) ON DELETE SET NULL
    );

CREATE TABLE IF NOT EXISTS challenge_hash_tags (
                                                   id bigint AUTO_INCREMENT,
                                                   hash_tag_options_id int NOT NULL,
                                                   challenge_id bigint NOT NULL,
                                                   PRIMARY KEY (id),
    FOREIGN KEY (hash_tag_options_id) REFERENCES hash_tag_options(id) ON DELETE CASCADE,
    FOREIGN KEY (challenge_id) REFERENCES challenges(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS comments (
                                        id bigint AUTO_INCREMENT,
                                        feed_id bigint NOT NULL,
                                        member_id int NOT NULL,
                                        content varchar(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (feed_id) REFERENCES feeds(id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS feed_hash_tags (
                                              id bigint AUTO_INCREMENT,
                                              hash_tag_options_id int NOT NULL,
                                              feed_id bigint NOT NULL,
                                              PRIMARY KEY (id),
    FOREIGN KEY (hash_tag_options_id) REFERENCES hash_tag_options(id) ON DELETE CASCADE,
    FOREIGN KEY (feed_id) REFERENCES feeds(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS likes (
                                     id bigint AUTO_INCREMENT,
                                     feed_id bigint NOT NULL,
                                     member_id int NOT NULL,
                                     PRIMARY KEY (id),
    FOREIGN KEY (feed_id) REFERENCES feeds(id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE
    );
