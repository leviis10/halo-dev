package enigma.halodev.controller.api;

import enigma.halodev.dto.SessionDTO;
import enigma.halodev.dto.response.PageResponse;
import enigma.halodev.dto.response.Response;
import enigma.halodev.dto.response.SuccessResponse;
import enigma.halodev.model.Session;
import enigma.halodev.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {
    private final SessionService sessionService;

    @PostMapping
    public ResponseEntity<SuccessResponse<Session>> create(
            @Valid @RequestBody SessionDTO dto
    ) {
        return Response.success(sessionService.create(dto), "Session created", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PageResponse<Session>> getAll(
            @PageableDefault Pageable pageable
    ) {
        return Response.page(sessionService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<Session>> getById(
            @PathVariable Long id
    ) {
        return Response.success(sessionService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<String>> deleteById(
            @PathVariable Long id
    ) {
        sessionService.deleteById(id);
        return Response.success("Session deleted");
    }
}
