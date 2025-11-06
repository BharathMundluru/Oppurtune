package com.example.CC.Controller;

import com.example.CC.Entity.Jobs;
import com.example.CC.Entity.User;
import com.example.CC.Repository.UserRepository;
import com.example.CC.Service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddJobForm(Model model) {
        model.addAttribute("job", new Jobs());
        return "add-job";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addJob(@ModelAttribute("job") Jobs job, Principal principal) {
        User adminUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        job.setUser(adminUser);
        jobService.createJob(job);
        return "redirect:/jobs/list";
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public String listJobs(Model model) {
        model.addAttribute("jobs", jobService.getAllJobs());
        return "jobs-list";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditJobForm(@PathVariable Long id, Model model) {
        Jobs job = jobService.getJobById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        model.addAttribute("job", job);
        return "edit-job";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateJob(@PathVariable Long id, @ModelAttribute("job") Jobs updatedJob, Principal principal) {
        User adminUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        updatedJob.setUser(adminUser);
        jobService.updateJob(id, updatedJob);
        return "redirect:/jobs/list";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return "redirect:/jobs/list";
    }
}
