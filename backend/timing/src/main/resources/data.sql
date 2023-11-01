INSERT INTO members (email, profile_image_url, nickname, birthyear, gender, is_delete)
VALUES ('test@com', '/test.png', '테스터', 2000, '1', 0),
       ('test2@com', '/test.png', '테스터2', 1998, '0', 0);

INSERT INTO hash_tag_options (content)
VALUES ('운동'),
       ('요리'),
       ('취미'),
       ('저녁');

INSERT INTO challenges (member_id, started_at, ended_at, goal_contents, object_url)
VALUES (1, '2023-10-01', '2023-10-22', '테스트목표', ''),
       (1, '2023-10-02', '2023-10-23', '테스트목표2', ''),
       (1, '2023-10-03', '2023-10-24', NULL, '');

INSERT INTO challenge_hash_tags (hash_tag_options_id, challenge_id)
VALUES (3, 2),
       (1, 3);

INSERT INTO snapshots (challenge_id, image_url, created_at)
VALUES (2, '/test.png', '2023-10-03'),
       (2, '/test.png', '2023-10-04'),
       (2, '/test.png', '2023-10-05'),
       (2, '/test.png', '2023-10-06');

INSERT INTO feeds (member_id, parent_id, root_id, started_at, ended_at, goal_contents,
                   thumbnail_url, timelapse_url, is_private, is_delete)
VALUES (1, NULL, NULL, '2023-10-02', '2023-10-23', '테스트목표', '/test.png', '/test.mp4', 1, 0);

INSERT INTO feed_hash_tags (hash_tag_options_id, feed_id)
VALUES (2, 1),
       (4, 1);

INSERT INTO comments (feed_id, member_id, content)
VALUES (1, 1, '인증이에요');

INSERT INTO likes (feed_id, member_id)
VALUES (1, 2);
