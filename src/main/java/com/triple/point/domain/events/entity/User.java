package com.triple.point.domain.events.entity;

import com.triple.point.domain.common.entity.BaseTimeEntity;
import com.triple.point.domain.events.dto.PointRequest;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USERS")
@Entity
public class User extends BaseTimeEntity {
    @Id
    @Column(name = "user_id", columnDefinition = "CHAR(36)")
    @Type(type = "uuid-char")
    private UUID id;
    private int point = 0;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();


    public static User createUser(PointRequest request) {
        return User.builder()
                .id(UUID.fromString(request.getUserId()))
                .build();
    }

    public void addReview(Review review) {
        review.setUser(this);
        reviews.add(review);
    }

    public void addPoint(int point) {
        this.point += point;
    }
}
