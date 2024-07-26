package enigma.halodev.service.implementation;

import enigma.halodev.dto.midtrans.*;
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

import java.util.List;
import java.util.UUID;
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
        savedSession.setTransaction(transaction);
        programmerService.updateAvailability(foundProgrammer);

        //midtrans
        TransactionDetails transactionDetails = TransactionDetails.builder()
                .order_id(UUID.randomUUID().toString())
                .gross_amount(foundProgrammer.getPrice())
                .build();

        CustomerDetails customerDetails = CustomerDetails.builder()
                .first_name(user.getFirstName())
                .email(user.getEmail())
                .build();

        PageExpiry pageExpiry = PageExpiry.builder()
                .duration(5)
                .unit("minutes")
                .build();

        CreateTransactionResponse response = restClient
                .post()
                .uri("https://app.sandbox.midtrans.com/snap/v1/transactions")
                .header("Authorization", "Basic " + midtransServerKey)
                .body(TransactionBody.builder()
                        .transaction_details(transactionDetails)
                        .customer_details(customerDetails)
                        .page_expiry(pageExpiry)
                        .enabled_payments(List.of("credit_card"))
                        .build()
                )
                .retrieve()
                .body(CreateTransactionResponse.class);

        if (response != null) {
            // create transaction with midtrans API and insert redirectUrl
            transaction.setRedirectUrl(response.getRedirect_url());
            transactionRepository.save(transaction);
        }

        executorService.submit(() -> updateTransactionStatus(
                transaction.getId(),
                transactionDetails.getOrder_id(),
                user.getId(),
                user.getBalance()
        ));

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

    private void updateTransactionStatus(Long id, String orderId, Long userId, Integer amount) {
        for (int i = 0; i < 20; i++) {
            try {
                GetTransactionDetailResponse response = restClient
                        .get()
                        .uri(String.format("https://api.sandbox.midtrans.com/v2/%s/status", orderId))
                        .header("Authorization", "Basic " + midtransServerKey)
                        .retrieve()
                        .body(GetTransactionDetailResponse.class);
                if (response != null && "capture".equals(response.getTransaction_status())) {
                    Transaction foundTransaction = transactionRepository.findById(id).orElseThrow(null);
                    foundTransaction.setStatus(PaymentStatus.PAID);
                    transactionRepository.save(foundTransaction);
                    userService.updateBalance(userId, amount);
                    break;
                }
                Thread.sleep(3000);
            } catch (Exception e) {
                log.error("error in updateTransactionStatus() {}", e.getMessage());
            }
        }
        log.info("Exiting updateTransactionStatus()");
    }
}
