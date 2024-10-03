package com.hhplus.clean.architecture.interfaces.dto;

import java.time.LocalDate;

public record RegisterLectureRes(
        Long registrationId,
        Long userId,
        String userName,
        Long scheduleId,
        Long lectureId,
        String lectureTitle,
        LocalDate scheduleDate
) {}
