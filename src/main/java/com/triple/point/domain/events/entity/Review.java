package com.triple.point.domain.events.entity;

import com.triple.point.domain.common.entity.BaseTimeEntity;
import com.triple.point.domain.common.type.ReviewType;
import com.triple.point.domain.events.dto.PointRequest;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "REVIEW",
    indexes ={
        @Index(name = "_placeIdOnReview", columnList = "placeId")
    }
)
@Entity
@ToString
public class Review extends BaseTimeEntity {
    @Id
    @Column(name = "review_id", columnDefinition = "CHAR(36)")
    @Type(type = "uuid-char")
    private UUID id;
    private String content;
    private int attachedPhotoIds;

    @Enumerated(EnumType.STRING)
    private ReviewType type;

    @Builder.Default
    private int point = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "CHAR(36)")
    private String placeId;

    public static Review createReview(PointRequest request) {
        Review review = Review.builder()
                .id(UUID.fromString(request.getReviewId()))
                .content(request.getContent())
                .attachedPhotoIds(request.getAttachedPhotoIds().size())
                .type(ReviewType.NORMAL)
                .placeId(request.getPlaceId())
                .build();
        review.initPoint();
        return review;
    }

    public static Review createFirstReview(PointRequest request) {
        Review review = Review.builder()
                .id(UUID.fromString(request.getReviewId()))
                .content(request.getContent())
                .attachedPhotoIds(request.getAttachedPhotoIds().size())
                .type(ReviewType.FIRST)
                .placeId(request.getPlaceId())
                .build();
        review.initPoint();
        return review;
    }

    public void setUser(User user) {
        this.user = user;
        user.addPoint(point);
    }

    public int modifyReview(String content, int attachedPhotoIds) {
        int current = point; // 현재 리뷰 점수 저장

        this.point = 0; // 초기화
        // 내용, 사진수 수정
        this.content = content;
        this.attachedPhotoIds = attachedPhotoIds;
        initPoint(); // 포인트 계산

        // 증감 포인트 계산 : 수정 포인트 - 현재 포인트
        int increasePoint = this.point - current;
        this.user.addPoint(increasePoint); // 유저 총점 -증감 포인트
        
        return increasePoint;
    }

    public int deleteReview() {
        int deletePoint = point;
        user.addPoint((point * -1));
        point -= point;
        return deletePoint;
    }

    private void initPoint() {
        point += (content.length() > 0) ? 1 : 0;
        point += (attachedPhotoIds > 0) ? 1 : 0;
        point += (type == ReviewType.FIRST) ? 1 : 0;
    }
}
