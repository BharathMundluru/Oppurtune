package com.example.CC.Controller;

import com.example.CC.Entity.Profile;
import com.example.CC.Entity.User;
import com.example.CC.Repository.UserRepository;
import com.example.CC.Service.ProfileService;
import com.example.CC.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        User user = userService.getLoggedInUser(principal);
        Profile profile = profileService.getOrCreateProfileWithUser(user);
        model.addAttribute("profile", profile);
        return "profile";
    }

    @GetMapping({"/updateprofile", "/updateProfile"})
    public String showUpdateProfileForm(Model model, Principal principal) {
        User user = userService.getLoggedInUser(principal);
        if (user == null) return "redirect:/login";
        Profile profile = profileService.getOrCreateProfileWithUser(user);
        model.addAttribute("profile", profile);
        return "updateprofile";
    }

    @PostMapping({"/updateprofile", "/updateProfile"})
    public String updateProfile(@Valid @ModelAttribute("profile") Profile profile,
                                BindingResult result,
                                Principal principal,
                                Model model) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (result.hasErrors()) {
            profile.setUsername(user.getUsername());
            profile.setUsermail(user.getEmail());
            model.addAttribute("profile", profile);
            return "updateprofile";
        }
        Optional<Profile> existingProfileOpt = profileService.getProfileByUser(user);
        Profile existingProfile = existingProfileOpt.orElse(new Profile());
        existingProfile.setUser(user);
        existingProfile.setPhone(profile.getPhone());
        existingProfile.setDob(profile.getDob());
        existingProfile.setAddress(profile.getAddress());
        existingProfile.setLinkedin(profile.getLinkedin());
        existingProfile.setExperience(profile.getExperience());
        existingProfile.setQualification(profile.getQualification());
        existingProfile.setSkills(profile.getSkills());
        existingProfile.setRegisteredJob(profile.getRegisteredJob());
        existingProfile.setSummary(profile.getSummary());
        profileService.saveOrUpdateProfile(existingProfile, user);
        return "redirect:/profile";
    }

}