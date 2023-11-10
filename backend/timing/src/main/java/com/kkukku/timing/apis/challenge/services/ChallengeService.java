package com.kkukku.timing.apis.challenge.services;

import com.amazonaws.services.s3.model.S3Object;
import com.kkukku.timing.apis.challenge.entities.ChallengeEntity;
import com.kkukku.timing.apis.challenge.entities.SnapshotEntity;
import com.kkukku.timing.apis.challenge.repositories.ChallengeRepository;
import com.kkukku.timing.apis.challenge.requests.ChallengeCreateRequest;
import com.kkukku.timing.apis.challenge.requests.ChallengeRelayRequest;
import com.kkukku.timing.apis.challenge.responses.ChallengePolygonResponse;
import com.kkukku.timing.apis.challenge.responses.ChallengeResponse;
import com.kkukku.timing.apis.challenge.responses.ChallengeResponse.Challenge;
import com.kkukku.timing.apis.feed.entities.FeedEntity;
import com.kkukku.timing.apis.feed.repositories.FeedRepository;
import com.kkukku.timing.apis.hashtag.entities.HashTagOptionEntity;
import com.kkukku.timing.apis.hashtag.services.ChallengeHashTagService;
import com.kkukku.timing.apis.hashtag.services.FeedHashTagService;
import com.kkukku.timing.apis.hashtag.services.HashTagOptionService;
import com.kkukku.timing.apis.member.entities.MemberEntity;
import com.kkukku.timing.apis.member.services.MemberService;
import com.kkukku.timing.exception.CustomException;
import com.kkukku.timing.external.services.VisionAIService;
import com.kkukku.timing.response.codes.ErrorCode;
import com.kkukku.timing.s3.services.S3Service;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient.ResponseSpec;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final S3Service s3Service;
    private final MemberService memberService;
    private final FeedRepository feedRepository;
    private final VisionAIService visionAIService;
    private final SnapshotService snapshotService;
    private final FeedHashTagService feedHashTagService;
    private final ChallengeRepository challengeRepository;
    private final HashTagOptionService hashTagOptionService;
    private final ChallengeHashTagService challengeHashTagService;

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

    public void deleteChallenge(ChallengeEntity challenge) {
        challengeRepository.save(challenge);
    }

    public List<ChallengeEntity> getAllChallenge() {
        return challengeRepository.findAll();
    }

    public ChallengeEntity saveChallenge(MemberEntity member,
        ChallengeCreateRequest challengeCreateRequest) {

        return challengeRepository.save(ChallengeEntity.of(member, challengeCreateRequest));
    }


    public ChallengeResponse getChallenge(Integer memberId) {

        return new ChallengeResponse(
            challengeRepository.findByMemberId(memberId)
                               .stream()
                               .map(c -> {
                                   long id = c.getId();
                                   String thumbnailUrl =
                                       s3Service.getS3StartUrl() + c.getThumbnailUrl();
                                   long countDays = diffDay(c.getStartedAt(), LocalDate.now());
                                   long maxDays = diffDay(c.getStartedAt(), c.getEndedAt());
                                   return new Challenge(id, thumbnailUrl, countDays, maxDays);
                               })
                               .toList());
    }

    @Transactional
    public void deleteChallengeProcedure(Integer memberId, Long challengeId) {

        checkOwnChallenge(memberId, challengeId);

        List<SnapshotEntity> snapshots = snapshotService.getAllSnapshotByChallenge(challengeId);

        snapshots.stream()
                 .map(SnapshotEntity::getImageUrl)
                 .forEach(s3Service::deleteFile);

        snapshotService.deleteSnapshot(snapshots);

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

        FeedEntity feed = feedRepository.findById(feedId)
                                        .orElseThrow(() -> new CustomException(
                                            ErrorCode.NOT_EXIST_FEED));
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

        S3Object polygonS3Object = s3Service.getFile(fineName);
        String polygonToString = s3Service.convertS3ObjectToString(polygonS3Object);

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
    public void saveObjectAndPolygon(Integer memberId, Long challengeId, String polygon,
        MultipartFile object) {

        ChallengeEntity challenge = getChallengeById(challengeId);

        checkOwnChallenge(memberId, challengeId);

        if (challenge.getObjectUrl() != null) {
            throw new CustomException(ErrorCode.EXIST_OBJECT_IN_CHALLENGE);
        }

        String savedPolygonUrl = s3Service.uploadStringAsTextFile(polygon, "polygon");
        String savedObjectUrl = s3Service.uploadFile(object);

        challenge.setPolygonUrl("/" + savedPolygonUrl);
        challenge.setObjectUrl("/" + savedObjectUrl);

        challengeRepository.save(challenge);
    }

    public void setSnapshotProcedure(
        Integer memberId, Long challengeId, MultipartFile snapshot) {

        ChallengeEntity challenge = getChallengeById(challengeId);

        checkOwnChallenge(memberId, challengeId);

        String objectUrl = s3Service.getS3StartUrl() + challenge.getObjectUrl();
        ByteArrayResource snapshotResource = getByteArrayResource(snapshot);
        MultiValueMap<String, Object> requestBody = getCheckSimilarityBody(snapshotResource,
            objectUrl);

        visionAIService.checkSimilarity(requestBody);

        String savedSnapshotUrl = s3Service.uploadFile(snapshot);

        saveChallengeThumbnail(challenge, savedSnapshotUrl);

        snapshotService.createSnapshot(challenge, "/" + savedSnapshotUrl);
    }


    public ResponseSpec getDetectedObject(MultipartFile snapshot) {

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        ByteArrayResource snapshotResource = getByteArrayResource(snapshot);
        body.add("snapshot", snapshotResource);

        return visionAIService.getDetectedObject(body);
    }

    public ResponseSpec getChoiceObject(String x, String y, MultipartFile snapshot) {

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        ByteArrayResource snapshotResource = getByteArrayResource(snapshot);
        body.add("snapshot", snapshotResource);
        body.add("x", x);
        body.add("y", y);

        return visionAIService.checkCoordinate(body);
    }

    private MultiValueMap<String, Object> getCheckSimilarityBody(ByteArrayResource snapshotResource,
        String objectUrl) {

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("snapshot", snapshotResource);
        body.add("objectUrl", objectUrl);

        return body;
    }

    private void saveChallengeThumbnail(ChallengeEntity challenge, String thumbnailUrl) {

        if (challenge.getThumbnailUrl()
                     .equals("/default_thumbnail.png")) {
            challenge.setThumbnailUrl("/" + thumbnailUrl);
            challengeRepository.save(challenge);
        }
    }

    private ByteArrayResource getByteArrayResource(MultipartFile file) {
        try {
            return new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
        } catch (IOException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public long diffDay(LocalDate startedAt, LocalDate yesterday) {
        return ChronoUnit.DAYS.between(startedAt, yesterday);
    }
}


