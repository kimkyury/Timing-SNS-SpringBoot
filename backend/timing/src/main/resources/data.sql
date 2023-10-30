INSERT INTO members (id, email, profile_image_url, nickname, birthyear, gender, is_delete)
VALUES (1, 'test@com', '/test.png', '테스터', 2000, '1', 0),
       (2, 'test2@com', '/test.png', '테스터2', 1998, '0', 0);

INSERT INTO hash_tag_options (id, content)
VALUES (1, '운동'),
       (2, '요리'),
       (3, '취미'),
       (4, '저녁');

INSERT INTO challenges (id, member_id, started_at, ended_at, goal_contents, object_url)
VALUES (1, 1, '2023-10-01', '2023-10-22', '테스트목표', ''),
       (2, 1, '2023-10-02', '2023-10-23', '테스트목표2', ''),
       (3, 1, '2023-10-03', '2023-10-24', NULL, '');

INSERT INTO challenge_hash_tags (id, hash_tag_options_id, challenge_id)
VALUES (3, 3, 2),
       (4, 1, 3);

INSERT INTO snapshots (id, challenge_id, image_url, created_at)
VALUES (1, 2, '/test.png', '2023-10-03'),
       (2, 2, '/test.png', '2023-10-04'),
       (3, 2, '/test.png', '2023-10-05'),
       (4, 2, '/test.png', '2023-10-06');

INSERT INTO feeds (id, member_id, parent_id, root_id, started_at, ended_at, goal_contents,
                   thumbnail_url, timelapse_url, is_private, is_delete)
VALUES (1, 1, NULL, NULL, '2023-10-02', '2023-10-23', '테스트목표', '/test.png', '/test.mp4', 1, 0);

INSERT INTO feed_hash_tags (id, hash_tag_options_id, feed_id)
VALUES (1, 2, 1),
       (2, 4, 1);


INSERT INTO comments (id, feed_id, member_id, content)
VALUES (1, 1, 1, '인증이에요');


INSERT INTO likes (id, feed_id, member_id)
VALUES (2, 1, 2);
