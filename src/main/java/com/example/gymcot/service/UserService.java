package com.example.gymcot.service;

import com.example.gymcot.domain.diary.Diary;
import com.example.gymcot.domain.user.Role;
import com.example.gymcot.domain.user.User;
import com.example.gymcot.domain.user.UserRequestDto;
import com.example.gymcot.domain.user.UserResponseDto;
import com.example.gymcot.repository.DiaryRepository;
import com.example.gymcot.repository.GymRepository;
import com.example.gymcot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final GymRepository gymRepository;

    private final DiaryRepository diaryRepository;

    private final BCryptPasswordEncoder encoder;

    private void validateDuplicateEmail(String email) throws IllegalArgumentException {
        List<User> findUser = userRepository.findByEmail(email);
        if (findUser.size() >= 3) {
            throw new IllegalArgumentException("같은 이메일로는 최대 3개의 계정까지만 허용됩니다");
        }
    }

    private void validateDuplicateUsername(String username) throws IllegalArgumentException {
        User findUser = userRepository.findByUsername(username);
        if (findUser != null) {
            throw new IllegalArgumentException("이미 존재하는 닉네임 입니다.");
        }
    }

    public void join(UserRequestDto userRequestDto) {
        User user = userRequestDto.toEntity();
        validateDuplicateUsername(user.getUsername());
        validateDuplicateEmail(user.getEmail());
        user.setRole(Role.ROLE_MEMBER);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setAttendState(false);
        user.setEnrolled(false);
        userRepository.save(user);
    }

    public User update(Long id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id).get();
        if (userRequestDto.getPassword() != null) {
            user.setPassword(encoder.encode(userRequestDto.getPassword()));
        }
        if (userRequestDto.getPhone() != null) user.setPhone(userRequestDto.getPhone());

        if (userRequestDto.getUsername() != null) {
            validateDuplicateUsername(userRequestDto.getUsername());
            user.setUsername(userRequestDto.getUsername());
        }
        return user;
    }

    public void setGym(Long id, Long gymId) {
        User findUser = userRepository.findById(id).get();
        findUser.setGym(gymRepository.findById(gymId).get());
        userRepository.save(findUser);
    }

    public void toggleState(Long id) {
        User user = userRepository.findById(id).get();
        if (user.isAttendState() == false) {
            user.setLatestAttendAt(LocalDateTime.now());
            user.setLatestFinishAt(null);
            List<Diary> todayDiaryList = diaryRepository.findByUserIdAndCreatedAtBetween(id
                    , LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0))
                    , LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59)));
            todayDiaryList.stream().forEach(o -> o.setAttended(true));
        } else {
            user.setLatestFinishAt(LocalDateTime.now());
        }
        user.setAttendState(!user.isAttendState());
    }


    public void changeRole(String username, String role) {
        User findUser = userRepository.findByUsername(username);
        if (role.equals("manager")) {
            findUser.setRole(Role.ROLE_MANAGER);
        } else if (role.equals("admin")) {
            findUser.setRole(Role.ROLE_ADMIN);
        } else
            findUser.setRole(Role.ROLE_MEMBER);
        ;
    }

    public UserResponseDto getUser(Long sessionId) {
        return userRepository.findById(sessionId).get().toDto();
    }

    public List<UserResponseDto> managerList() {
        List<UserResponseDto> results = new ArrayList<>();
        userRepository.findAllByRoleIs(Role.ROLE_MANAGER).stream().forEach(o -> {
            results.add(o.toDto());
        });
        return results;
    }

    public void approveMember(String username) {
        User user = userRepository.findByUsername(username);
        user.setEnrolled(true);
    }

    public void expelMember(String username) {
        User user = userRepository.findByUsername(username);
        user.setEnrolled(false);
    }

    public List<UserResponseDto> waitingList(Long sessionId) {
        List<UserResponseDto> results = new ArrayList<>();
        Long gymId = userRepository.findById(sessionId).get().getGym().getId();
        userRepository.findAllByEnrolledIsFalseAndGymIdIs(gymId).stream().forEach(o -> {
            results.add(o.toDto());
        });
        return results;
    }

    public List<UserResponseDto> enrolledList(Long sessionId) {
        List<UserResponseDto> results = new ArrayList<>();
        Long gymId = userRepository.findById(sessionId).get().getGym().getId();
        userRepository.findAllByEnrolledIsTrueAndGymIdIs(gymId).stream().forEach(o -> {
            results.add(o.toDto());
        });
        return results;
    }
}
