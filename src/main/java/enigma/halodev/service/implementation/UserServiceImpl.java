package enigma.halodev.service.implementation;

import enigma.halodev.dto.TransactionDTO;
import enigma.halodev.dto.UserDTO;
import enigma.halodev.dto.UserDTO.TopUpDto;
import enigma.halodev.dto.midtrans.*;
import enigma.halodev.exception.PasswordNotMatchException;
import enigma.halodev.exception.TransactionNotFoundException;
import enigma.halodev.exception.UserNotFoundException;
import enigma.halodev.model.PaymentStatus;
import enigma.halodev.model.Transaction;
import enigma.halodev.model.User;
import enigma.halodev.repository.TransactionRepository;
import enigma.halodev.repository.UserRepository;
import enigma.halodev.service.CloudinaryService;
import enigma.halodev.service.TransactionService;
import enigma.halodev.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TransactionService transactionService;
    private final CloudinaryService cloudinaryService;
    private final RestClient restClient;
    private final ExecutorService executorService;
    private final PasswordEncoder passwordEncoder;

    @Value("${midtrans.serverkey}")
    private String midtransServerKey;

    // TODO change TransactionRepository with TransactionService
    private final TransactionRepository transactionRepository;

    @Override
    public User getCurrentAuthenticatedUser(User user) {
        return user;
    }

    @Override
    public Page<Transaction> getAllTransactions(Pageable pageable, User user) {
        return transactionService.getAllByUserId(pageable, user);
    }

    @Override
    public Transaction getTransactionById(User user, Long transactionsId) {
        return transactionService.getById(user, transactionsId);
    }

    @Override
    public User updateUser(User user, UserDTO userDTO) {
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        return userRepository.save(user);
    }

    @Override
    public void changePassword(User user, UserDTO.ChangePasswordDTO changePasswordDTO) {
        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new PasswordNotMatchException();
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public Transaction topUp(User user, Double amount) {
        TransactionDetails transactionDetails = TransactionDetails.builder()
                .order_id(UUID.randomUUID().toString())
                .gross_amount(amount)
                .build();

        CustomerDetails customerDetails = CustomerDetails.builder()
                .first_name(user.getFirstName())
                .last_name(user.getLastName())
                .email(user.getEmail())
                .build();

        PageExpiry pageExpiry = PageExpiry.builder()
                .duration(5)
                .unit("minutes")
                .build();

        TransactionBody transactionBody = TransactionBody.builder()
                .transaction_details(transactionDetails)
                .customer_details(customerDetails)
                .page_expiry(pageExpiry)
                .enabled_payments(List.of("credit_card"))
                .build();

        CreateTransactionResponse response = restClient
                .post()
                .uri("https://app.sandbox.midtrans.com/snap/v1/transactions")
                .header("Authorization", String.format("Basic %s", Base64.getEncoder().encodeToString(midtransServerKey.getBytes())))
                .header("Content-Type", "application/json")
                .body(transactionBody)
                .retrieve()
                .body(CreateTransactionResponse.class);

        if (response == null) {
            throw new RuntimeException("Something went wrong");
        }


        // create transaction with midtrans API and insert redirectUrl
        Transaction savedTransaction = transactionService.create(user, TransactionDTO.builder()
                .paymentNominal(amount)
                .redirectUrl(response.getRedirect_url())
                .build()
        );

        executorService.submit(() -> updateTransactionStatus(
                savedTransaction.getId(),
                transactionDetails.getOrder_id(),
                user,
                amount
        ));

        return savedTransaction;
    }

    @Override
    public User uploadProfilePicture(Authentication auth, MultipartFile image) throws IOException {
        User currentUser = (User) auth.getPrincipal();
        currentUser.setProfilePicture(cloudinaryService.upload(currentUser, image));
        return userRepository.save(currentUser);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    private void updateTransactionStatus(Long transactionId, String orderId, User user, Double amount) {
        for (int i = 0; i < 100; i++) {
            try {
                GetTransactionDetailResponse response = restClient
                        .get()
                        .uri(String.format("https://api.sandbox.midtrans.com/v2/%s/status", orderId))
                        .header(
                                "Authorization",
                                String.format(
                                        "Basic %s",
                                        Base64.getEncoder().encodeToString(midtransServerKey.getBytes()))
                        )
                        .header("Content-Type", "application/json")
                        .retrieve()
                        .body(GetTransactionDetailResponse.class);

                if (response != null && "capture".equals(response.getTransaction_status())) {
//                    Transaction foundTransaction = transactionRepository.findById(transactionId)
//                            .orElseThrow(TransactionNotFoundException::new);
                    Transaction foundTransaction = transactionService.getById(user, transactionId);
                    // TODO create updateStatus in transactionService
                    foundTransaction.setStatus(PaymentStatus.PAID);
                    transactionRepository.save(foundTransaction);

                    // TODO create updateBalance in this service
                    user.setBalance(user.getBalance() + amount);
                    userRepository.save(user);
                    break;
                }
                Thread.sleep(3000);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    // TODO update to more meaning method
    @Override
    public void updateBalanceUserAfterTransaction(User user) {
        userRepository.save(user);
    }

    // TODO update to more meaning method
    @Override
    public void updateBalanceProgrammerAfterTransaction(User programmer) {
        userRepository.save(programmer);
    }
}
