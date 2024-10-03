package com.hhplus.clean.architecture.domain.lecture;

import com.hhplus.clean.architecture.domain.user.User;

import java.util.List;

public interface LectureRepository {

    LectureSchedule getLectureSchedule(Long lectureScheduleId);

    LectureRegistration completeLectureRegistration(LectureRegistration registration);

    boolean isUserAlreadyRegistered(User user, LectureSchedule schedule);

    List<Lecture> getLectureList();

    Lecture getLecture(Long lectureId);

    List<LectureSchedule> getLectureScheduleList(Long lectureId);

    List<LectureRegistration> getRegistrationList(Long userId);
}
