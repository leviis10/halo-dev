package enigma.halodev.service;

import enigma.halodev.dto.SessionDTO;
import enigma.halodev.model.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SessionService {
    Session create(SessionDTO dto);

    Page<Session> getAll(Pageable pageable);

    Session getById(Long id);

    void deleteById(Long id);
}
