package com.triple.point.domain.points.controller;

import com.triple.point.domain.points.dto.PointHistoryRequest;
import com.triple.point.domain.points.dto.PointHistoryResponse;
import com.triple.point.domain.points.service.PointHistoryService;
import com.triple.point.proxy.EventServiceProxy;
import com.triple.point.testDto.TestRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PointsController.class)
class PointsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PointHistoryService pointHistoryService;

    @MockBean
    private EventServiceProxy eventServiceProxy;

    @DisplayName("[GET] 전체 내역 조회")
    @Test
    public void get_all_test() throws Exception {
        // given
        final TestRequest testRequest = new TestRequest();
        PointHistoryRequest request = testRequest.getRequest();
        Map<String, String> uuidMap = testRequest.getUuidMap();

        final TestRequest testRequest2 = new TestRequest();
        PointHistoryRequest request2 = testRequest2.getRequest();
        Map<String, String> uuidMap2 = testRequest2.getUuidMap();

        // when
        when(pointHistoryService.getAllPointHistories())
                .thenReturn(List.of(
                        new PointHistoryResponse(request.toEntity()),
                        new PointHistoryResponse(request2.toEntity())
                ));


        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/points")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].reviewId").value(uuidMap.get(TestRequest.REVIEW)))
                .andExpect(jsonPath("$[1].userId").value(uuidMap2.get(TestRequest.USER)));

    }


}