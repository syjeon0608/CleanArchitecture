package com.hhplus.clean.architecture.domain.lecture;

import com.hhplus.clean.architecture.domain.error.BusinessException;
import com.hhplus.clean.architecture.domain.user.User;
import com.hhplus.clean.architecture.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static com.hhplus.clean.architecture.domain.error.BusinessExceptionCode.DUPLICATE_ENROLLMENT;
import static com.hhplus.clean.architecture.domain.error.BusinessExceptionCode.LECTURE_FULL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LectureServiceTest {

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LectureService lectureService;

    private User user;
    private Lecture lecture;

    @BeforeEach
    void setUp() {
        user = new User(1L, "UserA");
        when(userRepository.getUser(1L)).thenReturn(user);

        lecture = new Lecture(1L, "Go Java!", "허 재");
    }

    @Test
    @DisplayName("특강 신청이 성공적으로 이루어져야 한다")
    void shouldRegisterLectureSuccessfully() {
        //given
        LectureSchedule schedule = new LectureSchedule(1L, lecture, 30, LocalDate.now());
        when(lectureRepository.getLectureSchedule(1L)).thenReturn(schedule);
        when(lectureRepository.isUserAlreadyRegistered(user, schedule)).thenReturn(false);

        LectureRegistration registration = LectureRegistration.create(user, schedule);
        when(lectureRepository.completeLectureRegistration(any(LectureRegistration.class))).thenReturn(registration);

        //when
        LectureRegistration savedRegistration = lectureService.registerLecture(1L, 1L);

        //then
        assertNotNull(savedRegistration);
        assertEquals(user, savedRegistration.getUser());
        assertEquals(schedule, savedRegistration.getLectureSchedule());
    }


    @Test
    @DisplayName("이미 신청한 특강에 중복 신청 시 예외가 발생해야 한다")
    void shouldThrowExceptionWhenUserIsAlreadyRegistered() {
        // given
        LectureSchedule schedule = new LectureSchedule(1L, lecture, 30, LocalDate.now());
        when(lectureRepository.getLectureSchedule(1L)).thenReturn(schedule);
        when(lectureRepository.isUserAlreadyRegistered(user, schedule)).thenReturn(true);

        // when & then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> lectureService.registerLecture(1L, 1L));

        assertEquals(DUPLICATE_ENROLLMENT, exception.getErrorCode());
    }

    @Test
    @DisplayName("수강 인원이 마감된 경우 예외가 발생해야 한다")
    void shouldThrowExceptionWhenLectureIsFull() {
        // given
        LectureSchedule schedule = new LectureSchedule(1L, lecture, 0, LocalDate.now());
        when(lectureRepository.getLectureSchedule(1L)).thenReturn(schedule);
        when(lectureRepository.isUserAlreadyRegistered(user, schedule)).thenReturn(false);

        // when & then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> lectureService.registerLecture(1L, 1L));

        assertEquals(LECTURE_FULL, exception.getErrorCode());
    }

    @Test
    @DisplayName("특강 신청 후 수강인원이 정상적으로 감소해야 한다")
    void shouldDecreaseLectureCapacity() {
        // given
        LectureSchedule schedule = new LectureSchedule(1L, lecture, 30, LocalDate.now());
        when(lectureRepository.getLectureSchedule(1L)).thenReturn(schedule);
        when(lectureRepository.isUserAlreadyRegistered(user, schedule)).thenReturn(false);

        // when
        LectureRegistration registration = lectureService.registerLecture(1L, 1L);

        // then
        assertEquals(29, schedule.getCapacity());
    }


}