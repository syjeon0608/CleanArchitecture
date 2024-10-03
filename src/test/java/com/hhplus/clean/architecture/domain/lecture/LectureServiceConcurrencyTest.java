package com.hhplus.clean.architecture.domain.lecture;

import com.hhplus.clean.architecture.domain.error.BusinessException;
import com.hhplus.clean.architecture.domain.user.User;
import com.hhplus.clean.architecture.infrastructure.lecture.LectureJpaRepository;
import com.hhplus.clean.architecture.infrastructure.lecture.LectureScheduleJpaRepository;
import com.hhplus.clean.architecture.infrastructure.user.UserJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.hhplus.clean.architecture.domain.error.BusinessExceptionCode.LECTURE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class LectureServiceConcurrencyTest {

    @Autowired
    private LectureService lectureService;
    @Autowired
    private LectureScheduleJpaRepository scheduleJpaRepository;
    @Autowired
    private LectureJpaRepository lectureJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;


    @Test
    @DisplayName("40명의 사용자가 동시에 강의에 신청하면 30명만 성공해야 한다.")
    public void shouldHandleConcurrentLectureRegistrations() throws InterruptedException {
        LectureSchedule lectureSchedule = setupTestLectureSchedule();
        Long lectureScheduleId = lectureSchedule.getId();
        int numberOfThreads = 40;

        setupTestUsers(numberOfThreads);

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            final Long userId = (long) i + 1;

            executorService.submit(() -> {
                try {
                    lectureService.registerLecture(userId, lectureScheduleId);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        LectureSchedule updateLectureSchedule = scheduleJpaRepository.findById(lectureScheduleId)
                .orElseThrow(()-> new BusinessException(LECTURE_NOT_FOUND));

        assertEquals(0, updateLectureSchedule.getCapacity());
    }



    private LectureSchedule setupTestLectureSchedule() {
        Lecture lecture = new Lecture(1L,"Spring Boot 강의", "홍길동");
        lectureJpaRepository.save(lecture);
        LectureSchedule lectureSchedule = new LectureSchedule(1L, lecture, 30, LocalDate.of(2024, 10, 1));
        scheduleJpaRepository.save(lectureSchedule);
        return lectureSchedule;
    }

    private void setupTestUsers(int numberOfUsers) {
        for (int i = 1; i <= numberOfUsers; i++) {
            User user = new User((long) i, "user" + i);
            userJpaRepository.save(user);
        }
    }
}
