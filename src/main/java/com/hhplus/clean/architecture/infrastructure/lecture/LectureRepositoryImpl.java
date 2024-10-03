package com.hhplus.clean.architecture.infrastructure.lecture;

import com.hhplus.clean.architecture.domain.error.BusinessException;
import com.hhplus.clean.architecture.domain.lecture.Lecture;
import com.hhplus.clean.architecture.domain.lecture.LectureRegistration;
import com.hhplus.clean.architecture.domain.lecture.LectureRepository;
import com.hhplus.clean.architecture.domain.lecture.LectureSchedule;
import com.hhplus.clean.architecture.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.hhplus.clean.architecture.domain.error.BusinessExceptionCode.LECTURE_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class LectureRepositoryImpl implements LectureRepository {
    private final LectureJpaRepository lectureJpaRepository;
    private final LectureScheduleJpaRepository scheduleJpaRepository;
    private final LectureRegistrationJpaRepository registrationJpaRepository;

    @Override
    public LectureSchedule getLectureSchedule(Long lectureScheduleId) {
            return scheduleJpaRepository.findById(lectureScheduleId)
                    .orElseThrow(() -> new BusinessException(LECTURE_NOT_FOUND));
    }

    @Override
    public LectureRegistration completeLectureRegistration(LectureRegistration registration) {
        return registrationJpaRepository.save(registration);
    }

    @Override
    public boolean isUserAlreadyRegistered(User user, LectureSchedule schedule) {
        return registrationJpaRepository.existsByUserAndLectureSchedule(user,schedule);
    }

    @Override
    public List<Lecture> getLectureList() {
        List<Lecture> lectures = lectureJpaRepository.findAll();
        if(lectures.isEmpty()) {
            throw new BusinessException(LECTURE_NOT_FOUND);
        }
        return lectures;
    }

    @Override
    public Lecture getLecture(Long lectureId) {
        return lectureJpaRepository.findById(lectureId)
                .orElseThrow(() -> new BusinessException(LECTURE_NOT_FOUND));
    }

    @Override
    public List<LectureSchedule> getLectureScheduleList(Long lectureId) {
        List<LectureSchedule> schedules = scheduleJpaRepository.findByLectureIdOrderByScheduleDateAsc(lectureId);
        if(schedules.isEmpty()){
            throw new BusinessException(LECTURE_NOT_FOUND);
        }
        return schedules;
    }

}
