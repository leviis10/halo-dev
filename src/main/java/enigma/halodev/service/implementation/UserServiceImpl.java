package enigma.halodev.service.implementation;

import enigma.halodev.dto.UserDTO;
import enigma.halodev.dto.UserDTO.topUpDto;
import enigma.halodev.dto.midtrans.*;
import enigma.halodev.exception.TransactionNotFoundException;
import enigma.halodev.exception.UserNotFoundException;
import enigma.halodev.model.PaymentStatus;
import enigma.halodev.model.Transaction;
import enigma.halodev.model.User;
import enigma.halodev.repository.TransactionRepository;
import enigma.halodev.repository.UserRepository;
import enigma.halodev.service.CloudinaryService;
import enigma.halodev.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final TransactionRepository transactionRepository;
    private final RestClient restClient;
    private final ExecutorService executorService;

    @Value("${midtrans.serverkey}")
    private String midtransServerKey;

    @Override
    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User updateById(Long id, UserDTO dto) {
        User foundUser = getById(id);
        foundUser.setFirstName(dto.getFirstName());
        foundUser.setLastName(dto.getLastName());
        foundUser.setUsername(dto.getUsername());
        foundUser.setEmail(dto.getEmail());

        return userRepository.save(foundUser);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User uploadProfilePicture(Authentication auth, MultipartFile image) throws IOException {
        User currentUser = (User) auth.getPrincipal();
        currentUser.setProfilePicture(cloudinaryService.upload(currentUser, image));
        return userRepository.save(currentUser);
    }

    @Override
    public topUpDto topUp(Authentication auth, Double amount) {
        User user = (User) auth.getPrincipal();

        Transaction transaction = Transaction.builder()
                .paymentNominal(amount)
                .status(PaymentStatus.UNPAID)
                .user(user)
                .build();

        //midtrans
        TransactionDetails transactionDetails = TransactionDetails.builder()
                .order_id(UUID.randomUUID().toString())
                .gross_amount(amount)
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
                user,
                amount
        ));

        return topUpDto.builder()
                .payment_url(transaction.getRedirectUrl())
                .amount(amount)
                .build();
    }

    private void updateTransactionStatus(Long id, String orderId, User user, Double amount) {
        for (int i = 0; i < 20; i++) {
            try {
                GetTransactionDetailResponse response = restClient
                        .get()
                        .uri(String.format("https://api.sandbox.midtrans.com/v2/%s/status", orderId))
                        .header("Authorization", "Basic " + midtransServerKey)
                        .retrieve()
                        .body(GetTransactionDetailResponse.class);
                if (response != null && "capture".equals(response.getTransaction_status())) {
                    Transaction foundTransaction = transactionRepository.findById(id).orElseThrow(TransactionNotFoundException::new);
                    foundTransaction.setStatus(PaymentStatus.PAID);
                    transactionRepository.save(foundTransaction);
                    user.setBalance(user.getBalance() + amount);
                    userRepository.save(user);
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
