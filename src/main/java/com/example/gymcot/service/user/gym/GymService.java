package com.example.gymcot.service.user.gym;

import com.example.gymcot.domain.gym.Gym;
import com.example.gymcot.domain.user.User;
import com.example.gymcot.repository.GymRepository;
import com.example.gymcot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GymService {

    private final UserRepository userRepository;
    private final GymRepository gymRepository;

    public void enroll(Long userId, Gym gym) {
        gymRepository.save(gym);
    }

    public void approve(Long userId, Long gymId){
        Gym gym = gymRepository.findById(gymId).get();
        gym.setApproved(true);
        User user = userRepository.findById(userId).get();
        user.setGym(gym);
    }
}
