package enigma.halodev.repository;

import enigma.halodev.model.Programmer;
import enigma.halodev.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProgrammerRepository extends JpaRepository<Programmer, Long> {
    Programmer findByUserId(Long id);
    Set<Skill> deleteByIdIn(Set<Long> skillId);
}
