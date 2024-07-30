package enigma.halodev.repository;

import enigma.halodev.exception.SkillNotFoundException;
import enigma.halodev.model.Skill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SkillRepositoryTests {
    private final SkillRepository repository;
    private Skill skill1;
    private Skill skill2;
    private Skill duplicateSkill1;
    private Skill nullNameSkill;

    @Autowired
    public SkillRepositoryTests(SkillRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    public void beforeEach() {
        skill1 = Skill.builder()
                .name("skill 1")
                .build();
        skill2 = Skill.builder()
                .name("skill 2")
                .build();
        duplicateSkill1 = Skill.builder()
                .name("skill 1")
                .build();
        nullNameSkill = Skill.builder()
                .build();
    }

    @Test
    public void SkillRepository_CreateSkill_ReturnSkill() {
        Skill savedSkill = repository.save(skill1);
        Skill foundSkill = repository.findById(savedSkill.getId())
                .orElseThrow(SkillNotFoundException::new);

        assertThat(foundSkill).isNotNull();
    }

    @Test
    public void SkillRepository_CreateSkillWithoutName_ThrowDataIntegrityViolationException() {
        assertThrows(DataIntegrityViolationException.class, () -> repository.save(nullNameSkill));
    }

    @Test
    public void SkillRepository_CreateDuplicateSkill_ThrowDataIntegrityViolationException() {
        repository.save(skill1);
        assertThrows(DataIntegrityViolationException.class, () -> repository.save(duplicateSkill1));
    }

    @Test
    public void SkillRepository_GetSkillByName_ReturnSkill() {
        repository.save(skill1);
        Skill foundSkill = repository.findByName(skill1.getName())
                .orElseThrow(SkillNotFoundException::new);
        assertThat(foundSkill).isNotNull();
    }

    @Test
    public void SkillRepository_GetAllSkills_ReturnAllSkills() {
        repository.save(skill1);
        repository.save(skill2);
        List<Skill> foundSkills = repository.findAll();
        assertThat(foundSkills).isNotNull();
    }

    @Test
    public void SkillRepository_GetSkillById_ReturnSpecificSkill() {
        repository.save(skill1);
        Skill foundSkill = repository.findById(skill1.getId())
                .orElseThrow(SkillNotFoundException::new);
        assertThat(foundSkill).isNotNull();
    }

    @Test
    public void SkillRepository_GetSkillByNonExistentId_ThrowSkillNotFoundException() {
        assertThrows(SkillNotFoundException.class, () -> repository.findById(0L).orElseThrow(SkillNotFoundException::new));
    }
}
