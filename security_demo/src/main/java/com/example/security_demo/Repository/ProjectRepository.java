package com.example.security_demo.Repository;

import com.example.security_demo.Entity.Projects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Projects,Long> {
    Optional<Projects> findByProjectName(String projectName);
}
