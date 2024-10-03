package com.hhplus.clean.architecture.infrastructure.lecture;

import com.hhplus.clean.architecture.domain.lecture.LectureSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureScheduleJpaRepository extends JpaRepository<LectureSchedule,Long> {
    List<LectureSchedule> findByLectureIdOrderByScheduleDateAsc(Long lectureId);
}
