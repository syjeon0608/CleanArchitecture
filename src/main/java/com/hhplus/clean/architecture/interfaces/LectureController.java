package com.hhplus.clean.architecture.interfaces;

import com.hhplus.clean.architecture.application.LectureFacade;
import com.hhplus.clean.architecture.domain.lecture.model.LectureDetail;
import com.hhplus.clean.architecture.domain.lecture.model.LectureInfo;
import com.hhplus.clean.architecture.domain.lecture.model.RegistrationInfo;
import com.hhplus.clean.architecture.interfaces.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class LectureController {

    private final LectureFacade lectureFacade;
    private final LectureMapper lectureMapper;

    @PostMapping("register/{lectureScheduleId}")
    public ResponseEntity<RegisterLectureRes> registerLecture(@PathVariable Long lectureScheduleId, @RequestBody RegisterLectureReq userRequest) {
        RegistrationInfo registration = lectureFacade.registerLecture(userRequest.id(), lectureScheduleId);
        RegisterLectureRes response = lectureMapper.toRegisterLectureRes(registration);
        return ResponseEntity.ok(response);
    }

    @GetMapping("lectures")
    public ResponseEntity<List<GetLecturesRes>> getLectures() {
        List<LectureInfo> lectures = lectureFacade.getLectures();
        List<GetLecturesRes> response = lectureMapper.toGetLecturesRes(lectures);
        return ResponseEntity.ok(response);
    }

    @GetMapping("lectures/{lectureId}")
    public ResponseEntity<GetLectureWithScheduleRes> getLectureWithSchedules(@PathVariable Long lectureId) {
        LectureDetail lectureDetail = lectureFacade.getLectureWithSchedules(lectureId);
        GetLectureWithScheduleRes response = lectureMapper.toGetLectureWithScheduleRes(lectureDetail);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{userId}/lectures/registered")
    public ResponseEntity<List<GetRegisteredLecturesRes>> getRegisteredLectures(@PathVariable Long userId){
        List<LectureDetail> lectureDetails = lectureFacade.getRegisteredLectures(userId);
        List<GetRegisteredLecturesRes> response = lectureMapper.toGetRegisteredLectureRes(lectureDetails);
        return  ResponseEntity.ok(response);
    }

}
