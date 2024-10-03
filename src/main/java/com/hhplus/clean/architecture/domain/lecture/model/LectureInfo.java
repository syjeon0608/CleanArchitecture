package com.hhplus.clean.architecture.domain.lecture.model;

public record LectureInfo(
        Long lectureId,
        String title,
        String instructor
) { }
