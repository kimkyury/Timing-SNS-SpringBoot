package com.kkukku.timing.apis.challenge.services;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.kkukku.timing.apis.challenge.entities.ChallengeEntity;
import com.kkukku.timing.apis.challenge.entities.SnapshotEntity;
import com.kkukku.timing.apis.challenge.repositories.ChallengeRepository;
import com.kkukku.timing.apis.challenge.requests.ChallengeCreateRequest;
import com.kkukku.timing.apis.challenge.requests.ChallengeRelayRequest;
import com.kkukku.timing.apis.challenge.responses.ChallengePolygonResponse;
import com.kkukku.timing.apis.challenge.responses.ChallengeResponse;
import com.kkukku.timing.apis.challenge.responses.ChallengeResponse.Challenge;
import com.kkukku.timing.apis.feed.entities.FeedEntity;
import com.kkukku.timing.apis.feed.services.FeedService;
import com.kkukku.timing.apis.hashtag.entities.HashTagOptionEntity;
import com.kkukku.timing.apis.hashtag.services.ChallengeHashTagService;
import com.kkukku.timing.apis.hashtag.services.FeedHashTagService;
import com.kkukku.timing.apis.hashtag.services.HashTagOptionService;
import com.kkukku.timing.apis.member.entities.MemberEntity;
import com.kkukku.timing.apis.member.services.MemberService;
import com.kkukku.timing.exception.CustomException;
import com.kkukku.timing.response.codes.ErrorCode;
import com.kkukku.timing.s3.services.S3Service;
import jakarta.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final S3Service s3Service;
    private final FeedService feedService;
    private final MemberService memberService;
    private final SnapshotService snapshotService;
    private final FeedHashTagService feedHashTagService;
    private final HashTagOptionService hashTagOptionService;
    private final ChallengeHashTagService challengeHashTagService;

    private final ChallengeRepository challengeRepository;

    @Transactional
    public void createChallengeProcedure(Integer memberId,
        ChallengeCreateRequest challengeCreateRequest) {

        MemberEntity member = memberService.getMemberById(memberId);

        hashTagOptionService.createHashTagOptions(challengeCreateRequest.getHashTags());

        ChallengeEntity savedChallenge = saveChallenge(member, challengeCreateRequest);

        List<HashTagOptionEntity> hashTagOptions = hashTagOptionService.getHashTagOption(
            challengeCreateRequest.getHashTags());

        challengeHashTagService.createChallengeHashTag(savedChallenge, hashTagOptions);
    }

    public ChallengeEntity saveChallenge(MemberEntity member,
        ChallengeCreateRequest challengeCreateRequest) {

        ChallengeEntity challenge = ChallengeEntity.of(member, challengeCreateRequest);
        return challengeRepository.save(challenge);
    }


    public ChallengeResponse getChallenge(Integer memberId) {

        return new ChallengeResponse(
            challengeRepository.findByMemberId(memberId)
                               .stream()
                               .map(c -> {
                                   String thumbnailUrl =
                                       s3Service.getS3StartUrl() + c.getThumbnailUrl();
                                   long countDays = diffDay(c.getStartedAt(),
                                       LocalDate.now()
                                                .minusDays(1));
                                   long maxDays = diffDay(c.getStartedAt(), c.getEndedAt());
                                   return new Challenge(thumbnailUrl, countDays, maxDays);
                               })
                               .toList());
    }


    private long diffDay(LocalDate startedAt, LocalDate yesterday) {

        return ChronoUnit.DAYS.between(startedAt, yesterday);
    }

    public void deleteChallenge(Integer memberId, Long challengeId) {

        checkOwnChallenge(memberId, challengeId);

        List<SnapshotEntity> snapshots = snapshotService.getAllSnapshotByChallenge(challengeId);
        snapshots.stream()
                 .map(SnapshotEntity::getImageUrl)
                 .forEach(s3Service::deleteFile);

        challengeRepository.deleteById(challengeId);

    }

    public void extendChallenge(Integer memberId, Long challengeId) {

        checkOwnChallenge(memberId, challengeId);
        ChallengeEntity challenge = getChallengeById(challengeId);

        LocalDate endedAt = challenge.getEndedAt();
        LocalDate extendEndedAt = endedAt.plusDays(21);

        challenge.setEndedAt(extendEndedAt);
        challengeRepository.save(challenge);

    }


    @Transactional
    public void relayChallenge(Integer memberId, Long feedId, ChallengeRelayRequest request) {

        FeedEntity feed = feedService.getFeedById(feedId);
        List<HashTagOptionEntity> hashTagOptionByFeed = feedHashTagService.getHashTagOptionByFeedId(
            feedId);
        MemberEntity member = memberService.getMemberById(memberId);

        ChallengeEntity challenge = ChallengeEntity.of(member, request);
        challenge.setParent(feed);
        ChallengeEntity savedChallenge = challengeRepository.save(challenge);

        challengeHashTagService.createChallengeHashTag(savedChallenge, hashTagOptionByFeed);
    }

    public ChallengePolygonResponse getPolygonByChallenge(Integer memberId, Long challengeId) {

        ChallengeEntity challenge = getChallengeById(challengeId);
        checkOwnChallenge(memberId, challengeId);

        String fineName = challenge.getPolygonUrl();

        String polygonToString = "";
        S3Object s3Object = s3Service.getFile(fineName);
        try (S3ObjectInputStream s3is = s3Object.getObjectContent();
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(s3is, StandardCharsets.UTF_8))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line)
                             .append("\n"); // TODO: 줄바꿈은 필요가 없을 수도 있다
            }
            polygonToString = stringBuilder.toString()
                                           .trim();
        } catch (IOException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        } finally {
            try {
                s3Object.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return new ChallengePolygonResponse(polygonToString);
    }


    public ChallengeEntity getChallengeById(Long challengeId) {
        return challengeRepository.findById(challengeId)
                                  .orElseThrow(
                                      () -> new CustomException(ErrorCode.NOT_EXIST_CHALLENGE));

    }

    public void checkOwnChallenge(Integer memberId, Long challengeId) {

        challengeRepository.findByIdAndMemberId(challengeId, memberId)
                           .orElseThrow(
                               () -> new CustomException(ErrorCode.THIS_CHALLENGE_IS_NOT_YOURS));

    }

    public void checkCompletedChallenge(Long challengeId) {

        ChallengeEntity challenge = getChallengeById(challengeId);
        long expectedCnt = diffDay(challenge.getStartedAt(), challenge.getEndedAt());
        long actualCnt = snapshotService.getCntSnapshotByChallenge(challengeId);

        if (expectedCnt != actualCnt) {
            throw new CustomException(ErrorCode.NOT_COMPLETED_CHALLENGE);
        }
    }

    @Transactional
    public void saveObjectAndPolygon(Integer MemberId, Long challengeId, MultipartFile polygon,
        MultipartFile object) {

        ChallengeEntity challenge = getChallengeById(challengeId);
        checkOwnChallenge(MemberId, challengeId);
        // TODO: 스냅샷 등록 전적이 있다면 예외 처리

        String savedPolygonUrl = s3Service.uploadFile(polygon);
        String savedObjectUrl = s3Service.uploadFile(object);

        challenge.setPolygonUrl("/" + savedPolygonUrl);
        challenge.setObjectUrl("/" + savedObjectUrl);

        challengeRepository.save(challenge);

    }


}
