package enigma.halodev.service;

import enigma.halodev.model.Skill;
import enigma.halodev.repository.SkillRepository;
import enigma.halodev.service.implementation.SkillServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class SkillServiceTests {
    private Skill skill1;
    private Skill skill2;

    @Mock
    private SkillRepository repository;

    @InjectMocks
    private SkillServiceImpl skillService;

    @BeforeEach
    public void beforeEach(){
        skill1 = Skill.builder()
                .id(1L)
                .name("skill 1")
                .build();
        skill2 = Skill.builder()
                .id(2L)
                .name("skill 2")
                .build();
        repository.save(skill1);
        repository.save(skill2);
    }

    @Test
    public void SkillService_GetAllSkills_ReturnAllSkills() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Skill> skills = new PageImpl<>(List.of(skill1, skill2), pageable, 2);
        given(repository.findAll(pageable)).willReturn(skills);

        Page<Skill> foundSkills = skillService.getAll(pageable);
        assertThat(foundSkills).hasSize(2);
    }

    @Test
    public void SkillService_GetAllById_ReturnSpecificSkill() {
        Set<Long> skills = Set.of(skill1.getId(), skill2.getId());
        given(repository.findByIdIn(skills)).willReturn(Set.of(skill1, skill2));

        Set<Skill> foundSkill = skillService.getAllById(skills);
        assertThat(foundSkill).hasSize(2);
    }
}
