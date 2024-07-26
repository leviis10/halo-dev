package enigma.halodev.service;

import enigma.halodev.dto.SessionDTO;
import enigma.halodev.model.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface SessionService {
    Session create(Authentication auth, SessionDTO dto);

    Page<Session> getAll(Pageable pageable);

    Session getById(Long id);

    void deleteById(Long id);
}
