package com.hhplus.clean.architecture.interfaces;

import com.hhplus.clean.architecture.domain.lecture.LectureRegistration;
import com.hhplus.clean.architecture.interfaces.dto.RegisterLectureRes;
import org.springframework.stereotype.Component;

@Component
public class LectureMapper {

    public RegisterLectureRes toRegisterLectureRes(LectureRegistration registration) {
        return new RegisterLectureRes(
                registration.getId(),
                registration.getUser().getId(),
                registration.getUser().getName(),
                registration.getLectureSchedule().getId(),
                registration.getLectureSchedule().getLecture().getId(),
                registration.getLectureSchedule().getLecture().getTitle(),
                registration.getLectureSchedule().getScheduleDate()
        );
    }

}
