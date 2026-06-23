package com.aihoo.domain.visit.util;

import com.aihoo.domain.visit.dto.VisitStatusDto;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class VisitStatusUtil {

    public static final String WAIT = "WAIT";
    public static final String UNSUBMITTED = "UNSUBMITTED";
    public static final String SUBMITTED = "SUBMITTED";
    public static final String STARTED = "STARTED";
    public static final String ENDED = "ENDED";
    public static final String REFUND_PROCESSING = "REFUND_PROCESSING";
    public static final String REFUNDED = "REFUNDED";
    public static final String DONE = "DONE";

    private static final Map<String, List<String>> nextStatusMap = Maps.newHashMap();
    private static final Map<String, List<String>> prevStatusMap = Maps.newHashMap();

    static {
        initStatusFlow();
    }

    private static void initStatusFlow() {
        nextStatusMap.put(WAIT, Arrays.asList(UNSUBMITTED, DONE));
        nextStatusMap.put(UNSUBMITTED, Arrays.asList(SUBMITTED, REFUND_PROCESSING));
        nextStatusMap.put(SUBMITTED, Arrays.asList(STARTED, REFUND_PROCESSING));
        nextStatusMap.put(STARTED, Collections.singletonList(ENDED));
        nextStatusMap.put(ENDED, Collections.emptyList());
        nextStatusMap.put(REFUND_PROCESSING, Collections.singletonList(REFUNDED));
        nextStatusMap.put(REFUNDED, Collections.emptyList());
        nextStatusMap.put(DONE, Collections.emptyList());

        for (Map.Entry<String, List<String>> entry : nextStatusMap.entrySet()) {
            String currentStatus = entry.getKey();
            List<String> nextStatusList = entry.getValue();

            for (String nextStatus : nextStatusList) {
                prevStatusMap.computeIfAbsent(nextStatus, k -> Lists.newArrayList()).add(currentStatus);
            }
        }
    }

    public static VisitStatusDto getStatusFlow(String currentStatus) {
        VisitStatusDto statusVo = new VisitStatusDto();
        statusVo.setNow(currentStatus);

        List<String> prevStatusList = prevStatusMap.getOrDefault(currentStatus, new ArrayList<>());
        statusVo.setBefore(prevStatusList);

        List<String> nextStatusList = nextStatusMap.getOrDefault(currentStatus, new ArrayList<>());
        statusVo.setAfter(nextStatusList);

        return statusVo;
    }

    public static boolean isValidTransition(String fromStatus, String toStatus) {
        List<String> allowedNextStatus = nextStatusMap.get(fromStatus);
        if (allowedNextStatus == null) {
            return false;
        }
        return allowedNextStatus.contains(toStatus);
    }

    public static List<String> getAllStatus() {
        return Lists.newArrayList(WAIT, UNSUBMITTED, SUBMITTED, STARTED, ENDED,
                REFUND_PROCESSING, REFUNDED, DONE);
    }

    public static List<String> getSuccessFlow() {
        return Lists.newArrayList(WAIT, UNSUBMITTED, SUBMITTED, STARTED, ENDED);
    }

    public static boolean isTerminalStatus(String status) {
        List<String> nextStatus = nextStatusMap.get(status);
        return nextStatus == null || nextStatus.isEmpty();
    }
}