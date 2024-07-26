package enigma.halodev.repository;

import enigma.halodev.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Set;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    Set<Skill> findByIdIn(Set<Long> skillId);
}
