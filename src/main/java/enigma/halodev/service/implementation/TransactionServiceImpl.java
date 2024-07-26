package enigma.halodev.service.implementation;

import enigma.halodev.exception.TransactionNotFoundException;
import enigma.halodev.model.*;
import enigma.halodev.repository.TransactionRepository;
import enigma.halodev.service.ProgrammerService;
import enigma.halodev.service.TransactionService;
import enigma.halodev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final ProgrammerService programmerService;
    private final UserService userService;

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
    public Transaction updateById(Long id) {
        // change status transaction
        Transaction foundTransaction = getById(id);
        foundTransaction.setStatus(PaymentStatus.PAID);

        // change programmer availability after session done
        Session foundSession = foundTransaction.getSession();
        Programmer foundProgrammer = foundSession.getProgrammer();
        programmerService.updateAvailability(foundProgrammer);

        // reduce user (client) balance after session
        User foundUser = foundSession.getUser();
        foundUser.setBalance(foundUser.getBalance() - foundTransaction.getPaymentNominal());

        // add payment to programmer balance
        User foundUserProgrammer = foundProgrammer.getUser();
        foundUserProgrammer.setBalance(foundUserProgrammer.getBalance() + foundTransaction.getPaymentNominal());

        userService.updateBalanceAfterTransaction(foundUserProgrammer, foundUser);

        return transactionRepository.save(foundTransaction);
    }

    @Override
    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }
}
