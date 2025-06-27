package com.rishi.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.rishi.dto.SkillDTO;
import com.rishi.entity.Skill;
import com.rishi.repository.SkillRepository;
import com.rishi.service.SkillService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;
    private final ModelMapper modelMapper;

    @Override
    public SkillDTO createSkill(SkillDTO skillDTO) {
        // Prevent duplicates
        if (skillRepository.existsByName(skillDTO.getName())) {
            throw new RuntimeException("Skill already exists: " + skillDTO.getName());
        }

        Skill skill = modelMapper.map(skillDTO, Skill.class);
        Skill saved = skillRepository.save(skill);
        return modelMapper.map(saved, SkillDTO.class);
    }

    @Override
    public List<SkillDTO> getAllSkills() {
        return skillRepository.findAll().stream()
                .map(skill -> modelMapper.map(skill, SkillDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public SkillDTO getSkillById(Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found with id: " + id));
        return modelMapper.map(skill, SkillDTO.class);
    }

    @Override
    public void deleteSkill(Long id) {
        if (!skillRepository.existsById(id)) {
            throw new RuntimeException("Skill not found with id: " + id);
        }
        skillRepository.deleteById(id);
    }

    @Override
    public List<SkillDTO> createSkillsBulk(List<SkillDTO> skills) {
        return skills.stream()
                .map(this::createSkill)
                .collect(Collectors.toList());
    }
}
