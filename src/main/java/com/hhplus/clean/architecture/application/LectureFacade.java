package com.hhplus.clean.architecture.application;

import com.hhplus.clean.architecture.domain.lecture.LectureRegistration;
import com.hhplus.clean.architecture.domain.lecture.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LectureFacade {

    private final LectureService lectureService;

    public LectureRegistration registerLecture(Long userId, Long lectureScheduleId) {
        return lectureService.registerLecture(userId, lectureScheduleId);
    }

}
