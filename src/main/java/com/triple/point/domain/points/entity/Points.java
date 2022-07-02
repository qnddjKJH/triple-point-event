package com.triple.point.domain.points.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "_userId", columnList = "userId"),
        @Index(name = "_placeId", columnList = "placeId"),
        @Index(name = "_reviewId", columnList = "reviewId"),
})
@Entity
public class Points {
    @Id
    @Column(name = "event_point_id")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "uuid-char")
    private UUID id;

    private String userId;

    private String placeId;

    private String reviewId;

    @Enumerated(value = EnumType.STRING)
    private EventType type;

    @Enumerated(value = EnumType.STRING)
    private ActionType action;

    private boolean isContent;

    private boolean isPicture;

    private boolean isBonus;

    private Integer point;

    @CreatedDate
    private LocalDateTime createdDate;
}
