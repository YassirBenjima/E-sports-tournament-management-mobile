package com.esport.E_sports_tournament_management_system.repository;

import com.esport.E_sports_tournament_management_system.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByNameContainingIgnoreCase(String name);
    List<Team> findByCountry(String country);
    Optional<Team> findByName(String name);

    @Query("SELECT DISTINCT t.country FROM Team t")
    List<String> findDistinctCountries();

}
