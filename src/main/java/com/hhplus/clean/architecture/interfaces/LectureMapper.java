package com.hhplus.clean.architecture.interfaces;

import com.hhplus.clean.architecture.domain.lecture.model.LectureDetail;
import com.hhplus.clean.architecture.domain.lecture.model.LectureInfo;
import com.hhplus.clean.architecture.domain.lecture.model.RegistrationInfo;
import com.hhplus.clean.architecture.interfaces.dto.GetLectureWithScheduleRes;
import com.hhplus.clean.architecture.interfaces.dto.GetLecturesRes;
import com.hhplus.clean.architecture.interfaces.dto.GetRegisteredLecturesRes;
import com.hhplus.clean.architecture.interfaces.dto.RegisterLectureRes;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LectureMapper {

    public RegisterLectureRes toRegisterLectureRes(RegistrationInfo registration) {
        return new RegisterLectureRes(
                registration.registerId(),
                registration.userId(),
                registration.userName(),
                registration.scheduleId(),
                registration.lectureId(),
                registration.title(),
                registration.scheduleDate()
        );
    }

    public List<GetLecturesRes> toGetLecturesRes(List<LectureInfo> lectures) {
        return lectures.stream()
                .map(lecture -> new GetLecturesRes(
                        lecture.lectureId(),
                        lecture.title(),
                        lecture.instructor()
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
