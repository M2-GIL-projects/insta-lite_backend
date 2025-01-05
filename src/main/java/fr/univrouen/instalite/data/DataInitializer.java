package fr.univrouen.instalite.data;

import fr.univrouen.instalite.entities.User;
import fr.univrouen.instalite.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        // Vérifier si un utilisateur par défaut existe déjà
        if (userRepository.count() == 0) {
            User newUser = new User("Admin User", "$2a$10$aSI8gm7ytHJE6cx6.voA6.AIYmcRpwI.977Gc7wiMdeQgwu/6ssRq", "admin@instalite.fr", "ADMIN");
            userRepository.save(newUser);
        }
    }
}

