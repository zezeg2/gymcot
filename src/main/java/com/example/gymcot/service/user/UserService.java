package com.example.gymcot.service.user;

import com.example.gymcot.domain.gym.Gym;
import com.example.gymcot.domain.gym.GymDto;
import com.example.gymcot.domain.user.Role;
import com.example.gymcot.domain.user.User;
import com.example.gymcot.domain.user.UserDto;
import com.example.gymcot.domain.user.UserUpdateDto;
import com.example.gymcot.repository.GymRepository;
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

    private final GymRepository gymRepository;

    private final BCryptPasswordEncoder encoder;

    private void validateDuplicateEmail(String email) throws IllegalArgumentException {
        List<User> findUser = userRepository.findByEmail(email);
        if (findUser.size() >= 3) {
            throw new IllegalArgumentException("같은 이메일로는 최대 3개의 계정까지만 허용됩니다");
        }
    }

    private void validateDuplicateUsername(String username) throws IllegalArgumentException{
        User findUser = userRepository.findByUsername(username);
        if (findUser != null) {
            throw new IllegalArgumentException("이미 존재하는 닉네임 입니다.");
        }
    }

    public void join(UserDto userDto) {
        User user = userDto.toEntity();
        validateDuplicateUsername(user.getUsername());
        validateDuplicateEmail(user.getEmail());
        user.setRole(Role.ROLE_MEMBER);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setAttendState(false);
        userRepository.save(user);
    }

    public User update(Long id, UserUpdateDto userDto) {
        User user = userRepository.findById(id).get();
        if (userDto.getPassword() != null) {
            user.setPassword(encoder.encode(userDto.getPassword()));
        }
        if (userDto.getPhone() != null) user.setPhone(userDto.getPhone());

        if (userDto.getUsername() != null) {
            validateDuplicateUsername(userDto.getUsername());
            user.setUsername(userDto.getUsername());
        }
        return user;
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


    public void changeRole(String username, String role) {
        User findUser = userRepository.findByUsername(username);
        if (role.equals("manager")) {
            findUser.setRole(Role.ROLE_MANAGER);
        }
        else if (role.equals("admin")){
            findUser.setRole(Role.ROLE_ADMIN);
        }
        else
            findUser.setRole(Role.ROLE_MEMBER);;
    }

    public void enrollGym(GymDto gymDto) {
        gymRepository.save(gymDto.toEntity());
    }
}
