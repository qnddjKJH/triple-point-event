package com.triple.point.domain.events.controller;

import com.triple.point.domain.events.dto.EventReviewPointRequest;
import com.triple.point.domain.events.dto.EventReviewPointResponse;
import com.triple.point.domain.events.service.EventsReviewPointService;
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

@WebMvcTest(EventsController.class)
class EventsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EventsReviewPointService eventsReviewPointService;

    @MockBean
    private EventServiceProxy eventServiceProxy;

    @DisplayName("[GET] 전체 내역 조회")
    @Test
    public void get_all_test() throws Exception {
        // given
        final TestRequest testRequest = new TestRequest();
        EventReviewPointRequest request = testRequest.getRequest();
        Map<String, String> uuidMap = testRequest.getUuidMap();

        final TestRequest testRequest2 = new TestRequest();
        EventReviewPointRequest request2 = testRequest2.getRequest();
        Map<String, String> uuidMap2 = testRequest2.getUuidMap();

        // when
        when(eventsReviewPointService.getAllPointHistories())
                .thenReturn(List.of(
                        new EventReviewPointResponse(request.toEntity()),
                        new EventReviewPointResponse(request2.toEntity())
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