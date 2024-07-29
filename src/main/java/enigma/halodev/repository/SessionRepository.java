package enigma.halodev.repository;

import enigma.halodev.model.Session;
import enigma.halodev.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByUserAndId(User user, Long id);

    Page<Session> findAllByUser(Pageable pageable, User user);
}
