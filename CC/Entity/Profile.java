package com.example.CC.Entity;

import jakarta.persistence.*;
import jakarta.persistence.Transient;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@AllArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String phone;
    private LocalDate dob;
    private String address;
    private String linkedin;
    private Integer experience;
    private String qualification;
    private String skills;
    private String registeredJob;

    @Column(length = 5000)
    private String summary;
    @Transient
    private String username;

    @Transient
    private String usermail;

    public Profile() {
    }

    public Profile(User user, LocalDate dob, String address, String linkedin,
                   Integer experience, String qualification, String skills,
                   String registeredJob, String summary) {
        this.user = user;
        this.dob = dob;
        this.address = address;
        this.linkedin = linkedin;
        this.experience = experience;
        this.qualification = qualification;
        this.skills = skills;
        this.registeredJob = registeredJob;
        this.summary = summary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getRegisteredJob() {
        return registeredJob;
    }

    public void setRegisteredJob(String registeredJob) {
        this.registeredJob = registeredJob;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsermail() {
        return usermail;
    }

    public void setUsermail(String usermail) {
        this.usermail = usermail;
    }

}
