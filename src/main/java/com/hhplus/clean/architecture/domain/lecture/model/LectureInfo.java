package com.hhplus.clean.architecture.domain.lecture.model;

import java.util.List;

public record LectureInfo(
        Long lectureId,
        String title,
        String instructor,
        List<ScheduleInfo> scheduleInfos
) { }
