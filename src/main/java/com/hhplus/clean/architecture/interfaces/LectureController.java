package com.hhplus.clean.architecture.interfaces;

import com.hhplus.clean.architecture.application.LectureFacade;
import com.hhplus.clean.architecture.domain.lecture.Lecture;
import com.hhplus.clean.architecture.domain.lecture.LectureRegistration;
import com.hhplus.clean.architecture.domain.lecture.model.LectureInfo;
import com.hhplus.clean.architecture.interfaces.dto.GetLecturesRes;
import com.hhplus.clean.architecture.interfaces.dto.RegisterLectureReq;
import com.hhplus.clean.architecture.interfaces.dto.RegisterLectureRes;
import com.hhplus.clean.architecture.interfaces.dto.GetLectureWithScheduleRes;
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
    public ResponseEntity<RegisterLectureRes> registerLecture(
            @PathVariable Long lectureScheduleId, @RequestBody RegisterLectureReq userRequest) {

        LectureRegistration registration = lectureFacade.registerLecture(userRequest.id(), lectureScheduleId);
        RegisterLectureRes response = lectureMapper.toRegisterLectureRes(registration);

        return ResponseEntity.ok(response);
    }

    @GetMapping("lectures")
    public ResponseEntity<List<GetLecturesRes>> getLectures() {
        List<Lecture> lectures = lectureFacade.getLectures();
        List<GetLecturesRes> response = lectureMapper.toGetLecturesRes(lectures);
        return ResponseEntity.ok(response);
    }

    @GetMapping("lectures/{lectureId}")
    public ResponseEntity<GetLectureWithScheduleRes> getLectureWithSchedules(@PathVariable Long lectureId) {
        LectureInfo lectureInfo = lectureFacade.getLectureWithSchedules(lectureId);
        GetLectureWithScheduleRes response = lectureMapper.toGetLectureWithScheduleRes(lectureInfo);
        return ResponseEntity.ok(response);
    }
}
