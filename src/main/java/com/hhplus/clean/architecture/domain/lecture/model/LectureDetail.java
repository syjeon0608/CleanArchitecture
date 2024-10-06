package com.hhplus.clean.architecture.domain.lecture.model;

import com.hhplus.clean.architecture.domain.lecture.Lecture;

import java.util.List;

public record LectureDetail(
        Long lectureId,
        String title,
        String instructor,
        List<ScheduleInfo> scheduleInfos
) {

    public static LectureDetail from(Lecture lecture, List<ScheduleInfo> scheduleInfos) {
        return new LectureDetail(
                lecture.getId(),
                lecture.getTitle(),
                lecture.getInstructor(),
                scheduleInfos
        );
    }
}
