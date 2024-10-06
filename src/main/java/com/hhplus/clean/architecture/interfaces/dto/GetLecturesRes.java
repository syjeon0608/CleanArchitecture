package com.hhplus.clean.architecture.interfaces.dto;

import com.hhplus.clean.architecture.domain.lecture.model.LectureInfo;

import java.util.List;
import java.util.stream.Collectors;

public record GetLecturesRes (
        Long lectureId,
        String lectureTitle,
        String Instructor
) {
    public static List<GetLecturesRes> from(List<LectureInfo> lectures ) {
        return lectures.stream()
                .map(lecture -> new GetLecturesRes(
                        lecture.lectureId(),
                        lecture.title(),
                        lecture.instructor()
                ))
                .collect(Collectors.toList());
    }

}
