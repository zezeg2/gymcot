package com.example.gymcot.service;

import com.example.gymcot.domain.diary.Exercise;
import com.example.gymcot.domain.relation.*;
import com.example.gymcot.error.NotAllowedUserException;
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
        }
    }

    public void setStartAt(Long id, Long sessionId) {
        Optional<TogetherRelation> found = togetherRelationRepository.findById(id);
        found.ifPresent(o -> {
            if (o.getToUser().getId().equals(sessionId)) {
                Exercise exercise = o.getExercise();
                exercise.setStartAt(LocalDateTime.now());
                o.setExercise(exercise);

            } else{
                throw new NotAllowedUserException();
            }
        });
    }

    public void setEndAt(Long id, Long sessionId) {
        Optional<TogetherRelation> found = togetherRelationRepository.findById(id);
        found.ifPresent(o -> {
            if (o.getToUser().getId().equals(sessionId)) {
                Exercise exercise = o.getExercise();
                exercise.setEndAt(LocalDateTime.now());
                o.setExercise(exercise);
                o.setCompleted(true);

            } else{
                throw new NotAllowedUserException();
            }
        });
    }

    public void approveRequest(Long sessionId, String username) {
        friendRelationRepository.findByFromUser_UsernameAndToUserId(username, sessionId).ifPresent(o -> o.setApproved(true));

        FriendRelation friendRelation = new FriendRelation(true);
        friendRelation.setFromUser(userRepository.findById(sessionId).get());
        friendRelation.setToUser(userRepository.findByUsername(username));
        friendRelationRepository.save(friendRelation);
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
        togetherRelationRepository.findAllByToUserIdAndCompletedIs(sessionId,completed).forEach(o -> results.add(o.toDto()));
        return results;
    }


    public void deleteFriend(Long sessionId, String username) {
        Long fromUserId = userRepository.findByUsername(username).getId();
        togetherRelationRepository.findById(sessionId).ifPresent(o ->{
            friendRelationRepository.delete(friendRelationRepository.findByFromUserIdAndToUserId(sessionId, fromUserId).get());
            friendRelationRepository.delete(friendRelationRepository.findByFromUserIdAndToUserId(fromUserId, sessionId).get());
        });
    }

    public void deleteTogether(Long sessionId, String username) {
        Long fromUserId = userRepository.findByUsername(username).getId();
        togetherRelationRepository.findById(sessionId).ifPresent(o ->{
            friendRelationRepository.delete(friendRelationRepository.findByFromUserIdAndToUserId(sessionId, fromUserId).get());
            friendRelationRepository.delete(friendRelationRepository.findByFromUserIdAndToUserId(fromUserId, sessionId).get());
        });
    }
}
