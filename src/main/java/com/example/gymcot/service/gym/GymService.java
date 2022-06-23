package com.example.gymcot.service.gym;

import com.example.gymcot.domain.gym.Gym;
import com.example.gymcot.domain.gym.GymRequestDto;
import com.example.gymcot.domain.gym.GymResponseDto;
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

    private void validateDuplicatedUser(Long userId) throws IllegalArgumentException{
        Gym findGym = gymRepository.findByUserId(userId);
        if (findGym != null) {
            throw new IllegalArgumentException("이미 헬스장 등록을 완료한 사용자입니다.");
        }
    }

    public void enroll(Long userId, GymRequestDto gymdto) {
        validateDuplicatedUser(userId);
        Gym gym = gymdto.toEntity(userId);
    }

    public void approve(Long gymId) {
        Gym gym = gymRepository.findById(gymId).get();
        gym.setApproved(true);
        User user = userRepository.findById(gym.getUserId()).get();
        user.setGym(gym);
    }

    public List<GymResponseDto> preEnrolledGymList() {
        List<GymResponseDto> result = new ArrayList<>();
        gymRepository.findAllByApprovedIsFalse().stream().forEach(o -> {
            result.add(o.toDto());
        });
        return result;
    }

    public List<GymResponseDto> enrolledGymList() {
        List<GymResponseDto> result = new ArrayList<>();
        gymRepository.findAllByApprovedIsTrue().stream().forEach(o -> {
            result.add(o.toDto());
        });
        return result;
    }

    public void update(Long sessionId, GymRequestDto gymDto) {
        Gym gym = gymRepository.findByUserId(sessionId);
        gym.setTitle(gymDto.getTitle());
        gym.setLink(gymDto.getLink());
        gym.setCategory(gymDto.getCategory());
        gym.setDescription(gymDto.getDescription());
        gym.setTelephone(gymDto.getTelephone());
        gym.setAddress(gymDto.getAddress());
        gym.setRoadAddress(gymDto.getRoadAddress());
        gym.setMapx(gymDto.getMapx());
        gym.setMapy(gymDto.getMapy());
    }

    public GymResponseDto getMyGym(Long sessionId) {
        return gymRepository.findByUserId(sessionId).toDto();
    }

    public List<GymResponseDto> searchEnrolledGymList(String title, String address, String roadAddress) {
        if (address == null && roadAddress == null){
            throw new IllegalArgumentException("동/읍/면 혹은 도로명은 필수 입력사항입니다.");
        }

        List<Gym> searched;
        List<GymResponseDto> result = new ArrayList<>();

        if (address == null){
            searched = gymRepository.findAllByTitleContainsAndRoadAddressContainsAndApprovedIsTrue(title, roadAddress);
        }
        else{
            searched = gymRepository.findAllByTitleContainsAndAddressContainsAndApprovedIsTrue(title, address);
        }
        searched.stream().forEach(o -> {
            result.add(o.toDto());
        });
        return result;
    }
}
