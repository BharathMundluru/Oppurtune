package com.example.CC.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "jobs")
@Getter
@Setter
public class Jobs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private Double salary;

    @Column(nullable = false)
    private Long experience;

    @Column(nullable = false)
    private String skills;

    @Column(length = 5000)
    private String about;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String Qualification;

    @Column(nullable = false)
    private String Link;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Transient
    private String username;
    public Jobs(){
    }

    public Jobs(Long id, String title, String company, Double salary, Long experience, String skills, String about, String location, String Qualification, String Link, User user) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.company = company;
        this.salary = salary;
        this.experience = experience;
        this.skills = skills;
        this.about = about;
        this.location = location;
        this.Qualification = Qualification;
        this.Link = Link;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Long getExperience() {
        return experience;
    }

    public void setExperience(Long experience) {
        this.experience = experience;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getQualification() {
        return Qualification;
    }

    public void setQualification(String qualification) {
        this.Qualification = qualification;
    }

    public String getLink(){return Link;}

    public void setLink(String link){this.Link = link;}

}
