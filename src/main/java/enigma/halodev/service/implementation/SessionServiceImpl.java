package enigma.halodev.service.implementation;

import enigma.halodev.dto.SessionDTO;
import enigma.halodev.exception.SessionNotFoundException;
import enigma.halodev.model.*;
import enigma.halodev.repository.SessionRepository;
import enigma.halodev.repository.TransactionRepository;
import enigma.halodev.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;
    private final UserService userService;
    private final ProgrammerService programmerService;
    private final TopicService topicService;
    private final TransactionService transactionService;
    private final RestClient restClient;
    private final ExecutorService executorService;
    @Value("${midtrans.serverkey}")
    private String midtransServerKey;

    // Don't forget to change Lepai
    private final TransactionRepository transactionRepository;

    @Override
    public Session create(Authentication auth, SessionDTO dto) {
        return null;
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
