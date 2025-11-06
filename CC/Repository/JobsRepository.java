package com.example.CC.Repository;

import com.example.CC.Entity.Jobs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobsRepository extends JpaRepository<Jobs,Long> {
}
