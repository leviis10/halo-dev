package enigma.halodev.controller.api;

import enigma.halodev.dto.SessionDTO;
import enigma.halodev.dto.response.Response;
import enigma.halodev.dto.response.SuccessResponse;
import enigma.halodev.model.Session;
import enigma.halodev.model.User;
import enigma.halodev.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {
    private final SessionService sessionService;

    @PostMapping
    public ResponseEntity<SuccessResponse<Session>> create(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody SessionDTO dto
    ) {
        return Response.success(sessionService.create(user, dto), "Session created", HttpStatus.CREATED);
    }

    @PatchMapping("/{sessionId}/complete")
    public ResponseEntity<SuccessResponse<Session>> completeSession(
            @AuthenticationPrincipal User user,
            @PathVariable Long sessionId
    ) {
        return Response.success(sessionService.completeSession(user, sessionId));
    }
}
