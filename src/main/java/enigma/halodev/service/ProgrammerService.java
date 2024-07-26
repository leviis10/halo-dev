package enigma.halodev.service;

import enigma.halodev.dto.ProgrammerDTO;
import enigma.halodev.model.Programmer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface ProgrammerService {
    Programmer create(Authentication auth, ProgrammerDTO dto);

    Page<Programmer> getAll(Pageable pageable);

    Programmer getById(Long id);

    Programmer updateById(Long id, ProgrammerDTO dto);

    void deleteById(Long id);
}
