package com.triple.point.domain.events.repository;

import com.triple.point.config.AuditingConfig;
import com.triple.point.domain.events.entity.PointHistory;
import com.triple.point.testDto.TestRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Import(AuditingConfig.class)
@DataJpaTest
class EventsPointHistoryRepositoryTest {
    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @DisplayName("생성 테스트")
    @Test
    public void create_test() throws Exception {
        // given
        TestRequest testRequest = new TestRequest();
        PointHistory pointHistory = testRequest.getRequest().toHistory();

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

        PointHistory pointHistory1 = testRequest1.getRequest().toHistory();
        PointHistory pointHistory2 = testRequest2.getRequest().toHistory();
        pointHistoryRepository.save(pointHistory1);
        pointHistoryRepository.save(pointHistory2);

        // when
        List<PointHistory> userIds = pointHistoryRepository.findByUserId(pointHistory1.getUserId());

        // then
        assertThat(userIds.size()).isEqualTo(1);
    }
}