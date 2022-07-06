package com.triple.point.domain.events.repository;

import com.triple.point.config.AuditingConfig;
import com.triple.point.domain.common.type.ReviewType;
import com.triple.point.domain.events.dto.PointRequest;
import com.triple.point.domain.events.entity.Review;
import com.triple.point.testDto.TestRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import(AuditingConfig.class)
@DataJpaTest
class ReviewRepositoryTest {
    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void review_save_test() throws Exception {
        // given
        PointRequest request = new TestRequest().getRequest();
        Review normal = request.toNormalReview();

        PointRequest req = new TestRequest().getRequest();
        Review first = req.toFirstReview();

        // when
        reviewRepository.save(normal);
        reviewRepository.save(first);

        // then
        assertThat(normal.getId()).isNotNull();
        assertThat(normal.getType()).isEqualTo(ReviewType.NORMAL);
        assertThat(normal.getPoint()).isEqualTo(2);

        assertThat(first.getId()).isNotNull();
        assertThat(first.getType()).isEqualTo(ReviewType.FIRST);
        assertThat(first.getPoint()).isEqualTo(3);
    }
}