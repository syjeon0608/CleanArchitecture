package com.hhplus.clean.architecture.domain.lecture;

import com.hhplus.clean.architecture.domain.error.BusinessException;
import com.hhplus.clean.architecture.domain.user.User;
import com.hhplus.clean.architecture.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.hhplus.clean.architecture.domain.error.BusinessExceptionCode.DUPLICATE_ENROLLMENT;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;

    public LectureRegistration registerLecture(Long userId, Long lectureScheduleId) {
        User user = userRepository.getUser(userId);
        LectureSchedule schedule = lectureRepository.getLectureSchedule(lectureScheduleId);

        boolean isAlreadyRegisteredForLecture = lectureRepository.isUserAlreadyRegistered(user, schedule);
        if (isAlreadyRegisteredForLecture) {
            throw new BusinessException(DUPLICATE_ENROLLMENT);
        }

        schedule.reduceCapacity();
        LectureRegistration registration = LectureRegistration.create(user, schedule);
        LectureRegistration savedLecture =  lectureRepository.completeLectureRegistration(registration);

        return savedLecture;
    }


}
