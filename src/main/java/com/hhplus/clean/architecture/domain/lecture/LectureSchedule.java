package com.hhplus.clean.architecture.domain.lecture;

import com.hhplus.clean.architecture.domain.error.BusinessException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static com.hhplus.clean.architecture.domain.error.BusinessExceptionCode.LECTURE_FULL;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LectureSchedule {

    private static final int MAX_CAPACITY = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;


    @Column(nullable = false)
    private int capacity = MAX_CAPACITY;

    @Column(nullable = false)
    private LocalDate scheduleDate;

    public void reduceCapacity() {
        if (this.capacity <= 0) {
            throw new BusinessException(LECTURE_FULL);
        }
        this.capacity -= 1;
    }


}
