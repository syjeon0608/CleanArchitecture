package com.hhplus.clean.architecture.interfaces;

import com.hhplus.clean.architecture.domain.lecture.Lecture;
import com.hhplus.clean.architecture.domain.lecture.LectureRegistration;
import com.hhplus.clean.architecture.domain.lecture.model.LectureInfo;
import com.hhplus.clean.architecture.interfaces.dto.GetLectureWithScheduleRes;
import com.hhplus.clean.architecture.interfaces.dto.GetLecturesRes;
import com.hhplus.clean.architecture.interfaces.dto.GetRegisteredLecturesRes;
import com.hhplus.clean.architecture.interfaces.dto.RegisterLectureRes;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LectureMapper {

    public RegisterLectureRes toRegisterLectureRes(LectureRegistration registration) {
        return new RegisterLectureRes(
                registration.getId(),
                registration.getUser().getId(),
                registration.getUser().getName(),
                registration.getLectureSchedule().getId(),
                registration.getLectureSchedule().getLecture().getId(),
                registration.getLectureSchedule().getLecture().getTitle(),
                registration.getLectureSchedule().getScheduleDate()
        );
    }

    public List<GetLecturesRes> toGetLecturesRes(List<Lecture> lectures) {
        return lectures.stream()
                .map(lecture -> new GetLecturesRes(
                        lecture.getId(),
                        lecture.getTitle(),
                        lecture.getInstructor()
                ))
                .collect(Collectors.toList());
    }

    public GetLectureWithScheduleRes toGetLectureWithScheduleRes(LectureInfo lectureInfo) {
        return new GetLectureWithScheduleRes(
                lectureInfo.lectureId(),
                lectureInfo.title(),
                lectureInfo.instructor(),
                lectureInfo.scheduleInfos()
        );
    }

    public List<GetRegisteredLecturesRes> toGetRegisteredLectureRes(List<LectureInfo> lectureInfos) {
        return lectureInfos.stream()
                .map(lectureInfo -> new GetRegisteredLecturesRes(
                        lectureInfo.lectureId(),
                        lectureInfo.title(),
                        lectureInfo.instructor(),
                        lectureInfo.scheduleInfos()
                ))
                .collect(Collectors.toList());
    }
}
