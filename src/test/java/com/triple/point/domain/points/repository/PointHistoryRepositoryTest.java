package com.triple.point.domain.points.repository;

import com.triple.point.domain.points.dto.PointHistoryRequest;
import com.triple.point.domain.points.entity.PointHistory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class PointHistoryRepositoryTest {

    /*
        {
            "type": "REVIEW",
            "action": "ADD",
            "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
            "content": "좋아요!",
            "attachedPhotoIds": ["e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"],
            "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            "placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
        }
    */

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @Test
    public void create_test() throws Exception {
        // given
        PointHistoryRequest request = new PointHistoryRequest(
                "REVIEW", "ADD", "240a0658", "좋아요!",
                List.of("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"),
                "3ede0ef2-92b7-4817-a5f3-0c575361f745",
                "2e4baf1c-5acb-4efb-a1af-eddada31b00f");
        PointHistory pointHistory = request.toEntity();

        // when
        pointHistoryRepository.save(pointHistory);

        // then
        assertThat(pointHistory.getId()).isNotNull();
    }

}