package enigma.halodev.service.implementation;

import enigma.halodev.model.Skill;
import enigma.halodev.repository.SkillRepository;
import enigma.halodev.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {
    private final SkillRepository skillRepository;

    @Override
    public Set<Skill> getAllById(Set<Long> skillsId) {
        return skillRepository.findByIdIn(skillsId);
    }

    @Override
    public Page<Skill> getAll(Pageable pageable) {
        return skillRepository.findAll(pageable);
    }
}
