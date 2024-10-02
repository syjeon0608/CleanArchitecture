package com.hhplus.clean.architecture.domain.lecture;

import com.hhplus.clean.architecture.domain.user.User;

public interface LectureRepository {

    LectureSchedule getLectureSchedule(Long lectureScheduleId);

    LectureRegistration completeLectureRegistration(LectureRegistration registration);

    boolean isUserAlreadyRegistered(User user, LectureSchedule schedule);
}
