package enigma.halodev.service.implementation;

import enigma.halodev.exception.TransactionNotFoundException;
import enigma.halodev.model.PaymentStatus;
import enigma.halodev.model.Session;
import enigma.halodev.model.Transaction;
import enigma.halodev.repository.TransactionRepository;
import enigma.halodev.service.SessionService;
import enigma.halodev.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    @Override
    public Transaction create(Transaction request) {
        return transactionRepository.save(request);
    }

    @Override
    public Page<Transaction> getAll(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    @Override
    public Transaction getById(Long id) {
        return transactionRepository.findById(id).
                orElseThrow(TransactionNotFoundException::new);
    }

    @Override
    public Transaction updateById(Long id, Transaction request) {
        Transaction foundTransaction = getById(id);
        foundTransaction.setStatus(PaymentStatus.PAID);

        return transactionRepository.save(foundTransaction);
    }

    @Override
    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }
}
