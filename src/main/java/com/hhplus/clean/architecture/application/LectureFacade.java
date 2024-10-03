package com.hhplus.clean.architecture.application;

import com.hhplus.clean.architecture.domain.lecture.Lecture;
import com.hhplus.clean.architecture.domain.lecture.LectureRegistration;
import com.hhplus.clean.architecture.domain.lecture.LectureService;
import com.hhplus.clean.architecture.domain.lecture.model.LectureInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LectureFacade {

    private final LectureService lectureService;

    public LectureRegistration registerLecture(Long userId, Long lectureScheduleId) {
        return lectureService.registerLecture(userId, lectureScheduleId);
    }

    public List<Lecture> getLectures(){
        return lectureService.getLectureList();
    }

    public LectureInfo getLectureWithSchedules(Long lectureId) {
        return lectureService.getLectureWithSchedule(lectureId);
    }

}
