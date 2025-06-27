package com.rishi.service;

import java.util.List;

import com.rishi.dto.SkillDTO;

public interface SkillService {

    SkillDTO createSkill(SkillDTO skillDTO);

    List<SkillDTO> getAllSkills();

    SkillDTO getSkillById(Long id);

    void deleteSkill(Long id);

    public List<SkillDTO> createSkillsBulk(List<SkillDTO> skills);
}

