INSERT INTO hash_tag_options (content)
VALUES ('운동'),
       ('요리'),
       ('취미'),
       ('저녁');

INSERT INTO members(email, profile_image_url, nickname, is_delete)
VALUES ('kkr@test.com', '/default_profile.png', '테스터', 0),
       ('unit@test.com', '/default_profile.png', '테스터2', 0),
       ('man@test.com', '/default_profile.png', '테스터3', 0),
       ('aeae2323@kakao.com',
        'http://k.kakaocdn.net/dn/ba8EwB/btsrAQ5HxYP/yz7T8qhedmmMQAWKkur861/img_640x640.jpg', '김규리',
        0);


INSERT INTO challenges (member_id, started_at, ended_at, goal_content, thumbnail_url, object_url,
                        polygon_url)
VALUES (1, '2023-10-01', '2023-10-22', '강아지와 산책', '/default_thumbnail.png', '/test_object.png',
        '/test_polygon.txt'),
       (1, '2023-10-02', '2023-10-23', '하체 운동하기', '/test_thumbnail.png', '/test_object.png',
        '/test_polygon.txt'),
       (1, '2023-10-03', '2023-10-24', NULL, '/default_thumbnail.png', null, null),
       (1, '2023-11-09', '2023-12-01', NULL, '/default_thumbnail.png', null, null),
       (1, '2023-11-10', '2023-12-01', NULL, '/default_thumbnail.png', null, null),
       (2, '2023-11-11', '2023-12-02', NULL, '/default_thumbnail.png', null, null),
       (1, '2023-11-01', '2023-11-22', NULL, '/default_thumbnail.png', null, null),
       (4, DATE '2023-11-13', DATE '2023-12-04', NULL,
        '/ef1368d4-ec41-42f9-b698-75f0b8e4519f_2.jpg',
        '/da95c569-f2a0-4fb3-82fa-8d93024c4ade_watch_final_object.png',
        '/3750b2aa-faa4-482c-a677-007a447fdfee_polygon');
INSERT INTO challenge_hash_tags (hash_tag_option_id, challenge_id)
VALUES (3, 2),
       (1, 3),
       (1, 1),
       (3, 1),
       (3, 8),
       (2, 8),
       (1, 8);

INSERT INTO snapshots(challenge_id, image_url, created_at)
VALUES (2, '/test_snapshot.png', TIMESTAMP '2023-10-03 00:00:00'),
       (2, '/test_snapshot.png2', TIMESTAMP '2023-10-04 00:00:00'),
       (2, '/test_snapshot.png3', TIMESTAMP '2023-10-05 00:00:00'),
       (2, '/test_snapshot.png', TIMESTAMP '2023-10-06 00:00:00'),
       (4, '/test_snapshot.png', TIMESTAMP '2023-11-09 00:00:00'),
       (8, '/ef1368d4-ec41-42f9-b698-75f0b8e4519f_2.jpg', TIMESTAMP '2023-11-13 16:14:56.163002'),
       (8, '/dbc5cb2d-4979-41a2-8831-43871fa79b09_3.jpg', TIMESTAMP '2023-11-13 16:15:16.125791'),
       (8, '/6cfea5d8-c66f-4576-b91c-d56dd6e4df87_4.jpg', TIMESTAMP '2023-11-13 16:15:34.561797'),
       (8, '/59311d8e-b561-43b8-9687-706149731df0_5.jpg', TIMESTAMP '2023-11-13 16:15:50.355863'),
       (8, '/e2b6349d-5073-4246-9325-3ea7191bafaa_6.jpg', TIMESTAMP '2023-11-13 16:16:10.402465'),
       (8, '/d401a89b-8b51-4cb6-bf29-d71845b7c2c3_7.jpg', TIMESTAMP '2023-11-13 16:16:27.149214'),
       (8, '/b8aecc6c-f0b9-427e-bdb0-3ce8c6fb7267_8.jpg', TIMESTAMP '2023-11-13 16:16:43.969775'),
       (8, '/9678a0e0-4112-4a43-ac3b-92785f7d785b_9.jpg', TIMESTAMP '2023-11-13 16:17:02.400139'),
       (8, '/e81b2dd9-5906-4819-b071-51f45f784c53_10.jpg', TIMESTAMP '2023-11-13 16:17:20.147055'),
       (8, '/ae609292-c668-4f7a-9fc6-4a7ddd1e0d3e_11.jpg', TIMESTAMP '2023-11-13 16:17:47.693571'),
       (8, '/9c9d7ab4-525c-413b-b385-fdac448c187d_12.jpg', TIMESTAMP '2023-11-13 16:18:06.991622'),
       (8, '/4f3ca3ed-1aa0-465a-8bfa-435232eabd46_13.jpg', TIMESTAMP '2023-11-13 16:19:39.673374'),
       (8, '/0ef860bc-1210-4752-9562-810c6a090923_14.jpg', TIMESTAMP '2023-11-13 16:21:21.937891'),
       (8, '/7b4c534b-3e7b-4949-8079-19116e30184b_15.jpg', TIMESTAMP '2023-11-13 16:21:44.077161'),
       (8, '/7965385d-9367-47df-8ef8-6707aaa682e6_16.jpg', TIMESTAMP '2023-11-13 16:22:04.002233'),
       (8, '/e9074231-fe2b-4006-bdeb-959c14e0cf98_17.jpg', TIMESTAMP '2023-11-13 16:23:31.636113'),
       (8, '/1a2baf36-89ca-43e9-9821-c13d0c50ae5f_18.jpg', TIMESTAMP '2023-11-13 16:23:51.447764'),
       (8, '/9a9853fa-7373-48f5-a0eb-4836f1138a59_19.jpg', TIMESTAMP '2023-11-13 16:24:19.952465'),
       (8, '/48b65e34-38d1-4ec2-a758-b1eb3e295ffc_20.jpg', TIMESTAMP '2023-11-13 16:24:41.670123'),
       (8, '/9e5af592-db59-46bb-822b-57a154c30050_21.jpg', TIMESTAMP '2023-11-13 16:25:01.452882'),
       (8, '/bb3e5bac-28e7-4a10-b1e5-88b74cc3b23e_22.jpg', TIMESTAMP '2023-11-13 16:25:42.828884');

