package com.hhplus.clean.architecture.domain.lecture;

import com.hhplus.clean.architecture.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lecture_registration", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "lecture_schedule_id"})
})
public class LectureRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_schedule_id", nullable = false)
    private LectureSchedule lectureSchedule;

    @Column(nullable = false)
    private LocalDateTime registerDateTime;

    public static LectureRegistration create(User user, LectureSchedule lectureSchedule) {
        return new LectureRegistration(null,user, lectureSchedule, LocalDateTime.now());
    }

}
