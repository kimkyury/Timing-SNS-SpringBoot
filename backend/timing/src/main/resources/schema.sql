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
                                                id INT NOT NULL AUTO_INCREMENT,
                                                content VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS members (
                                       id INT NOT NULL AUTO_INCREMENT,
                                       email VARCHAR(255) NOT NULL,
    profile_image_url VARCHAR(255) DEFAULT NULL,
    nickname VARCHAR(255) DEFAULT NULL,
    birthyear INT DEFAULT NULL,
    gender CHAR(1) DEFAULT NULL,
    is_delete BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id),
    UNIQUE (email)
    );

CREATE TABLE IF NOT EXISTS challenges (
                                          id BIGINT NOT NULL AUTO_INCREMENT,
                                          member_id INT NOT NULL,
                                          started_at DATE NOT NULL,
                                          ended_at DATE NOT NULL,
                                          goal_contents VARCHAR(255) DEFAULT NULL,
    object_url VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS snapshots (
                                         id BIGINT NOT NULL AUTO_INCREMENT,
                                         challenge_id BIGINT NOT NULL,
                                         image_url VARCHAR(255) NOT NULL,
    created_at DATE NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (challenge_id) REFERENCES challenges(id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS challenge_hash_tags (
                                                   id BIGINT NOT NULL AUTO_INCREMENT,
                                                   hash_tag_options_id INT NOT NULL,
                                                   challenge_id BIGINT NOT NULL,
                                                   PRIMARY KEY (id),
    FOREIGN KEY (hash_tag_options_id) REFERENCES hash_tag_options(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (challenge_id) REFERENCES challenges(id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS feeds (
                                     id BIGINT NOT NULL AUTO_INCREMENT,
                                     member_id INT NOT NULL,
                                     parent_id BIGINT DEFAULT NULL,
                                     root_id BIGINT DEFAULT NULL,
                                     started_at DATE NOT NULL,
                                     ended_at DATE NOT NULL,
                                     goal_contents VARCHAR(255) DEFAULT NULL,
    thumbnail_url VARCHAR(255) DEFAULT NULL,
    timelapse_url VARCHAR(255) NOT NULL,
    is_private BOOLEAN DEFAULT TRUE,
    is_delete BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (parent_id) REFERENCES feeds(id) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (root_id) REFERENCES feeds(id) ON DELETE SET NULL ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS feed_hash_tags (
                                              id BIGINT NOT NULL AUTO_INCREMENT,
                                              hash_tag_options_id INT NOT NULL,
                                              feed_id BIGINT NOT NULL,
                                              PRIMARY KEY (id),
    FOREIGN KEY (hash_tag_options_id) REFERENCES hash_tag_options(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (feed_id) REFERENCES feeds(id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS comments (
                                        id BIGINT NOT NULL AUTO_INCREMENT,
                                        feed_id BIGINT NOT NULL,
                                        member_id INT NOT NULL,
                                        content VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (feed_id) REFERENCES feeds(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS likes (
                                     id BIGINT NOT NULL AUTO_INCREMENT,
                                     feed_id BIGINT NOT NULL,
                                     member_id INT NOT NULL,
                                     PRIMARY KEY (id),
    FOREIGN KEY (feed_id) REFERENCES feeds(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE ON UPDATE CASCADE
    );
