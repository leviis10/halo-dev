package enigma.halodev.service;

import enigma.halodev.dto.SkillDTO;
import enigma.halodev.model.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SkillService {
    Skill create(SkillDTO dto);

    Page<Skill> getAll(Pageable pageable);

    Skill getById(Long id);

    Skill updateById(Long id, SkillDTO dto);

    void deleteById(Long id);
}
