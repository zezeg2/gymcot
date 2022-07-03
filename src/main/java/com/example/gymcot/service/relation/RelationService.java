package com.example.gymcot.service.relation;

import com.example.gymcot.domain.diary.Exercise;
import com.example.gymcot.domain.relation.*;
import com.example.gymcot.repository.FriendRelationRepository;
import com.example.gymcot.repository.TogetherRelationRepository;
import com.example.gymcot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RelationService {

    private final UserRepository userRepository;
    private final FriendRelationRepository friendRelationRepository;
    private final TogetherRelationRepository togetherRelationRepository;

    public void makeRelation(Long sessionId, RelationRequestDto relationRequestDto){
        Relation relation = relationRequestDto.toEntity();
        relation.setFromUser(userRepository.findById(sessionId).get());
        relation.setToUser(userRepository.findByUsername(relationRequestDto.getToUsername()));
        if (relationRequestDto.getDtype().equals("t")){
            togetherRelationRepository.save((TogetherRelation) relation);
        } else if(relationRequestDto.getDtype().equals("f")){
            friendRelationRepository.save((FriendRelation) relation);
        } else{
            return;
        }
    }

    public void setStartAt(Long id, Long sessionId) {
        Optional<TogetherRelation> found = togetherRelationRepository.findById(id);
        if (!found.isPresent()){
            return;
        }
        TogetherRelation relation = found.get();
        if (relation.getToUser().getId() != sessionId) {
            return;
        }
        Exercise exercise = relation.getExercise();
        exercise.setStartAt(LocalDateTime.now());
        relation.setExercise(exercise);
    }

    public void setEndAt(Long id, Long sessionId) {
        Optional<TogetherRelation> found = togetherRelationRepository.findById(id);
        if (!found.isPresent()){
            return;
        }
        TogetherRelation relation = found.get();
        if (relation.getToUser().getId() != sessionId) {
            return;
        }
        Exercise exercise = relation.getExercise();
        exercise.setEndAt(LocalDateTime.now());
        relation.setExercise(exercise);
        relation.setCompleted(true);
    }

    public void approveRequest(Long sessionId, String username) {
        FriendRelation result = friendRelationRepository.findByFromUser_UsernameAndToUserId(username, sessionId);
        result.setApproved(true);
    }

    public List<RelationResponseDto> getApprovedList(Long sessionId) {
        List<RelationResponseDto> results = new ArrayList<>();
        friendRelationRepository.findByToUserIdAndApprovedIs(sessionId, true).forEach(o -> results.add(o.toDto()));
        return results;
    }

    public List<RelationResponseDto> getWaitingList(Long sessionId) {
        List<RelationResponseDto> results = new ArrayList<>();
        friendRelationRepository.findByToUserIdAndApprovedIs(sessionId, false).forEach(o -> results.add(o.toDto()));
        return results;
    }

    public List<RelationResponseDto> getTogetherList(Long sessionId, boolean completed) {
        List<RelationResponseDto> results = new ArrayList<>();
        togetherRelationRepository.findAllByToUserIdAndCompletedIs(completed).forEach(o -> results.add(o.toDto()));
        return results;
    }


//    public void approveRequest
}
