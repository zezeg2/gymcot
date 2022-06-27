package com.example.gymcot.service.relation;

import com.example.gymcot.repository.RelationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RelationService {
    private final RelationRepository relationRepository;
}
