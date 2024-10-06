package com.hhplus.clean.architecture.infrastructure.lecture;

import com.hhplus.clean.architecture.domain.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureJpaRepository extends JpaRepository<Lecture,Long> {
}
