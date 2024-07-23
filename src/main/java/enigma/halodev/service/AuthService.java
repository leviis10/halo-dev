package enigma.halodev.service;

import enigma.halodev.dto.AuthDTO;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> register(AuthDTO.RegisterRequest request);
    AuthDTO.AuthenticationResponse authenticate(AuthDTO.AuthenticationRequest request);
}
