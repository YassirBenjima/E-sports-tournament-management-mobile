package com.esport.E_sports_tournament_management_system.service;

import com.esport.E_sports_tournament_management_system.model.User;
import com.esport.E_sports_tournament_management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Récupérer tous les utilisateurs
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Ajouter un utilisateur
    public User addUser(User user) {
        return userRepository.save(user);
    }

    // Supprimer un utilisateur
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    // Trouver un utilisateur par son ID
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }
}
