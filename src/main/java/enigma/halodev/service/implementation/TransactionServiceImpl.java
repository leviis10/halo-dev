package enigma.halodev.service.implementation;

import enigma.halodev.dto.TransactionDTO;
import enigma.halodev.exception.TransactionNotFoundException;
import enigma.halodev.model.PaymentStatus;
import enigma.halodev.model.Transaction;
import enigma.halodev.model.User;
import enigma.halodev.repository.TransactionRepository;
import enigma.halodev.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    @Override
    public Transaction create(User user, TransactionDTO transactionDTO) {
        Transaction newTransaction = Transaction.builder()
                .user(user)
                .paymentNominal(transactionDTO.getPaymentNominal())
                .status(PaymentStatus.UNPAID)
                .redirectUrl(transactionDTO.getRedirectUrl())
                .build();
        return transactionRepository.save(newTransaction);
    }

    @Override
    public Page<Transaction> getAllByUserId(Pageable pageable, User user) {
        return transactionRepository.findAllByUser(pageable, user);
    }

    @Override
    public Transaction getById(User user, Long id) {
        return transactionRepository.findByUserAndId(user, id).
                orElseThrow(TransactionNotFoundException::new);
    }

    @Override
    public void updateStatus(User user, Long id, PaymentStatus paymentStatus) {
        Transaction foundTransaction = getById(user, id);
        foundTransaction.setStatus(PaymentStatus.PAID);
        transactionRepository.save(foundTransaction);
    }
}
