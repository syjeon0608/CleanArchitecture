package com.hhplus.clean.architecture.domain.lecture.model;

import java.time.LocalDate;

public record ScheduleInfo(
        Long scheduleId,
        int capacity,
        LocalDate scheduleDate
) { }
