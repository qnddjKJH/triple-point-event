package com.triple.point.domain.points.repository;

import com.triple.point.config.AuditingConfig;
import com.triple.point.domain.common.type.ActionType;
import com.triple.point.domain.points.dto.PointHistoryRequest;
import com.triple.point.domain.points.entity.PointHistory;
import com.triple.point.testDto.TestRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static com.triple.point.testDto.TestRequest.*;
import static org.assertj.core.api.Assertions.assertThat;

@Import(AuditingConfig.class)
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

        // when
        List<PointHistory> reviews = pointHistoryRepository.findByReviewId(testRequest1.getUuidMap().get(REVIEW));

        // then
        assertThat(reviews.size()).isEqualTo(1);
        assertThat(reviews.get(0).getReviewId()).isEqualTo(testRequest1.getUuidMap().get(REVIEW));
    }

    @DisplayName("사용자가 장소에 리뷰 등록(ADD) 내역 조회")
    @Test
    public void findByUserIdAndPlaceIdAndActionTest() throws Exception {
        // given
        TestRequest testRequest1 = new TestRequest();
        PointHistoryRequest request1 = testRequest1.getRequest();

        TestRequest testRequest2 = new TestRequest();
        PointHistoryRequest request2 = testRequest2.getRequest();


        pointHistoryRepository.save(request1.toEntity());
        pointHistoryRepository.save(request2.toEntity());

        // when
        PointHistory pointHistory = pointHistoryRepository
                .findByUserIdAndPlaceIdAndAction(request1.getUserId(), request1.getPlaceId(), request1.getAction())
                .orElse(null);


        // then
        assertThat(pointHistory).isNotNull();
        assertThat(pointHistory.getUserId()).isEqualTo(request1.getUserId());
        assertThat(pointHistory.getAction()).isEqualTo(request1.getAction());
        assertThat(pointHistory.getUserId()).isNotEqualTo(request2.getUserId());
    }
    
    @DisplayName("리뷰 최근 내역 1건 조회")
    @Test
    public void findTopReviewIdOrderByCreatedAtDescTest() throws Exception {
        // given
        TestRequest testRequest1 = new TestRequest();
        PointHistoryRequest request1 = testRequest1.getRequest();
        TestRequest testRequest2 = new TestRequest();
        PointHistoryRequest request2 = testRequest2.getRequest();

        request2.setAction(ActionType.MOD.name());
        request2.setContent("");
        request2.setUserId(request1.getUserId());
        request2.setPlaceId(request1.getPlaceId());
        request2.setReviewId(request1.getReviewId());


        PointHistory save1 = pointHistoryRepository.save(request1.toEntity());
        Thread.sleep(1000);
        PointHistory save2 = pointHistoryRepository.save(request2.toEntity());

        // when
        PointHistory pointHistory = pointHistoryRepository
                .findTopByReviewIdOrderByCreatedAtDesc(request1.getReviewId())
                .orElse(null);

        // then
        assertThat(pointHistory).isNotNull();
        assertThat(pointHistory.getId()).isEqualTo(save2.getId());
        assertThat(pointHistory.getId()).isNotEqualTo(save1.getId());
        assertThat(pointHistory.getUserId()).isEqualTo(request2.getUserId());
        assertThat(pointHistory.getAction()).isEqualTo(request2.getAction());
    }

}