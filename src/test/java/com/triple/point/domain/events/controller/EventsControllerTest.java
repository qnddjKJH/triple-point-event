package com.triple.point.domain.events.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.triple.point.domain.common.type.ActionType;
import com.triple.point.domain.common.type.EventType;
import com.triple.point.domain.events.dto.PointRequest;
import com.triple.point.domain.events.dto.PointResponse;
import com.triple.point.domain.events.dto.ReviewResponse;
import com.triple.point.domain.events.entity.PointHistory;
import com.triple.point.domain.events.entity.Review;
import com.triple.point.domain.events.entity.User;
import com.triple.point.domain.events.service.EventsReviewPointService;
import com.triple.point.proxy.EventServiceProxy;
import com.triple.point.testDto.TestRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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
        PointRequest request1 = new TestRequest().getRequest();
        PointRequest request2 = new TestRequest().getRequest();

        // when
        when(eventsReviewPointService.getAllPointHistories())
                .thenReturn(List.of(
                        new PointResponse(request1.toHistory()),
                        new PointResponse(request2.toHistory())
                ));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/events")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].action").value(request1.getAction().name()))
                .andExpect(jsonPath("$.data[1].action").value(request2.getAction().name()));
    }

    @DisplayName("[POST] ADD EVENT")
    @Test
    public void post_add_test() throws Exception {
        // given
        PointRequest request1 = new TestRequest().getRequest();
        request1.setAction(ActionType.ADD.name());

        User user = request1.toUser();
        Review review = request1.toFirstReview();
        user.addReview(review);

        PointHistory history = request1.toHistory();
        history.setReviewInfo(review);
        history.setUserInto(user);

        // when
        when(eventServiceProxy.invoke(request1.getType(), request1.getAction(), request1))
                .thenReturn(new PointResponse(history));

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request1)))
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.action").value(request1.getAction().name()));
    }

    @DisplayName("[POST] MOD EVENT")
    @Test
    public void post_mod_test() throws Exception {
        // given
        PointRequest request1 = new TestRequest().getRequest();
        request1.setAction(ActionType.MOD.name());

        User user = request1.toUser();
        Review review = request1.toFirstReview();
        user.addReview(review);

        PointHistory history = request1.toHistory();
        history.setReviewInfo(review);
        history.setUserInto(user);

        // when
        when(eventServiceProxy.invoke(request1.getType(), request1.getAction(), request1))
                .thenReturn(new PointResponse(history));

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request1)))
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.action").value(request1.getAction().name()));
    }

    @DisplayName("[POST] DELETE EVENT")
    @Test
    public void post_delete_test() throws Exception {
        // given
        PointRequest request1 = new TestRequest().getRequest();
        request1.setAction(ActionType.DELETE.name());

        User user = request1.toUser();
        Review review = request1.toFirstReview();
        user.addReview(review);

        PointHistory history = request1.toHistory();
        history.setReviewInfo(review);
        history.setUserInto(user);

        // when
        when(eventServiceProxy.invoke(request1.getType(), request1.getAction(), request1))
                .thenReturn(new PointResponse(history));

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request1)))
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.action").value(request1.getAction().name()));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}