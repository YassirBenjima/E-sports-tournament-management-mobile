package com.esport.E_sports_tournament_management_system.service;

import com.esport.E_sports_tournament_management_system.model.MatchResult;
import com.esport.E_sports_tournament_management_system.repository.MatchResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MatchResultService {

    private final MatchResultRepository matchResultRepository;

    @Autowired
    public MatchResultService(MatchResultRepository repository) {
        this.matchResultRepository = repository;
    }

    public MatchResult save(MatchResult result) {
        return matchResultRepository.save(result);
    }

    public Optional<MatchResult> findById(Long id) {
        return matchResultRepository.findById(id);
    }

    public void delete(Long id) {
        matchResultRepository.deleteById(id);
    }
}

