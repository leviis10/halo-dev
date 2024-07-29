package enigma.halodev.service;

import enigma.halodev.dto.TransactionDTO;
import enigma.halodev.model.Transaction;
import enigma.halodev.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    Transaction create(User user, TransactionDTO transactionDTO);

    Page<Transaction> getAllByUserId(Pageable pageable, User user);

    Transaction getById(User user, Long id);
}
