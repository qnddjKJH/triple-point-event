package com.triple.point.domain.points.repository;

import com.triple.point.domain.points.entity.PointHistory;
import com.triple.point.testDto.TestRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static com.triple.point.testDto.TestRequest.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PointHistoryRepositoryTest {
    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @DisplayName("생성 테스트")
    @Test
    public void create_test() throws Exception {
        // given
        TestRequest testRequest = new TestRequest();
        PointHistory pointHistory = testRequest.getRequest().toEntity();

        // when
        pointHistoryRepository.save(pointHistory);

        // then
        assertThat(pointHistory.getId()).isNotNull();
    }

    @DisplayName("사용자 내역 조회 테스트")
    @Test
    public void findByUserIdTest() throws Exception {
        // given
        TestRequest testRequest1 = new TestRequest();
        TestRequest testRequest2 = new TestRequest();

        PointHistory pointHistory1 = testRequest1.getRequest().toEntity();
        PointHistory pointHistory2 = testRequest2.getRequest().toEntity();
        pointHistoryRepository.save(pointHistory1);
        pointHistoryRepository.save(pointHistory2);

        // when
        List<PointHistory> userIds = pointHistoryRepository.findByUserId(testRequest1.getUuidMap().get(USER));

        // then
        assertThat(userIds.size()).isEqualTo(1);
        assertThat(userIds.get(0).getUserId()).isEqualTo(testRequest1.getUuidMap().get(USER));
    }

    @DisplayName("장소별 내역 조회 테스트")
    @Test
    public void findByPlaceIdTest() throws Exception {
        // given
        TestRequest testRequest1 = new TestRequest();
        TestRequest testRequest2 = new TestRequest();

        PointHistory pointHistory1 = testRequest1.getRequest().toEntity();
        PointHistory pointHistory2 = testRequest2.getRequest().toEntity();
        pointHistoryRepository.save(pointHistory1);
        pointHistoryRepository.save(pointHistory2);

        // when
        List<PointHistory> places = pointHistoryRepository.findByPlaceId(testRequest1.getUuidMap().get(PLACE));

        // then
        assertThat(places.size()).isEqualTo(1);
        assertThat(places.get(0).getPlaceId()).isEqualTo(testRequest1.getUuidMap().get(PLACE));
    }

    @DisplayName("리뷰별 내역 조회 테스트")
    @Test
    public void findByReviewIdTest() throws Exception {
        // given
        TestRequest testRequest1 = new TestRequest();
        TestRequest testRequest2 = new TestRequest();

        PointHistory pointHistory1 = testRequest1.getRequest().toEntity();
        PointHistory pointHistory2 = testRequest2.getRequest().toEntity();
        pointHistoryRepository.save(pointHistory1);
        pointHistoryRepository.save(pointHistory2);

        System.out.println("testRequest1.toString() = " + testRequest1.toString());

        // when
        List<PointHistory> reviews = pointHistoryRepository.findByReviewId(testRequest1.getUuidMap().get(REVIEW));
        System.out.println("reviews.get(0).toString() = " + reviews.get(0).toString());

        // then
        assertThat(reviews.size()).isEqualTo(1);
        assertThat(reviews.get(0).getReviewId()).isEqualTo(testRequest1.getUuidMap().get(REVIEW));
    }

}