package enigma.halodev.service.implementation;

import enigma.halodev.dto.TransactionDTO;
import enigma.halodev.dto.UserDTO;
import enigma.halodev.dto.midtrans.*;
import enigma.halodev.exception.PasswordNotMatchException;
import enigma.halodev.model.PaymentStatus;
import enigma.halodev.model.Programmer;
import enigma.halodev.model.Transaction;
import enigma.halodev.model.User;
import enigma.halodev.repository.UserRepository;
import enigma.halodev.service.CloudinaryService;
import enigma.halodev.service.TransactionService;
import enigma.halodev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TransactionService transactionService;
    private final CloudinaryService cloudinaryService;
    private final RestClient restClient;
    private final ExecutorService executorService;
    private final PasswordEncoder passwordEncoder;

    @Value("${midtrans.serverkey}")
    private String midtransServerKey;

    @Override
    public User getCurrentAuthenticatedUser(User user) {
        return user;
    }

    @Override
    public User updateUser(User user, UserDTO userDTO) {
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName() != null ? userDTO.getLastName() : user.getLastName());
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

    public void updateTransactionStatus(Long transactionId, String orderId, User user, Double amount) {
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
                    transactionService.updateStatus(user, transactionId, PaymentStatus.PAID);
                    addBalanceAfterTransaction(user, amount);
                    break;
                }
                Thread.sleep(3000);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    public void addBalanceAfterTransaction(User user, Double amount) {
        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);
    }

    @Override
    public void chargeUserAfterTransaction(User user, Double amount) {
        user.setBalance(user.getBalance() - amount);
        userRepository.save(user);
    }

    @Override
    public void addProgrammerBalanceAfterTransaction(Programmer foundProgrammer, Double amount) {
        User foundUserProgrammer = foundProgrammer.getUser();
        foundUserProgrammer.setBalance(foundUserProgrammer.getBalance() + amount);
        userRepository.save(foundUserProgrammer);
    }
}
