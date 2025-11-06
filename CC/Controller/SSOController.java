package com.example.CC.Controller;

import com.example.CC.Service.JobService;
import com.example.CC.Service.UserService;
import com.example.CC.Entity.User;
import com.example.CC.JWT.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Map;

@Controller
public class SSOController {
    @Autowired
    private UserService userService;
    @Autowired
    private JobService jobService;

    @GetMapping("/Web")
    public String app(){
        return "Web";
    }
    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal OAuth2User oAuth2User, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && oAuth2User != null) {
            String name = oAuth2User.getAttribute("name");
            String mail = oAuth2User.getAttribute("email");
            model.addAttribute("username", name != null ? name : "Guest");
            model.addAttribute("usermail", mail != null ? mail : "Guest");
        } else {
            model.addAttribute("username", "Guest");
            model.addAttribute("usermail", "Guest mail");
        }
        model.addAttribute("jobs", jobService.getAllJobs());
        return "home";
    }

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "forward:/home";
        }
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestParam String username,
                                              @RequestParam String password,
                                              HttpServletResponse response) {
        UserDetails userDetails = userService.loadUserByUsername(username);
        if (userDetails != null && userService.verifyPassword(password, userDetails.getPassword())) {
            String token = JwtUtil.generateToken(username);
            ResponseCookie jwtCookie = ResponseCookie.from("JWT_TOKEN", token)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(1800)
                    .sameSite("None")
                    .build();
            response.addHeader("Set-Cookie", jwtCookie.toString());
            return ResponseEntity.ok(Map.of("message", "Login successful"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        userService.saveUser(user);
        model.addAttribute("successMessage", "You have signed up successfully!");
        return "redirect:/signup?registered=true";
    }

    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(Map.of(
                "username", userDetails.getUsername(),
                "roles", userDetails.getAuthorities()
        ));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie deleteCookie = ResponseCookie.from("JWT_TOKEN", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", deleteCookie.toString());
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}
