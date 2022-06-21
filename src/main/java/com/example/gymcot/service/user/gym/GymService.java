package com.example.gymcot.service.user.gym;

import com.example.gymcot.domain.gym.Gym;
import com.example.gymcot.domain.gym.GymDto;
import com.example.gymcot.domain.user.User;
import com.example.gymcot.repository.GymRepository;
import com.example.gymcot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GymService {

    private final UserRepository userRepository;
    private final GymRepository gymRepository;

    public void enroll(Long userId, GymDto gymdto) {
        Gym gym = gymdto.toEntity(userId);
        gymRepository.save(gym);
    }

    public void approve(Long gymId){
        Gym gym = gymRepository.findById(gymId).get();
        gym.setApproved(true);
        User user = userRepository.findById(gym.getUserId()).get();
        user.setGym(gym);
    }

    public List<GymDto> preEnrolledGymList() {
        List<GymDto> result = new ArrayList<>();
        gymRepository.findAllByApprovedIsFalse().stream().forEach(o -> {
            result.add(o.toDto());
        });
        return result;
    }

    public List<GymDto> EnrolledGymList() {
        List<GymDto> result = new ArrayList<>();
        gymRepository.findAllByApprovedIsTrue().stream().forEach(o -> {
            result.add(o.toDto());
        });
        return result;
    }
}
