package com.triple.point.domain.points.entity;

import com.triple.point.domain.common.BaseTimeEntity;
import com.triple.point.domain.points.dto.PointHistoryRequest;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "_userId", columnList = "userId"),
        @Index(name = "_placeId", columnList = "placeId"),
        @Index(name = "_reviewId", columnList = "reviewId"),
        @Index(name = "_action", columnList = "action"),
})
@Entity
public class PointHistory extends BaseTimeEntity {
    @Id
    @Column(name = "history_id", columnDefinition = "CHAR(36)")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "uuid-char")
    private UUID id;

    @Enumerated(value = EnumType.STRING)
    private EventType type;

    @Enumerated(value = EnumType.STRING)
    private ActionType action;
    @Builder.Default
    private Integer increasePoint = 0; // 지급 포인트 (증감된 포인트)
    @Builder.Default
    private Integer currentPoint = 0; // 현재 시점 포인트 추가, 수정, 삭제
    @Builder.Default
    @Column(name = "is_first_review")
    private boolean firstReview = false; // 첫 글 여부

    @Column(columnDefinition = "CHAR(36)")
    private String userId;

    @Column(columnDefinition = "CHAR(36)")
    private String placeId;

    @Column(columnDefinition = "CHAR(36)")
    private String reviewId;

    public static PointHistory createPointHistory(PointHistoryRequest request) {
        PointHistory history = PointHistory.builder()
                .userId(request.getUserId())
                .type(EventType.valueOf(request.getType()))
                .action(ActionType.valueOf(request.getAction()))
                .placeId(request.getPlaceId())
                .reviewId(request.getReviewId())
                .build();

        history.initPoint(request);
        return history;
    }

    public void calculationPoint() {
        increasePoint = increasePoint - currentPoint;
        currentPoint = currentPoint + increasePoint;
    }

    // 리뷰 수정
    public void modifyPoint(boolean firstReview, PointHistoryRequest request, Integer currentPoint) {
        if(firstReview) {
            bonusPoint();
        }
        setCurrentPoint(currentPoint);
        calculationPoint();
    }

    // 리뷰 삭제 포인트 회수
    public void deletePoint(Integer currentPoint, boolean firstReview) {
        if (firstReview) {
            bonusPoint();
        }

        this.increasePoint = currentPoint * -1;
        this.currentPoint = currentPoint + increasePoint;
    }

    private void initPoint(PointHistoryRequest request) {
        if (request.isContentLengthBonus()) {
            addPoint();
        }

        if (request.isAttachedPhotoIds()) {
            addPoint();
        }
    }

    private void setCurrentPoint(Integer currentPoint) {
        this.currentPoint = currentPoint;
    }

    // 보너스 포인트 증가
    public void bonusPoint() {
        if(!firstReview) {
            firstReview = true;
        }
            addPoint();
    }

    // 포인트 증가
    private void addPoint() {
        increasePoint += 1;
    }
}
