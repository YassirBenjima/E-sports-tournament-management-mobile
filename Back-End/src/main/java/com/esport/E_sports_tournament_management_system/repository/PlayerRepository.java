package com.esport.E_sports_tournament_management_system.repository;

import com.esport.E_sports_tournament_management_system.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByUsernameContainingIgnoreCaseAndNationalityContainingIgnoreCase(String username, String nationality);

}
