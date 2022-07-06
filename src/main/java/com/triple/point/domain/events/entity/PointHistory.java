package com.triple.point.domain.events.entity;

import com.triple.point.domain.common.entity.BaseCreatedTimeEntity;
import com.triple.point.domain.common.type.ActionType;
import com.triple.point.domain.common.type.EventType;
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
@Table(indexes = {
        @Index(name = "_userId", columnList = "userId"),
        @Index(name = "_placeId", columnList = "placeId"),
        @Index(name = "_reviewId", columnList = "reviewId"),
        @Index(name = "_action", columnList = "action"),
})
@Entity
public class PointHistory extends BaseCreatedTimeEntity {
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
    private Integer userTotal = 0; // 이력 시점 유저 총점
    @Builder.Default
    private Integer increasePoint = 0; // 지급 포인트 (증감된 포인트)
    @Builder.Default
    private Integer reviewPoint = 0; // 이력 시점 리뷰 점수

    @Enumerated(value = EnumType.STRING)
    private ReviewType reviewType;

    @Column(columnDefinition = "CHAR(36)")
    private String userId;
    @Column(columnDefinition = "CHAR(36)")
    private String reviewId;

    @Column(columnDefinition = "CHAR(36)")
    private String placeId;

    public static PointHistory createPointHistory(PointRequest request) {
        PointHistory history = PointHistory.builder()
                .type(request.getType())
                .action(request.getAction())
                .build();

        return history;
    }

    public void setUserInto(User user) {
        userTotal = user.getPoint();
        userId = user.getId().toString();
    }

    public void setReviewInfo(Review review) {
        reviewId = review.getId().toString();
        placeId = review.getPlaceId();
        reviewPoint = review.getPoint();
        reviewType = review.getType();
    }

    public void setIncreasePoint(Integer increasePoint) {
        this.increasePoint = increasePoint;
    }
}
