package com.hhplus.clean.architecture.interfaces.dto;

import com.hhplus.clean.architecture.domain.lecture.model.LectureDetail;
import com.hhplus.clean.architecture.domain.lecture.model.ScheduleInfo;

import java.util.List;
import java.util.stream.Collectors;

public record GetRegisteredLecturesRes(
        Long lectureId,
        String title,
        String instructor,
        List<ScheduleInfo> scheduleInfos
) {
    public static List<GetRegisteredLecturesRes> from(List<LectureDetail> lectureDetails) {
        return lectureDetails.stream()
                .map(lectureDetail -> new GetRegisteredLecturesRes(
                        lectureDetail.lectureId(),
                        lectureDetail.title(),
                        lectureDetail.instructor(),
                        lectureDetail.scheduleInfos()
                )).collect(Collectors.toList());
    }
}

