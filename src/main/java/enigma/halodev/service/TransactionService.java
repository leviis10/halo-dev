package enigma.halodev.service;

import enigma.halodev.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    Transaction create(Transaction request);

    Page<Transaction> getAll(Pageable pageable);

    Transaction getById(Long id);

    Transaction updateById(Long id);

    void deleteById(Long id);
}
