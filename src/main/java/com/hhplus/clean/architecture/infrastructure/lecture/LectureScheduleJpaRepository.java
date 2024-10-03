package com.hhplus.clean.architecture.infrastructure.lecture;

import com.hhplus.clean.architecture.domain.lecture.LectureSchedule;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureScheduleJpaRepository extends JpaRepository<LectureSchedule,Long> {
    List<LectureSchedule> findByLectureIdOrderByScheduleDateAsc(Long lectureId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM LectureSchedule l WHERE l.id = :id")
    Optional<LectureSchedule> findByIdWithLock(@Param("id") Long id);

}
