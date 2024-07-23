package enigma.halodev.service;

import enigma.halodev.model.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SkillService {
    Skill create(Skill request);
    Page<Skill> getAll(Pageable pageable);
    Skill getOne(Long id);
    Skill update(Long id, Skill request);
    void delete(Long id);
}
