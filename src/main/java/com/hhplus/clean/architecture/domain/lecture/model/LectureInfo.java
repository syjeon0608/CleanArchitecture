package com.hhplus.clean.architecture.domain.lecture.model;

import com.hhplus.clean.architecture.domain.lecture.Lecture;

public record LectureInfo(
        Long lectureId,
        String title,
        String instructor
) {

    public static LectureInfo from(Lecture lecture) {
        return new LectureInfo(
                lecture.getId(),
                lecture.getTitle(),
                lecture.getInstructor()
        );
    }
}
