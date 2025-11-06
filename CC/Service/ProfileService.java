package com.example.CC.Service;

import com.example.CC.Entity.Profile;
import com.example.CC.Entity.User;
import com.example.CC.Repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Optional<Profile> getProfileByEmail(String email) {
        return profileRepository.findByUserEmail(email);
    }

    public Profile saveOrUpdateProfile(Profile profile, User user) {
        profile.setUser(user);
        return profileRepository.save(profile);
    }

    public Optional<Profile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }

    public Optional<Profile> getProfileByUser(User user) {
        return profileRepository.findByUser(user);
    }
    public Profile getOrCreateProfileWithUser(User user) {
        Optional<Profile> profileOpt = getProfileByUser(user);
        Profile profile = profileOpt.orElse(new Profile());
        profile.setUser(user);
        profile.setUsername(user.getUsername());
        profile.setUsermail(user.getEmail());
        return profile;
    }
    public List<Profile> searchProfilesByNameOrEmail(String keyword) {
        return profileRepository.findByUserUsernameContainingIgnoreCaseOrUserEmailContainingIgnoreCase(keyword, keyword);
    }

    public void deleteProfileById(Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        // Optional: delete user if needed
        // userRepository.delete(profile.getUser());

        profileRepository.delete(profile);
    }

}
