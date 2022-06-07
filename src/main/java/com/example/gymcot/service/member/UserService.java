package com.example.gymcot.service.member;

import com.example.gymcot.domain.gym.Gym;
import com.example.gymcot.domain.member.Role;
import com.example.gymcot.domain.member.User;
import com.example.gymcot.domain.member.UserDto;
import com.example.gymcot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    private void validateDuplicateEmail(User user) {
        List<User> findUser = userRepository.findByEmail(user.getEmail());
        if (findUser.size() >= 3) {
            throw new IllegalStateException("같은 이메일로는 최대 3개의 계정까지만 허용됩니다");
        }
    }

    private void validateDuplicateUsername(User user) {
        User findUser = userRepository.findByUsername(user.getUsername());
        if (findUser != null) {
            throw new IllegalStateException("이미 존재하는 닉네임 입니다.");
        }
    }

    public void join(UserDto userDto) {
        User user = userDto.toEntity();
        validateDuplicateUsername(user);
        validateDuplicateEmail(user);
        user.setRole(Role.ROLE_MEMBER);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setAttendState(false);
        userRepository.save(user);
    }

    public void update(Long id, UserDto userDto) {
        User user = userRepository.findById(id).get();
        if (userDto.getUsername() != null) {
            validateDuplicateUsername(user);
            user.setUsername(userDto.getUsername());
        }
        if (userDto.getPassword() != null) {
            user.setPassword(encoder.encode(userDto.getPassword()));
        }
        if (userDto.getPhone() != null) user.setPhone(userDto.getPhone());
    }

    public void setGym(Long id, Gym gym) {
        User findUser = userRepository.findById(id).get();
        findUser.setGym(gym);
        userRepository.save(findUser);
    }

    public void toggleState(Long id) {
        User user = userRepository.findById(id).get();
        user.setAttendState(!user.isAttendState());
    }


    public void changeRole(Long id, String role) {
        User findUser = userRepository.findById(id).get();
        if (role.equals("manager")) {
            findUser.setRole(Role.ROLE_MANAGER);
        }
        else if (role.equals("admin")){
            findUser.setRole(Role.ROLE_ADMIN);
        }
        else
            return;
    }
}
