package com.hhplus.clean.architecture.interfaces.dto;

import com.hhplus.clean.architecture.domain.lecture.model.ScheduleInfo;

import java.util.List;

public record GetRegisteredLecturesRes(
        Long lectureId,
        String title,
        String instructor,
        List<ScheduleInfo> scheduleInfos
) { }
