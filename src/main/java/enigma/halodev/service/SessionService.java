package enigma.halodev.service;

import enigma.halodev.dto.SessionDTO;
import enigma.halodev.model.Session;
import enigma.halodev.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SessionService {
    Session create(User user, SessionDTO dto);

    Page<Session> getAll(Pageable pageable, User user);

    Session getById(User user, Long sessionId);

    Session completeSession(User user, Long sessionId);
}
