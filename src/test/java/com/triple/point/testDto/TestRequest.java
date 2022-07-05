package com.triple.point.testDto;

import com.triple.point.domain.points.dto.PointHistoryRequest;
import com.triple.point.domain.common.type.ActionType;

import java.util.*;

public class TestRequest {
    public final static String USER = "userId";
    public final static String PLACE = "placeId";
    public final static String REVIEW = "reviewId";
    private Map<String, String> uuidMap;

    private PointHistoryRequest request;

    public TestRequest() {
        setUuidMap();
        setRequest();
    }

    private void setRequest() {
        request = new PointHistoryRequest(
                "REVIEW", randomAction(), uuidMap.get(REVIEW), randomContent(),
                randomPhoto(), uuidMap.get(USER), uuidMap.get(PLACE)
        );
    }

    private List<String> randomPhoto() {
        List<String> list = new ArrayList<>();
        int photoCount = new Random().nextInt(3);
        for (int i = 0; i < photoCount; i++) {
            list.add(UUID.randomUUID().toString());
        }
        return list;
    }

    private String randomAction() {
        String action = "";
        switch (new Random().nextInt(3)) {
            case 0:
                action = ActionType.ADD.name();
                break;
            case 1:
                action = ActionType.MOD.name();
                break;
            case 2:
                action = ActionType.DELETE.name();
                break;
            default:
                action = "ADD";
                break;
        }
        return action;
    }

    private String randomContent() {
        int leftLimit = 48;
        int rightLimit = 122;
        int length = 5;
        Random random = new Random();
        String randomContent = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return randomContent;
    }

    private void setUuidMap() {
        Map<String, String> map = new HashMap<>();
        map.put(USER, UUID.randomUUID().toString());
        map.put(PLACE, UUID.randomUUID().toString());
        map.put(REVIEW, UUID.randomUUID().toString());
        uuidMap = map;
    }

    public Map<String, String> getUuidMap() {
        return uuidMap;
    }

    public PointHistoryRequest getRequest() {
        return request;
    }

    @Override
    public String toString() {
        return "TestRequest{" +
                "uuidMap=" + uuidMap +
                ", request=" + request +
                '}';
    }
}
