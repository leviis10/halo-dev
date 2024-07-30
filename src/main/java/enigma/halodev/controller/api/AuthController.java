package enigma.halodev.controller.api;

import enigma.halodev.dto.LoginDTO;
import enigma.halodev.dto.RegisterDTO;
import enigma.halodev.dto.response.Response;
import enigma.halodev.dto.response.SuccessResponse;
import enigma.halodev.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<String>> register(
            @Valid @RequestBody RegisterDTO dto
    ) {
        return Response.success(authService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<String>> login(
            @Valid @RequestBody LoginDTO dto
    ) {
        return Response.success(authService.login(dto));
    }

}
