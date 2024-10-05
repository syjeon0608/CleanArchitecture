package com.hhplus.clean.architecture.domain.lecture.model;

import com.hhplus.clean.architecture.domain.lecture.LectureSchedule;

import java.time.LocalDate;

public record ScheduleInfo(
        Long scheduleId,
        int capacity,
        LocalDate scheduleDate
) {

    public static ScheduleInfo from(LectureSchedule schedule) {
        return new ScheduleInfo(
                schedule.getId(),
                schedule.getCapacity(),
                schedule.getScheduleDate()
        );
    }
}
