package enigma.halodev.service.implementation;

import enigma.halodev.dto.SessionDTO;
import enigma.halodev.exception.SessionNotFoundException;
import enigma.halodev.model.*;
import enigma.halodev.repository.SessionRepository;
import enigma.halodev.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;
    private final UserService userService;
    private final ProgrammerService programmerService;
    private final TopicService topicService;
    private final TransactionService transactionService;

    @Override
    public Session create(Authentication auth, SessionDTO dto) {
        User user = (User) auth.getPrincipal();
        Programmer foundProgrammer = programmerService.getById(dto.getProgrammerId());
        Topic foundTopic = topicService.getById(dto.getTopicId());

        Session savedSession = sessionRepository.save(Session.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .user(user)
                .programmer(foundProgrammer)
                .topic(foundTopic)
                .build()
        );

        Transaction transaction = Transaction.builder()
                .paymentNominal(foundProgrammer.getPrice())
                .status(PaymentStatus.UNPAID)
                .session(savedSession)
                .build();
        transactionService.create(transaction);

        return savedSession;
    }

    @Override
    public Page<Session> getAll(Pageable pageable) {
        return sessionRepository.findAll(pageable);
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
