package enigma.halodev.service.implementation;

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
    public Skill create(Skill request) {
        return skillRepository.save(request);
    }

    @Override
    public Page<Skill> getAll(Pageable pageable) {
        return skillRepository.findAll(pageable);
    }

    @Override
    public Skill getOne(Long id) {
        return skillRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Skill with id " + id + " not found!"));
    }

    @Override
    public Skill update(Long id, Skill request) {
        if(skillRepository.findById(id).isEmpty()){
            throw new RuntimeException("Skill with id " + id + " not found!");
        } {
            Skill skill = this.getOne(id);
            skill.setName(request.getName());
            return skillRepository.save(skill);
        }
    }

    @Override
    public void delete(Long id) {
        if(skillRepository.findById(id).isEmpty()){
            throw new RuntimeException("Skill with id " + id + " not found!");
        } else{
            skillRepository.deleteById(id);
        }
    }
}
