package com.hhplus.clean.architecture.interfaces;

import com.hhplus.clean.architecture.application.LectureFacade;
import com.hhplus.clean.architecture.domain.lecture.LectureRegistration;
import com.hhplus.clean.architecture.interfaces.dto.RegisterLectureReq;
import com.hhplus.clean.architecture.interfaces.dto.RegisterLectureRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
