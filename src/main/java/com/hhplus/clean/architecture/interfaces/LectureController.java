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

    @PostMapping("register/{lectureScheduleId}")
    public ResponseEntity<RegisterLectureRes> registerLecture(@PathVariable Long lectureScheduleId, @RequestBody RegisterLectureReq userRequest) {
        RegistrationInfo registration = lectureFacade.registerLecture(userRequest.id(), lectureScheduleId);
        return ResponseEntity.ok(RegisterLectureRes.from(registration));
    }

    @GetMapping("lectures")
    public ResponseEntity<List<GetLecturesRes>> getLectures() {
        List<LectureInfo> lectures = lectureFacade.getLectures();
        return ResponseEntity.ok(GetLecturesRes.from(lectures));
    }

    @GetMapping("lectures/{lectureId}")
    public ResponseEntity<GetLectureWithScheduleRes> getLectureWithSchedules(@PathVariable Long lectureId) {
        LectureDetail lectureDetail = lectureFacade.getLectureWithSchedules(lectureId);
        return ResponseEntity.ok(GetLectureWithScheduleRes.from(lectureDetail));
    }

    @GetMapping("/users/{userId}/lectures/registered")
    public ResponseEntity<List<GetRegisteredLecturesRes>> getRegisteredLectures(@PathVariable Long userId) {
        List<LectureDetail> lectureDetails = lectureFacade.getRegisteredLectures(userId);
        return ResponseEntity.ok(GetRegisteredLecturesRes.from(lectureDetails));
    }

}
