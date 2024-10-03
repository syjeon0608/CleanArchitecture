package com.hhplus.clean.architecture.infrastructure.lecture;

import com.hhplus.clean.architecture.domain.lecture.LectureRegistration;
import com.hhplus.clean.architecture.domain.lecture.LectureSchedule;
import com.hhplus.clean.architecture.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRegistrationJpaRepository extends JpaRepository<LectureRegistration,Long> {
    boolean existsByUserAndLectureSchedule(User user, LectureSchedule lectureSchedule);
    List<LectureRegistration> findByUserId(Long userId);
}
