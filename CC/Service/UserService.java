package com.example.CC.Service;

import com.example.CC.Entity.User;
import com.example.CC.Repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserService implements UserDetailsService {
    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            logger.warning("Attempted to create duplicate user: " + user.getUsername());
            throw new RuntimeException("Username already exists");
        }

        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        user.setRole("USER");

        logger.info("Saving new user: " + user.getUsername());
        logger.info("Raw password length: " + rawPassword.length());
        logger.info("Encoded password: " + encodedPassword);

        User savedUser = userRepository.save(user);
        logger.info("User registered successfully: " + user.getUsername() + " with ID: " + savedUser.getId());
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        logger.info("Authentication attempt for user: " + usernameOrEmail);

        User user = userRepository.findByEmail(usernameOrEmail)
                .orElse(userRepository.findByUsername(usernameOrEmail));

        if (user == null) {
            throw new UsernameNotFoundException("User not found with email or username: " + usernameOrEmail);
        }

        logger.info("User found in database: " + user.getUsername());
        logger.info("Stored password hash: " + user.getPassword());

        // Use email as the principal name
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole()))
        );

        logger.info("UserDetails created successfully for: " + user.getEmail());
        return userDetails;
    }

    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        logger.info("Password verification: " + (matches ? "MATCH" : "NO MATCH"));
        return matches;
    }

    @PostConstruct
    public void init() {
        logger.info("UserService initialized - checking existing users");
        try {
            List<User> users = userRepository.findAll();
            logger.info("Found " + users.size() + " users in the database");
            users.forEach(user -> {
                logger.info("User in DB: " + user.getUsername() +
                        ", ID: " + user.getId() +
                        ", Password hash: " + user.getPassword());
            });

            // Password encoder test
            String testPassword = "password123";
            String encoded = passwordEncoder.encode(testPassword);
            boolean matches = passwordEncoder.matches(testPassword, encoded);
            logger.info("Password encoder test: " +
                    testPassword + " -> " + encoded +
                    " (Verification: " + matches + ")");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during UserService initialization", e);
        }
    }

    public User getLoggedInUser(Principal principal) {
        String identifier = principal.getName();
        User user = userRepository.findByUsername(identifier);
        if (user == null) {
            user = userRepository.findByEmail(identifier).orElse(null);
        }
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }

    public void deleteUserById(Long userId) {
        userRepository.findById(userId).ifPresent(userRepository::delete);
    }

}
