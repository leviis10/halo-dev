package enigma.halodev.service;

import enigma.halodev.dto.TransactionDTO;
import enigma.halodev.exception.TransactionNotFoundException;
import enigma.halodev.model.PaymentStatus;
import enigma.halodev.model.Transaction;
import enigma.halodev.model.User;
import enigma.halodev.repository.TransactionRepository;
import enigma.halodev.service.implementation.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTests {
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private final Long transactionId = 1L;
    private Transaction transaction;
    private TransactionDTO transactionDTO;
    private User user;

    @BeforeEach
    void beforeEach(){
        // init transaction
        transaction = new Transaction();
        transaction.setId(1L);

        transactionDTO = new TransactionDTO();
        transactionDTO.setPaymentNominal(5000.0);
        transactionDTO.setRedirectUrl("transaction.test.com");

        user = new User();
        user.setId(1L);
    }

    @Test
    void TransactionService_CreateTransaction_ReturnTransactionSuccess(){
        // given
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // when
        Transaction result = transactionService.create(user, transactionDTO);
        result.setCreatedAt(LocalDateTime.now());
        result.setStatus(PaymentStatus.UNPAID);

        // then
        assertEquals(result, transaction);
        verify(transactionRepository, times(1))
                .save(any(Transaction.class));
    }

    @Test
    void TransactionService_GetAllTransactionByUserId_ReturnSpecificTransaction(){
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Transaction> transactions = new PageImpl<>(Collections.singletonList(transaction), pageable, 1);
        when(transactionRepository.findAllByUser(pageable, user)).thenReturn(transactions);

        // when
        Page<Transaction> result = transactionService.getAllByUserId(pageable, user);

        // then
        assertEquals(transactions, result);
        verify(transactionRepository, times(1)).findAllByUser(pageable, user);
    }

    @Test
    void TransactionService_GetTransactionById_ReturnSpecificTransaction(){
        // given
        when(transactionRepository.findByUserAndId(user, transactionId)).thenReturn(Optional.of(transaction));

        // When
        Transaction result = transactionService.getById(user, transactionId);

        // Then
        assertEquals(transaction, result);
        verify(transactionRepository, times(1)).findByUserAndId(user, transactionId);
    }

    @Test
    void TransactionService_UpdatePaymentStatus_ReturnStatusPaid(){
        // given
        when(transactionRepository.findByUserAndId(user, transactionId)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // when
        transactionService.updateStatus(user, transactionId, PaymentStatus.PAID);

        // then
        verify(transactionRepository).findByUserAndId(user, transactionId);
        verify(transactionRepository).save(transaction);
        assertEquals(PaymentStatus.PAID, transaction.getStatus());
    }

    @Test
    void TransactionService_GetTransactionById_ThrowsExceptionWhenTransactionNotFound() {
        // given
        Long nonExistentTransactionId = 99L;
        transaction.setId(nonExistentTransactionId);

        // When
        when(transactionRepository.findByUserAndId(user, nonExistentTransactionId)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> transactionService.getById(user, nonExistentTransactionId))
                .isInstanceOf(TransactionNotFoundException.class)
                .hasMessageContaining("Transaction not found");
        verify(transactionRepository, times(1)).findByUserAndId(user, nonExistentTransactionId);
    }

    @Test
    void TransactionService_UpdateTransactionStatus_ShouldThrowExceptionWhenTransactionNotFound() {
        // given
        Long nonExistentTransactionId = 99L;
        PaymentStatus newStatus = PaymentStatus.PAID;

        // When
        when(transactionRepository.findByUserAndId(user, nonExistentTransactionId)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> transactionService.updateStatus(user, nonExistentTransactionId, newStatus))
                .isInstanceOf(TransactionNotFoundException.class)
                .hasMessageContaining("Transaction not found");

        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }
}
