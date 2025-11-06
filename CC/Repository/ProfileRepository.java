package com.example.CC.Repository;

import com.example.CC.Entity.Profile;
import com.example.CC.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUserEmail(String email);

    Optional<Profile> findByUser(User user);

    List<Profile> findByUserUsernameContainingIgnoreCaseOrUserEmailContainingIgnoreCase(String username, String email);

}
