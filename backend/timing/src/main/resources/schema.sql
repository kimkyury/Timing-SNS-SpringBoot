DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS feed_hash_tags;
DROP TABLE IF EXISTS feeds;
DROP TABLE IF EXISTS challenge_hash_tags;
DROP TABLE IF EXISTS snapshots;
DROP TABLE IF EXISTS challenges;
DROP TABLE IF EXISTS members;
DROP TABLE IF EXISTS hash_tag_options;

CREATE TABLE hash_tag_options (
                                  id INT PRIMARY KEY AUTO_INCREMENT,
                                  content VARCHAR(255) NOT NULL
);

CREATE TABLE members (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         profile_image_url VARCHAR(255),
                         nickname VARCHAR(255),
                         is_delete BOOLEAN DEFAULT FALSE
);

CREATE TABLE challenges (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            member_id INT NOT NULL,
                            started_at DATE NOT NULL,
                            ended_at DATE NOT NULL,
                            goal_content VARCHAR(255),
                            object_url VARCHAR(255),
                            FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE
);

CREATE TABLE snapshots (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           challenge_id BIGINT NOT NULL,
                           image_url VARCHAR(255) NOT NULL,
                           created_at DATE NOT NULL,
                           FOREIGN KEY (challenge_id) REFERENCES challenges(id) ON DELETE CASCADE
);

CREATE TABLE challenge_hash_tags (
                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     hash_tag_option_id INT NOT NULL,
                                     challenge_id BIGINT NOT NULL,
                                     FOREIGN KEY (challenge_id) REFERENCES challenges(id) ON DELETE CASCADE,
                                     FOREIGN KEY (hash_tag_option_id) REFERENCES hash_tag_options(id) ON DELETE CASCADE
);

CREATE TABLE feeds (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       member_id INT NOT NULL,
                       parent_id BIGINT,
                       root_id BIGINT NOT NULL,
                       started_at DATE NOT NULL,
                       ended_at DATE NOT NULL,
                       goal_content VARCHAR(255),
                       thumbnail_url VARCHAR(255) NOT NULL,
                       timelapse_url VARCHAR(255) NOT NULL,
                       is_private BOOLEAN NOT NULL DEFAULT TRUE,
                       is_delete BOOLEAN NOT NULL DEFAULT FALSE,
                       review VARCHAR(255),
                       updated_at TIMESTAMP NOT NULL DEFAULT now(),
                       created_at TIMESTAMP NOT NULL DEFAULT now(),
                       FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
                       FOREIGN KEY (parent_id) REFERENCES feeds(id) ON DELETE SET NULL,
                       FOREIGN KEY (root_id) REFERENCES feeds(id)
);

CREATE TABLE feed_hash_tags (
                                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                hash_tag_option_id INT NOT NULL,
                                feed_id BIGINT NOT NULL,
                                FOREIGN KEY (feed_id) REFERENCES feeds(id) ON DELETE CASCADE,
                                FOREIGN KEY (hash_tag_option_id) REFERENCES hash_tag_options(id) ON DELETE CASCADE
);

CREATE TABLE comments (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          feed_id BIGINT NOT NULL,
                          member_id INT NOT NULL,
                          content VARCHAR(255),
                          created_at TIMESTAMP NOT NULL,
                          FOREIGN KEY (feed_id) REFERENCES feeds(id) ON DELETE CASCADE,
                          FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE
);


CREATE TABLE likes (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       feed_id BIGINT NOT NULL,
                       member_id INT NOT NULL,
                       FOREIGN KEY (feed_id) REFERENCES feeds(id) ON DELETE CASCADE,
                       FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE
);

