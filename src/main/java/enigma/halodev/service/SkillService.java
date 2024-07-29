package enigma.halodev.service;

import enigma.halodev.model.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface SkillService {
    Set<Skill> getAllById(Set<Long> skillsId);

    Page<Skill> getAll(Pageable pageable);
}
