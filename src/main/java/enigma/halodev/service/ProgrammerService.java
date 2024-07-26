package enigma.halodev.service;

import enigma.halodev.dto.ProgrammerDTO;
import enigma.halodev.dto.ProgrammerSkillsDTO;
import enigma.halodev.model.Programmer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface ProgrammerService {
    Programmer create(Authentication auth, ProgrammerDTO dto);

    Page<Programmer> getAll(Pageable pageable);

    Programmer getById(Long id);

    Programmer updateById(Authentication auth, ProgrammerDTO dto);

    Programmer deleteProgrammerSkill(Authentication auth, ProgrammerSkillsDTO dto);

    Programmer addProgrammerSkill(Authentication auth, ProgrammerSkillsDTO dto);

    Programmer updateAvailability(Programmer request);

    void deleteById(Long id);
}
