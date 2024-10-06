package com.hhplus.clean.architecture.domain.lecture.model;

import com.hhplus.clean.architecture.domain.lecture.LectureRegistration;
import com.hhplus.clean.architecture.domain.lecture.LectureSchedule;
import com.hhplus.clean.architecture.domain.user.User;

import java.time.LocalDate;

public record RegistrationInfo(
        Long registerId,
        Long userId,
        String userName,
        Long scheduleId,
        Long lectureId,
        String title,
        LocalDate scheduleDate
) {

    public static RegistrationInfo from(LectureRegistration registration) {
        LectureSchedule schedule = registration.getLectureSchedule();
        User user = registration.getUser();
        return new RegistrationInfo(registration.getId(),
                user.getId(),
                user.getName(),
                schedule.getId(),
                schedule.getLecture().getId(),
                schedule.getLecture().getTitle(),
                schedule.getScheduleDate()
        );
    }
}
