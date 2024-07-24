package enigma.halodev.service.implementation;

import enigma.halodev.dto.SessionDTO;
import enigma.halodev.exception.SessionNotFoundException;
import enigma.halodev.model.*;
import enigma.halodev.repository.SessionRepository;
import enigma.halodev.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Session create(SessionDTO dto) {
        User foundUser = userService.getById(dto.getUser_id());
        Programmer foundProgrammer = programmerService.getById(dto.getProgrammer_id());
        Topic foundTopic = topicService.getById(dto.getTopic_id());

        Transaction transaction = new Transaction();
        transaction.setPayment_nominal(foundProgrammer.getPrice());
        transaction.setStatus(PaymentStatus.UNPAID);

        transactionService.create(transaction);

        return sessionRepository.save(Session.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .user(foundUser)
                .programmer(foundProgrammer)
                .topic(foundTopic)
                .transaction(transaction)
                .build()
        );
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
