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

INSERT INTO feeds (member_id,
                   root_id,
                   started_at,
                   ended_at,
                   goal_contents,
                   thumbnail_url,
                   timelapse_url,
                   is_private,
                   is_delete,
                   review)
VALUES (1, 1, '2023-05-01', '2023-05-30', '마라톤 훈련 프로그램 완성', 'http://example.com/thumbnail1.jpg',
        'http://example.com/timelapse1.mp4', FALSE, FALSE, '완주해서 기분이 좋았습니다.'),
       (2, 2, '2023-05-15', '2023-06-14', '개인 개발에 관한 5권의 책 읽기', 'http://example.com/thumbnail2.jpg',
        'http://example.com/timelapse2.mp4', TRUE, FALSE, '유익하고 영감을 주는 책이었습니다.'),
       (1, 3, '2023-06-01', '2023-06-30', '프로그래밍 과정 완료', 'http://example.com/thumbnail3.jpg',
        'http://example.com/timelapse3.mp4', TRUE, TRUE, '새로운 개념을 많이 배웠습니다.'),
       (2, 4, '2023-06-20', '2023-07-19', '새로운 요리 레시피 마스터', 'http://example.com/thumbnail4.jpg',
        'http://example.com/timelapse4.mp4', FALSE, FALSE, '요리가 맛있게 나왔습니다.'),
       (1, 5, '2023-07-05', '2023-07-25', '자바스크립트 공부하기', 'http://example.com/thumbnail5.jpg',
        'http://example.com/timelapse5.mp4', TRUE, FALSE, '자바스크립트 기초를 잘 다질 수 있었습니다.'),
       (2, 6, '2023-07-10', '2023-08-09', '온라인 마케팅 과정 수료', 'http://example.com/thumbnail6.jpg',
        'http://example.com/timelapse6.mp4', FALSE, FALSE, '마케팅 스킬이 향상되었습니다.'),
       (1, 7, '2023-08-01', '2023-08-31', '풀스택 개발자 과정', 'http://example.com/thumbnail7.jpg',
        'http://example.com/timelapse7.mp4', TRUE, FALSE, '프론트엔드와 백엔드를 모두 경험해 볼 수 있었습니다.'),
       (2, 8, '2023-08-15', '2023-09-14', '데이터 분석 프로젝트 완성', 'http://example.com/thumbnail8.jpg',
        'http://example.com/timelapse8.mp4', FALSE, TRUE, '데이터 분석 기술을 향상시켰습니다.');



INSERT INTO comments
VALUES (1, 1, 1, '인증이에요', '2023-11-03 12:04:07');

INSERT INTO feed_hash_tags
VALUES (1, 2, 1),
       (2, 4, 1);


INSERT INTO likes
VALUES (2, 1, 2);


