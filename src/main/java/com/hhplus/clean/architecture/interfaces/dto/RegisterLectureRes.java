package com.hhplus.clean.architecture.interfaces.dto;

import com.hhplus.clean.architecture.domain.lecture.model.RegistrationInfo;

import java.time.LocalDate;

public record RegisterLectureRes(
        Long registrationId,
        Long userId,
        String userName,
        Long scheduleId,
        Long lectureId,
        String lectureTitle,
        LocalDate scheduleDate
) {
    public static RegisterLectureRes from(RegistrationInfo registrationInfo) {
        return new RegisterLectureRes(
                registrationInfo.registerId(),
                registrationInfo.userId(),
                registrationInfo.userName(),
                registrationInfo.scheduleId(),
                registrationInfo.lectureId(),
                registrationInfo.title(),
                registrationInfo.scheduleDate()
        );
    }
}
