package com.hhplus.clean.architecture.interfaces;

import com.hhplus.clean.architecture.domain.lecture.Lecture;
import com.hhplus.clean.architecture.domain.lecture.LectureRegistration;
import com.hhplus.clean.architecture.domain.lecture.model.LectureDetail;
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

    public GetLectureWithScheduleRes toGetLectureWithScheduleRes(LectureDetail lectureDetail) {
        return new GetLectureWithScheduleRes(
                lectureDetail.lectureId(),
                lectureDetail.title(),
                lectureDetail.instructor(),
                lectureDetail.scheduleInfos()
        );
    }

    public List<GetRegisteredLecturesRes> toGetRegisteredLectureRes(List<LectureDetail> lectureDetails) {
        return lectureDetails.stream()
                .map(lectureInfo -> new GetRegisteredLecturesRes(
                        lectureInfo.lectureId(),
                        lectureInfo.title(),
                        lectureInfo.instructor(),
                        lectureInfo.scheduleInfos()
                ))
                .collect(Collectors.toList());
    }
}
