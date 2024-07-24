package enigma.halodev.controller.api;

import enigma.halodev.dto.response.PageResponse;
import enigma.halodev.dto.response.Response;
import enigma.halodev.dto.response.SuccessResponse;
import enigma.halodev.model.Transaction;
import enigma.halodev.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<PageResponse<Transaction>> getAll(
            @PageableDefault Pageable pageable
    ) {
        return Response.page(transactionService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<Transaction>> getById(
            @PathVariable Long id
    ) {
        return Response.success(transactionService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<Transaction>> updateById(
            @PathVariable Long id,
            @Valid @RequestBody Transaction request
    ) {
        return Response.success(transactionService.updateById(id, request), "Transaction Updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<String>> deleteById(
            @PathVariable Long id
    ) {
        transactionService.deleteById(id);
        return Response.success("Transaction deleted");
    }
}
