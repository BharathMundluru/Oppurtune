package com.example.CC.Service;

import com.example.CC.Entity.Jobs;
import com.example.CC.Repository.JobsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    @Autowired
    private JobsRepository jobsRepository;

    public List<Jobs> getAllJobs() {
        return jobsRepository.findAll();
    }

    public Optional<Jobs> getJobById(Long id) {
        return jobsRepository.findById(id);
    }

    public Jobs createJob(Jobs job) {
        return jobsRepository.save(job);
    }

    public Jobs updateJob(Long id, Jobs updatedJob) {
        return jobsRepository.findById(id).map(job -> {
            job.setTitle(updatedJob.getTitle());
            job.setCompany(updatedJob.getCompany());
            job.setSalary(updatedJob.getSalary());
            job.setExperience(updatedJob.getExperience());
            job.setSkills(updatedJob.getSkills());
            job.setAbout(updatedJob.getAbout());
            job.setLocation(updatedJob.getLocation());
            job.setQualification(updatedJob.getQualification());
            job.setLink(updatedJob.getLink());
            job.setUser(updatedJob.getUser());
            return jobsRepository.save(job);
        }).orElseThrow(() -> new RuntimeException("Job not found with id " + id));
    }

    public void deleteJob(Long id) {
        jobsRepository.deleteById(id);
    }
}
