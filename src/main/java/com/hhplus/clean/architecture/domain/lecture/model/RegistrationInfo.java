package com.hhplus.clean.architecture.domain.lecture.model;

import java.time.LocalDate;

public record RegistrationInfo(
        Long registerId,
        Long userId,
        String userName,
        Long scheduleId,
        Long lectureId,
        String title,
        LocalDate scheduleDate
) { }
