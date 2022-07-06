package com.triple.point.domain.events.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserPoint {

    @Id
    @Column(name = "user_point_id", columnDefinition = "CHAR(36)")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "uuid-char")
    private UUID id;

    private int point ;

    @OneToMany(mappedBy = "userPoint")
    private List<EventsReviewPoint> pointHistories = new ArrayList<>();

    public UserPoint(UUID id) {
        this.id = id;
        this.point = 0;
    }

    public void isContentLengthBonus(String content) {
        if(content.length() > 0){ addPoint();}
    }

    public void isAttachedPhotoIds(List<String> attachedPhotoIds) {
        if (attachedPhotoIds.size() > 0) {
            addPoint();
        }
    }

    public boolean checkPlaceReview(String placeId) {
        return pointHistories.stream()
                .filter(history -> history.getPlaceId().equals(placeId))
                .findFirst().isEmpty();
    }

    public void addPoint() {
        point += 1;
    }
}
