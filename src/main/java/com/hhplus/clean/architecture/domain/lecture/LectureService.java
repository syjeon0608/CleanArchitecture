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
        LectureSchedule schedule = lectureRepository.getLectureSchedule(lectureScheduleId);

        boolean isAlreadyRegisteredForLecture = lectureRepository.isUserAlreadyRegistered(user, schedule);
        if (isAlreadyRegisteredForLecture) {
            throw new BusinessException(DUPLICATE_ENROLLMENT);
        }

        schedule.reduceCapacity();
        LectureRegistration registration = LectureRegistration.create(user, schedule);
        RegistrationInfo registrationInfo = new RegistrationInfo(
                registration.getId(),user.getId(),
                user.getName(),
                schedule.getId(),
                schedule.getLecture().getId(),
                schedule.getLecture().getTitle(),
                schedule.getScheduleDate()
                );

        lectureRepository.completeLectureRegistration(registration);

        return registrationInfo;
    }

    @Transactional(readOnly = true)
    public List<LectureInfo> getLectureList() {
        List<Lecture> lectures = lectureRepository.getLectureList();

        List<LectureInfo> lectureInfos = lectures.stream()
                .map(lecture -> new LectureInfo(
                        lecture.getId(),
                        lecture.getTitle(),
                        lecture.getInstructor()
                ))
                .collect(Collectors.toList());

        return lectureInfos;
    }

    @Transactional(readOnly = true)
    public LectureDetail getLectureWithSchedule(Long lectureId){
        Lecture lecture = lectureRepository.getLecture(lectureId);
        List<LectureSchedule> schedules = lectureRepository.getLectureScheduleList(lectureId)
                .stream()
                .filter(schedule -> schedule.getCapacity() > 0)
                .toList();

        List<ScheduleInfo> scheduleInfos = schedules.stream()
                .map(schedule -> new ScheduleInfo(schedule.getId(), schedule.getCapacity(), schedule.getScheduleDate()))
                .toList();

        LectureDetail lectureDetail = new LectureDetail(lectureId, lecture.getTitle(), lecture.getInstructor(), scheduleInfos);

        return lectureDetail;
    }

    @Transactional(readOnly = true)
    public List<LectureDetail> getRegisteredLectures(Long userId) {
        userRepository.getUser(userId);

        List<LectureRegistration> registrations = lectureRepository.getRegistrationList(userId);
        Map<Lecture, List<ScheduleInfo>> lectureScheduleMap = new HashMap<>();

        for (LectureRegistration registration : registrations) {
            LectureSchedule lectureSchedule = registration.getLectureSchedule();
            Lecture lecture = lectureSchedule.getLecture();
            ScheduleInfo scheduleInfo = new ScheduleInfo(
                    lectureSchedule.getId(),
                    lectureSchedule.getCapacity(),
                    lectureSchedule.getScheduleDate()
            );

            lectureScheduleMap
                    .computeIfAbsent(lecture, k -> new ArrayList<>())
                    .add(scheduleInfo);
        }

        List<LectureDetail> registeredLectures = new ArrayList<>();
        for (Map.Entry<Lecture, List<ScheduleInfo>> entry : lectureScheduleMap.entrySet()) {
            Lecture lecture = entry.getKey();
            List<ScheduleInfo> scheduleInfos = entry.getValue();

            registeredLectures.add(new LectureDetail(
                    lecture.getId(),
                    lecture.getTitle(),
                    lecture.getInstructor(),
                    scheduleInfos
            ));
        }
        return registeredLectures;
    }

}
