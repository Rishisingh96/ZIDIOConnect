package com.rishi.repository;

import com.rishi.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    // ✅ Find skill by name (used while saving profile)
    Optional<Skill> findByName(String name);

    // (Optional) Search feature
    boolean existsByName(String name);
}

