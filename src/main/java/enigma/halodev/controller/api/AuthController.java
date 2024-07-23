package enigma.halodev.controller.api;

import enigma.halodev.dto.LoginDTO;
import enigma.halodev.dto.RegisterDTO;
import enigma.halodev.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterDTO dto
    ) {
        Map<String, String> response = new HashMap<>();
        response.put("authToken", authService.register(dto));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginDTO dto
    ) {
        Map<String, String> response = new HashMap<>();
        response.put("authToken", authService.login(dto));
        return ResponseEntity.ok(response);
    }

}
