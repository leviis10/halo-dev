package enigma.halodev.service.implementation;

import enigma.halodev.dto.SkillDTO;
import enigma.halodev.exceptions.SkillNotFoundException;
import enigma.halodev.model.Skill;
import enigma.halodev.repository.SkillRepository;
import enigma.halodev.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {
    private final SkillRepository skillRepository;

    @Override
    public Skill create(SkillDTO dto) {
        return skillRepository.save(Skill.builder()
                .name(dto.getName())
                .build());
    }

    @Override
    public Page<Skill> getAll(Pageable pageable) {
        return skillRepository.findAll(pageable);
    }

    @Override
    public Skill getById(Long id) {
        return skillRepository.findById(id)
                .orElseThrow(SkillNotFoundException::new);
    }

    @Override
    public Skill updateById(Long id, SkillDTO dto) {
        Skill foundSkill = getById(id);
        foundSkill.setName(dto.getName());
        return skillRepository.save(foundSkill);
    }

    @Override
    public void deleteById(Long id) {
        skillRepository.deleteById(id);
    }
}
