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

import static com.hhplus.clean.architecture.domain.error.BusinessExceptionCode.DUPLICATE_ENROLLMENT;
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

    @Test
    @DisplayName("동일한 유저가 동일한 특강을 5번 신청할 때 1번만 성공해야 한다.")
    public void shouldAllowOnlyOneRegistrationPerUser() throws InterruptedException {
        // given
        User user = new User(1L, "User1");
        userJpaRepository.save(user);

        Lecture lecture = new Lecture(1L, "Go Java!", "허 재");
        lectureJpaRepository.save(lecture);

        LectureSchedule lectureSchedule = new LectureSchedule(1L, lecture, 30, LocalDate.now());
        scheduleJpaRepository.save(lectureSchedule);


        int numberOfThreads = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        int[] successfulRegistrations = {0};
        int[] failedRegistrations = {0};

        // when
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    lectureService.registerLecture(user.getId(), lectureSchedule.getId());
                    successfulRegistrations[0]++;
                } catch (BusinessException e) {
                    assertEquals(DUPLICATE_ENROLLMENT, e.getErrorCode());
                    failedRegistrations[0]++;
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // then
        assertEquals(1, successfulRegistrations[0]);
        assertEquals(4,failedRegistrations[0]);
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
