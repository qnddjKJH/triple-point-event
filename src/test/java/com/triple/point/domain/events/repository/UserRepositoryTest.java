package com.triple.point.domain.events.repository;

import com.triple.point.config.AuditingConfig;
import com.triple.point.domain.events.dto.PointRequest;
import com.triple.point.domain.events.entity.Review;
import com.triple.point.domain.events.entity.User;
import com.triple.point.testDto.TestRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import(AuditingConfig.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void user_save_test() throws Exception {
        // given
        PointRequest request = new TestRequest().getRequest();
        User user = request.toUser();
        Review review = request.toNormalReview();

        // 미리 id 값을 넣어서 영속성 전이 하면 에러가남...
        // id 값을 넣어둔 상태이면 select 로 있는지 먼저 확인 하고 저장하기 때문에
        // 영속성 전이를 걸어둔 review 가 user 에 들어가면 review 를 먼저 찾는데 디비에는
        // review id 를 없으니깐 찾지 못한다... 그래서 미리 저장하고 user 와 연관관계를 맺어줘야한다.
        // when
        user.addReview(review);
//        reviewRepository.save(review);
        userRepository.save(user);

        // then
        assertThat(review.getId()).isNotNull();
        assertThat(user.getPoint()).isEqualTo(2);
    }

}