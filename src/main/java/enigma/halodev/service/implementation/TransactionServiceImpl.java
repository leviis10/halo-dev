package enigma.halodev.service.implementation;

import enigma.halodev.dto.TransactionDTO;
import enigma.halodev.exception.TransactionNotFoundException;
import enigma.halodev.model.*;
import enigma.halodev.repository.TransactionRepository;
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
    public Transaction updateById(Long id, TransactionDTO updatedTransaction) {
        Transaction newTransaction = getById(id);
        newTransaction.setPaymentNominal(updatedTransaction.getPaymentNominal());
        newTransaction.setRedirectUrl(updatedTransaction.getRedirectUrl());

        return transactionRepository.save(newTransaction);
    }

    @Override
    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public Transaction toggleTransactionStatus(Long id) {
        Transaction foundTransaction = getById(id);

        if(foundTransaction.getStatus().equals(PaymentStatus.PAID)) {
            foundTransaction.setStatus(PaymentStatus.UNPAID);
        } else {
            foundTransaction.setStatus(PaymentStatus.PAID);
        }

        return transactionRepository.save(foundTransaction);
    }
}