INSERT INTO feeds (member_id,
                   root_id,
                   parent_id,
                   started_at,
                   ended_at,
                   goal_content,
                   thumbnail_url,
                   timelapse_url,
                   is_private,
                   is_delete,
                   review)
VALUES (1, 1, null, '2023-05-01', '2023-05-30', '마라톤 훈련 프로그램 완성',
        '/example.com/thumbnail1.jpg',
        '/example.com/timelapse1.mp4', FALSE, FALSE, '완주해서 기분이 좋았습니다.'),
       (2, 2, null, '2023-05-15', '2023-06-14', '개인 개발에 관한 5권의 책 읽기',
        '/example.com/thumbnail2.jpg',
        '/example.com/timelapse2.mp4', TRUE, FALSE, '유익하고 영감을 주는 책이었습니다.'),
       (1, 1, 1, '2023-06-01', '2023-06-30', '프로그래밍 과정 완료', '/example.com/thumbnail3.jpg',
        '/example.com/timelapse3.mp4', TRUE, TRUE, '새로운 개념을 많이 배웠습니다.'),
       (2, 2, 2, '2023-06-20', '2023-07-19', '새로운 요리 레시피 마스터', '/example.com/thumbnail4.jpg',
        '/example.com/timelapse4.mp4', FALSE, FALSE, '요리가 맛있게 나왔습니다.'),
       (1, 2, 4, '2023-07-05', '2023-07-25', '자바스크립트 공부하기', '/example.com/thumbnail5.jpg',
        '/example.com/timelapse5.mp4', TRUE, FALSE, '자바스크립트 기초를 잘 다질 수 있었습니다.'),
       (2, 2, 4, '2023-07-10', '2023-08-09', '온라인 마케팅 과정 수료', '/example.com/thumbnail6.jpg',
        '/example.com/timelapse6.mp4', FALSE, FALSE, '마케팅 스킬이 향상되었습니다.'),
       (1, 2, 6, '2023-08-01', '2023-08-31', '풀스택 개발자 과정', '/example.com/thumbnail7.jpg',
        '/example.com/timelapse7.mp4', TRUE, FALSE, '프론트엔드와 백엔드를 모두 경험해 볼 수 있었습니다.'),
       (2, 2, 4, '2023-08-15', '2023-09-14', '데이터 분석 프로젝트 완성', '/example.com/thumbnail8.jpg',
        '/example.com/timelapse8.mp4', FALSE, TRUE, '데이터 분석 기술을 향상시켰습니다.');

INSERT INTO comments (feed_id, member_id, content, created_at)
VALUES (1, 1, '인증이에요', '2023-11-03 12:04:07');

INSERT INTO feed_hash_tags (hash_tag_option_id, feed_id)
VALUES (2, 1),
       (4, 1);


INSERT INTO likes (feed_id, member_id)
VALUES (1, 2);


