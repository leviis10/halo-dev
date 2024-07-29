package enigma.halodev.controller.api;

import enigma.halodev.dto.UserDTO;
import enigma.halodev.dto.response.PageResponse;
import enigma.halodev.dto.response.Response;
import enigma.halodev.dto.response.SuccessResponse;
import enigma.halodev.model.Session;
import enigma.halodev.model.Transaction;
import enigma.halodev.model.User;
import enigma.halodev.service.SessionService;
import enigma.halodev.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SessionService sessionService;

    @GetMapping("/me")
    public ResponseEntity<SuccessResponse<User>> getCurrentAuthenticatedUser(
            @AuthenticationPrincipal User user
    ) {
        return Response.success(userService.getCurrentAuthenticatedUser(user));
    }

    @GetMapping("/sessions")
    public ResponseEntity<PageResponse<Session>> getAllSessions(
            @AuthenticationPrincipal User user,
            @PageableDefault Pageable pageable
    ) {
        return Response.page(sessionService.getAll(pageable, user));
    }

    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<SuccessResponse<Session>> getSessionDetail(
            @AuthenticationPrincipal User user,
            @PathVariable Long sessionId
    ) {
        return Response.success(sessionService.getById(user, sessionId));
    }

    @GetMapping("/transactions")
    public ResponseEntity<PageResponse<Transaction>> getAllTransactions(
            @AuthenticationPrincipal User user,
            @PageableDefault Pageable pageable
    ) {
        // TODO use transactionService
        return Response.page(userService.getAllTransactions(pageable, user));
    }

    @GetMapping("/transactions/{transactionsId}")
    public ResponseEntity<SuccessResponse<Transaction>> getTransactionDetail(
            @AuthenticationPrincipal User user,
            @PathVariable Long transactionsId
    ) {
        // TODO user transactionService
        return Response.success(userService.getTransactionById(user, transactionsId));
    }

    @PutMapping
    public ResponseEntity<SuccessResponse<User>> updateUser(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UserDTO userDTO
    ) {
        return Response.success(userService.updateUser(user, userDTO));
    }

    @PatchMapping("/password")
    public ResponseEntity<SuccessResponse<String>> changePassword(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UserDTO.ChangePasswordDTO changePasswordDTO
    ) {
        userService.changePassword(user, changePasswordDTO);
        return Response.success("Password changed successfully");
    }

    @PostMapping("/top-up")
    public ResponseEntity<SuccessResponse<Transaction>> topUp(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UserDTO.TopUpDto request
    ) {
        return Response.success(userService.topUp(user, request.getAmount()));
    }

    @PatchMapping(path = "/picture", consumes = "multipart/form-data")
    public ResponseEntity<SuccessResponse<User>> uploadProfilePicture(
            Authentication auth,
            @RequestPart("image") MultipartFile image
    ) throws IOException {
        return Response.success(userService.uploadProfilePicture(auth, image), "Image uploaded", HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<SuccessResponse<String>> deleteUser(
            @AuthenticationPrincipal User user
    ) {
        userService.delete(user);
        return Response.success("User successfully deleted");
    }
}
