package com.hhplus.clean.architecture.infrastructure.lecture;

import com.hhplus.clean.architecture.domain.lecture.LectureRegistration;
import com.hhplus.clean.architecture.domain.lecture.LectureSchedule;
import com.hhplus.clean.architecture.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRegistrationJpaRepository extends JpaRepository<LectureRegistration,Long> {
    boolean existsByUserAndLectureSchedule(User user, LectureSchedule lectureSchedule);
}
