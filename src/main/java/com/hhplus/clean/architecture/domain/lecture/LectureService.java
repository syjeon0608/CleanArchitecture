package com.hhplus.clean.architecture.domain.lecture;

import com.hhplus.clean.architecture.domain.error.BusinessException;
import com.hhplus.clean.architecture.domain.lecture.model.LectureDetail;
import com.hhplus.clean.architecture.domain.lecture.model.LectureInfo;
import com.hhplus.clean.architecture.domain.lecture.model.RegistrationInfo;
import com.hhplus.clean.architecture.domain.lecture.model.ScheduleInfo;
import com.hhplus.clean.architecture.domain.user.User;
import com.hhplus.clean.architecture.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.hhplus.clean.architecture.domain.error.BusinessExceptionCode.DUPLICATE_ENROLLMENT;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;

    @Transactional
    public RegistrationInfo registerLecture(Long userId, Long lectureScheduleId) {
        User user = userRepository.getUser(userId);
        LectureSchedule schedule = lectureRepository.getLectureScheduleWithLock(lectureScheduleId);

        boolean isAlreadyRegisteredForLecture = lectureRepository.isUserAlreadyRegistered(user, schedule);
        if (isAlreadyRegisteredForLecture) {
            throw new BusinessException(DUPLICATE_ENROLLMENT);
        }

        schedule.reduceCapacity();
        LectureRegistration registration = LectureRegistration.create(user, schedule);

        lectureRepository.completeLectureRegistration(registration);

        return  RegistrationInfo.from(registration);
    }

    @Transactional(readOnly = true)
    public List<LectureInfo> getLectureList() {
        List<Lecture> lectures = lectureRepository.getLectureList();

        return lectures.stream()
                .map(LectureInfo::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LectureDetail getLectureWithSchedule(Long lectureId){
        Lecture lecture = lectureRepository.getLecture(lectureId);

        List<ScheduleInfo> scheduleInfos = lectureRepository.getLectureScheduleList(lectureId).stream()
                .filter(schedule -> schedule.getCapacity() > 0)
                .map(ScheduleInfo::from)
                .toList();

        return LectureDetail.from(lecture, scheduleInfos);
    }

    @Transactional(readOnly = true)
    public List<LectureDetail> getRegisteredLectures(Long userId) {
        userRepository.getUser(userId);

        List<LectureRegistration> registrations = lectureRepository.getRegistrationList(userId);
        Map<Lecture, List<ScheduleInfo>> lectureScheduleMap = new HashMap<>();

        for (LectureRegistration registration : registrations) {
            LectureSchedule lectureSchedule = registration.getLectureSchedule();
            Lecture lecture = lectureSchedule.getLecture();
            ScheduleInfo scheduleInfo = ScheduleInfo.from(lectureSchedule);

            lectureScheduleMap
                    .computeIfAbsent(lecture, k -> new ArrayList<>())
                    .add(scheduleInfo);
        }

        return lectureScheduleMap.entrySet().stream()
                .map(entry -> LectureDetail.from(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

}
