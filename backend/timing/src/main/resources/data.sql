INSERT INTO hash_tag_options
VALUES (1, '운동'),
       (2, '요리'),
       (3, '취미'),
       (4, '저녁');

INSERT INTO members
VALUES (1, 'kkr@com', '/test.png', '테스터', 0),
       (2, 'unit@com', '/test.png', '테스터2', 0);

INSERT INTO challenges
VALUES (1, 1, '2023-10-01', '2023-10-22', '강아지와 산책', '/test.png'),
       (2, 1, '2023-10-02', '2023-10-23', '하체 운동하기', '/test.png'),
       (3, 1, '2023-10-03', '2023-10-24', NULL, '/test.png');

INSERT INTO challenge_hash_tags
VALUES (3, 3, 2),
       (4, 1, 3);

INSERT INTO snapshots
VALUES (1, 2, '/test.png', '2023-10-03'),
       (2, 2, '/test.png', '2023-10-04'),
       (3, 2, '/test.png', '2023-10-05'),
       (4, 2, '/test.png', '2023-10-06');

INSERT INTO feeds
VALUES (1, 1, NULL, NULL, '2023-10-02', '2023-10-23', '테스트목표', '/test.png', '/test.mp4', 1, 0,
        '잘 살고있죠?', NULL, '2023-10-23 12:06:43');


INSERT INTO comments
VALUES (1, 1, 1, '인증이에요', '2023-11-03 12:04:07');

INSERT INTO feed_hash_tags
VALUES (1, 2, 1),
       (2, 4, 1);


INSERT INTO likes
VALUES (2, 1, 2);


