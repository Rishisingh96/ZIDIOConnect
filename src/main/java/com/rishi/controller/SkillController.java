package com.rishi.controller;

import com.rishi.dto.SkillDTO;
import com.rishi.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    // 🔹 Create Skill
    @PostMapping
    public ResponseEntity<SkillDTO> createSkill(@RequestBody SkillDTO skillDTO) {
        return ResponseEntity.ok(skillService.createSkill(skillDTO));
    }

    // 🔹 Get All Skills
    @GetMapping
    public ResponseEntity<List<SkillDTO>> getAllSkills() {
        return ResponseEntity.ok(skillService.getAllSkills());
    }

    // 🔹 Get Skill By ID
    @GetMapping("/{id}")
    public ResponseEntity<SkillDTO> getSkillById(@PathVariable Long id) {
        return ResponseEntity.ok(skillService.getSkillById(id));
    }

    // 🔹 Delete Skill By ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSkill(@PathVariable Long id) {
        skillService.deleteSkill(id);
        return ResponseEntity.ok("Skill deleted successfully with id: " + id);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<SkillDTO>> createSkillsBulk(@RequestBody List<SkillDTO> skills) {
        return ResponseEntity.ok(skillService.createSkillsBulk(skills));
    }

}

