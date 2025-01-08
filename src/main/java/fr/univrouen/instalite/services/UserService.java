package fr.univrouen.instalite.services;

import fr.univrouen.instalite.entities.User;
import fr.univrouen.instalite.exceptions.ResourceNotFoundException;
import fr.univrouen.instalite.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers() {
        return new ArrayList<>(userRepository.findAll());
    }

    // Mettre à jour un utilisateur
    public User updateUser(Long userId, User updatedUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Vous devez être connecté pour mettre à jour un utilisateur.");
        }

        User currentUser = (User) authentication.getPrincipal();

        // On vérifie si l'utilisateur connecté est autorisé (propriétaire ou admin)
        if (!currentUser.getId().equals(userId) && !currentUser.getRole().equals("ADMIN")) {
            throw new SecurityException("Vous n'avez pas le droit de réaliser cette action.");
        }

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec l'ID : " + userId));

        // Mise à jour des champs
        if (updatedUser.getFullName() != null) existingUser.setFullName(updatedUser.getFullName());
        if (updatedUser.getPseudo() != null) existingUser.setPseudo(updatedUser.getPseudo());
        if (updatedUser.getEmail() != null) existingUser.setEmail(updatedUser.getEmail());
        if (updatedUser.getPassword() != null) existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        if (updatedUser.getProfileImg() != null) existingUser.setProfileImg(updatedUser.getProfileImg());
        if (currentUser.getRole().equals("ADMIN") && updatedUser.getRole() != null) {
            existingUser.setRole(updatedUser.getRole());
        }

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Vous devez être connecté pour supprimer un utilisateur.");
        }
        User currentUser = (User) authentication.getPrincipal();

        // On verifie si l'utilisateur connecté est autorisé (propriétaire ou admin)
        if (!currentUser.getId().equals(userId) && !currentUser.getRole().equals("ADMIN")) {
            throw new SecurityException("Vous n'avez pas le droit de réaliser cette action.");
        }

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Utilisateur introuvable avec l'ID : " + userId);
        }
        userRepository.deleteById(userId);
    }
}
