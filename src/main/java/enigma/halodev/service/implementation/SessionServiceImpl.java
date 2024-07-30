package enigma.halodev.service.implementation;

import enigma.halodev.dto.ProgrammerDTO;
import enigma.halodev.dto.SessionDTO;
import enigma.halodev.exception.NotEnoughBalanceException;
import enigma.halodev.exception.SessionNotFoundException;
import enigma.halodev.model.*;
import enigma.halodev.repository.SessionRepository;
import enigma.halodev.service.ProgrammerService;
import enigma.halodev.service.SessionService;
import enigma.halodev.service.TopicService;
import enigma.halodev.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;
    private final UserService userService;
    private final ProgrammerService programmerService;
    private final TopicService topicService;

    @Override
    public Session create(User user, SessionDTO dto) {
        Programmer foundProgrammer = programmerService.getById(dto.getProgrammerId());
        Topic foundTopic = topicService.getById(dto.getTopicId());

        if (user.getProgrammer() != null && user.getProgrammer().equals(foundProgrammer)) {
            throw new RuntimeException("Cannot create session with yourself");
        }

        if (user.getBalance() < foundProgrammer.getPrice()) {
            throw new NotEnoughBalanceException();
        }

        if (foundProgrammer.getAvailability().equals(Availability.NOT_AVAILABLE)) {
            throw new RuntimeException("programmer is not available");
        }

        Session savedSession = sessionRepository.save(Session.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .user(user)
                .programmer(foundProgrammer)
                .topic(foundTopic)
                .completed(SessionStatus.NOT_COMPLETED)
                .build()
        );

        // change programmer availability after session created
        programmerService.updateAvailability(
                foundProgrammer.getUser(),
                ProgrammerDTO.ChangeAvailabilityDTO.builder()
                        .availability(Availability.NOT_AVAILABLE)
                        .build()
        );

        // reduce user (client) balance after session
        userService.chargeUserAfterTransaction(user, foundProgrammer.getPrice());

        // add payment to programmer balance
        userService.addProgrammerBalanceAfterTransaction(foundProgrammer, foundProgrammer.getPrice());

        return savedSession;
    }

    @Override
    public Page<Session> getAll(Pageable pageable, User user) {
        return sessionRepository.findAllByUser(pageable, user);
    }

    @Override
    public Session getById(User user, Long sessionId) {
        return sessionRepository.findByUserAndId(user, sessionId)
                .orElseThrow(SessionNotFoundException::new);
    }

    @Override
    public Session completeSession(User user, Long sessionId) {
        Session foundSession = getById(user, sessionId);
        if (foundSession.getCompleted().equals(SessionStatus.COMPLETED)) {
            throw new RuntimeException("Already completed");
        }

        programmerService.updateAvailability(
                foundSession.getProgrammer().getUser(),
                ProgrammerDTO.ChangeAvailabilityDTO.builder()
                        .availability(Availability.AVAILABLE)
                        .build()
        );
        foundSession.setCompleted(SessionStatus.COMPLETED);
        return sessionRepository.save(foundSession);
    }
}
