package com.hhplus.clean.architecture.application;

import com.hhplus.clean.architecture.domain.lecture.LectureService;
import com.hhplus.clean.architecture.domain.lecture.model.LectureDetail;
import com.hhplus.clean.architecture.domain.lecture.model.LectureInfo;
import com.hhplus.clean.architecture.domain.lecture.model.RegistrationInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LectureFacade {

    private final LectureService lectureService;

    public RegistrationInfo registerLecture(Long userId, Long lectureScheduleId) {
        return lectureService.registerLecture(userId, lectureScheduleId);
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
