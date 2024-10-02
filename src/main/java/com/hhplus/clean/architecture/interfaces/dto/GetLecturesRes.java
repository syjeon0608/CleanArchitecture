package com.hhplus.clean.architecture.interfaces.dto;

public record GetLecturesRes (
        Long lectureId,
        String lectureTitle,
        String Instructor
) { }
