package com.hhplus.clean.architecture.domain.lecture;

import com.hhplus.clean.architecture.domain.error.BusinessException;
import com.hhplus.clean.architecture.domain.lecture.model.LectureDetail;
import com.hhplus.clean.architecture.domain.lecture.model.LectureInfo;
import com.hhplus.clean.architecture.domain.lecture.model.RegistrationInfo;
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
import java.util.Arrays;
import java.util.List;

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
        lecture = new Lecture(1L, "Go Java!", "허 재");
    }

    @Test
    @DisplayName("특강 신청이 성공적으로 이루어져야 한다")
    void shouldRegisterLectureSuccessfully() {
        //given
        when(userRepository.getUser(1L)).thenReturn(user);
        LectureSchedule schedule = new LectureSchedule(1L, lecture, 30, LocalDate.now());
        when(lectureRepository.getLectureSchedule(1L)).thenReturn(schedule);
        when(lectureRepository.isUserAlreadyRegistered(user, schedule)).thenReturn(false);

        LectureRegistration registration = LectureRegistration.create(user, schedule);
        when(lectureRepository.completeLectureRegistration(any(LectureRegistration.class))).thenReturn(registration);

        //when
        RegistrationInfo savedRegistration = lectureService.registerLecture(1L, 1L);

        //then
        assertNotNull(savedRegistration);
        assertEquals(1L, savedRegistration.userId());
        assertEquals(1L, savedRegistration.scheduleId());
    }


    @Test
    @DisplayName("이미 신청한 특강에 중복 신청 시 예외가 발생해야 한다")
    void shouldThrowExceptionWhenUserIsAlreadyRegistered() {
        // given
        when(userRepository.getUser(1L)).thenReturn(user);
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
        when(userRepository.getUser(1L)).thenReturn(user);
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
        when(userRepository.getUser(1L)).thenReturn(user);
        LectureSchedule schedule = new LectureSchedule(1L, lecture, 30, LocalDate.now());
        when(lectureRepository.getLectureSchedule(1L)).thenReturn(schedule);
        when(lectureRepository.isUserAlreadyRegistered(user, schedule)).thenReturn(false);

        // when
        RegistrationInfo registration = lectureService.registerLecture(1L, 1L);

        // then
        assertEquals(29, schedule.getCapacity());
    }

    @Test
    @DisplayName("전체 특강목록을 조회한다")
    void shouldGetLectureSuccessfully() {
        //given
        Lecture lecture1 = new Lecture(1L, "Go Java!", "허 재");
        Lecture lecture2 = new Lecture(2L, "Go python!", "렌");
        Lecture lecture3 = new Lecture(3L, "Go C#!", "로이");
        when(lectureRepository.getLectureList()).thenReturn(Arrays.asList(lecture1, lecture2, lecture3));

        //when
        List<LectureInfo> lectures = lectureService.getLectureList();
        //then
        assertNotNull(lectures);
        assertEquals(3, lectures.size());
        assertEquals(3L, lectures.get(2).lectureId());
    }

    @Test
    @DisplayName("수강 신청 가능한 날짜별 특강 목록을 조회한다.")
    void shouldGetAvailableLectureSchedulesByLectureId(){
        //given
        Lecture lecture = new Lecture(1L, "Go Java!", "허 재");
        when(lectureRepository.getLecture(1L)).thenReturn(lecture);

        LectureSchedule schedule1 = new LectureSchedule(1L, lecture, 30, LocalDate.of(2024, 10, 1));
        LectureSchedule schedule2 = new LectureSchedule(2L, lecture, 20, LocalDate.of(2024, 10, 2));
        LectureSchedule schedule3 = new LectureSchedule(3L, lecture, 20, LocalDate.of(2024, 10, 3));
        when(lectureRepository.getLectureScheduleList(1L)).thenReturn(Arrays.asList(schedule1, schedule2, schedule3));

        //when
        LectureDetail lectureDetail = lectureService.getLectureWithSchedule(lecture.getId());

        //then
        assertEquals(3, lectureDetail.scheduleInfos().size());
        assertEquals(30, lectureDetail.scheduleInfos().get(0).capacity());
        assertEquals(LocalDate.of(2024, 10, 3), lectureDetail.scheduleInfos().get(2).scheduleDate());

    }

    @Test
    @DisplayName("특강 인원이 마감된 강의는 목록에서 조회되지 않는다.")
    void shouldExcludeFullyBookedSchedulesFromLectureList(){
        //given
        Lecture lecture = new Lecture(1L, "Go Java!", "허 재");
        when(lectureRepository.getLecture(1L)).thenReturn(lecture);

        LectureSchedule schedule1 = new LectureSchedule(1L, lecture, 0, LocalDate.of(2024, 10, 1));
        LectureSchedule schedule2 = new LectureSchedule(2L, lecture, 0, LocalDate.of(2024, 10, 2));
        LectureSchedule schedule3 = new LectureSchedule(3L, lecture, 20, LocalDate.of(2024, 10, 3));
        when(lectureRepository.getLectureScheduleList(1L)).thenReturn(Arrays.asList(schedule1, schedule2, schedule3));

        //when
        LectureDetail lectureDetail = lectureService.getLectureWithSchedule(lecture.getId());

        //then
        assertEquals(1, lectureDetail.scheduleInfos().size());
        assertEquals(3L, lectureDetail.scheduleInfos().get(0).scheduleId());
    }

    @Test
    @DisplayName("특강 신청 완료한 목록을 조회한다")
    void shouldGetRegisteredLectureSchedulesByUserId(){
        //given
        Lecture lecture = new Lecture(1L, "Go Java!", "허 재");
        Lecture lecture2 = new Lecture(2L, "Go python!", "토투");

        when(lectureRepository.getRegistrationList(1L)).thenReturn(List.of(
                LectureRegistration.create(user, new LectureSchedule(1L, lecture, 30, LocalDate.now())),
                LectureRegistration.create(user, new LectureSchedule(2L, lecture2, 20, LocalDate.now()))
        ));

        //when
        List<LectureDetail> lectureDetails = lectureService.getRegisteredLectures(user.getId());

        //then
        assertEquals(1L, lectureDetails.get(0).lectureId());
        assertEquals("Go python!", lectureDetails.get(1).title());
    }

}