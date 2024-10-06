package com.hhplus.clean.architecture.application;

import com.hhplus.clean.architecture.domain.lecture.LectureSchedule;
import com.hhplus.clean.architecture.domain.lecture.LectureService;
import com.hhplus.clean.architecture.domain.lecture.model.LectureDetail;
import com.hhplus.clean.architecture.domain.lecture.model.LectureInfo;
import com.hhplus.clean.architecture.domain.lecture.model.RegistrationInfo;
import com.hhplus.clean.architecture.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LectureFacade {

    private final LectureService lectureService;

    @Transactional
    public RegistrationInfo registerLecture(Long userId, Long lectureScheduleId) {
        User user = lectureService.getUser(userId);
        LectureSchedule schedule = lectureService.getLectureSchedule(lectureScheduleId);

        lectureService.validateAlreadyRegistration(user, schedule);
        return lectureService.registerUserForLecture(user, schedule);
    }

    public List<LectureInfo> getLectures(){
        return lectureService.getLectureList();
    }

    public LectureDetail getLectureWithSchedules(Long lectureId) {
        return lectureService.getLectureWithSchedule(lectureId);
    }

    public List<LectureDetail> getRegisteredLectures(Long userId) {
        return lectureService.getRegisteredLectures(userId);
    }
}
