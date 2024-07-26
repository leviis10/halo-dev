package enigma.halodev.service.implementation;

import enigma.halodev.dto.SessionDTO;
import enigma.halodev.exception.NotEnoughBalanceException;
import enigma.halodev.exception.SessionNotFoundException;
import enigma.halodev.model.*;
import enigma.halodev.repository.SessionRepository;
import enigma.halodev.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;
    private final UserService userService;
    private final ProgrammerService programmerService;
    private final TopicService topicService;

    @Override
    public Session create(Authentication auth, SessionDTO dto) {
        User user = (User) auth.getPrincipal();
        Programmer foundProgrammer = programmerService.getById(dto.getProgrammerId());
        Topic foundTopic = topicService.getById(dto.getTopicId());

        if(user.getBalance() < foundProgrammer.getPrice()){
            throw new NotEnoughBalanceException();
        }

        Session savedSession = sessionRepository.save(Session.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .user(user)
                .programmer(foundProgrammer)
                .topic(foundTopic)
                .sessionStatus(SessionStatus.NOT_COMPLETED)
                .build()
        );

        // change programmer availability after session created
        programmerService.updateAvailability(foundProgrammer);

        // reduce user (client) balance after session
        user.setBalance(user.getBalance() - foundProgrammer.getPrice());
        userService.updateBalanceUserAfterTransaction(user);

        // add payment to programmer balance
        User foundUserProgrammer = foundProgrammer.getUser();
        foundUserProgrammer.setBalance(foundUserProgrammer.getBalance() + foundProgrammer.getPrice());
        userService.updateBalanceProgrammerAfterTransaction(foundUserProgrammer);

        return savedSession;
    }

    @Override
    public Page<Session> getAll(Pageable pageable) {
        return sessionRepository.findAll(pageable);
    }

    @Override
    public Session updateById(Long id) {
        Session foundSession = getById(id);
        foundSession.setSessionStatus(SessionStatus.COMPLETED);

        // change availability of programmer after session done
        Programmer foundProgrammer = foundSession.getProgrammer();
        programmerService.updateAvailability(foundProgrammer);

        return sessionRepository.save(foundSession);
    }

    @Override
    public Session getById(Long id) {
        return sessionRepository.findById(id).
                orElseThrow(SessionNotFoundException::new);
    }

    @Override
    public void deleteById(Long id) {
        sessionRepository.deleteById(id);
    }
}
