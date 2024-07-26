package enigma.halodev.service;

import enigma.halodev.dto.SkillDTO;
import enigma.halodev.model.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface SkillService {
    Skill create(SkillDTO dto);

    Set<Skill> getAllById(Set<Long> skillsId);

    Page<Skill> getAll(Pageable pageable);

    Skill getById(Long id);

    Skill updateById(Long id, SkillDTO dto);

    void deleteById(Long id);
}
