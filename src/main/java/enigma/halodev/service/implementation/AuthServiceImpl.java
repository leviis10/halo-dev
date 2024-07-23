package enigma.halodev.service.implementation;

import enigma.halodev.dto.AuthDTO;
import enigma.halodev.model.User;
import enigma.halodev.repository.UserRepository;
import enigma.halodev.response.Response;
import enigma.halodev.security.JwtService;
import enigma.halodev.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<?> register(AuthDTO.RegisterRequest request) {
        // Check if user already exists
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        PasswordEncoder passwordEncoderAlt = new BCryptPasswordEncoder(); // originnya

        var user = User.builder()
                .firstName(request.getLastName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .profilePicture(request.getProfilePicture())
                .createdAt(LocalDateTime.now())
                .balance(Integer.valueOf(0))
                .build();
        User savedUser = repository.save(user);
        savedUser.setPassword(null); // biar gak return password
        return Response.renderJSON(
                savedUser,
                "New User Created",
                HttpStatus.CREATED
        );
    }

    @Override
    public AuthDTO.AuthenticationResponse authenticate(AuthDTO.AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        var jwtToken = jwtService.generateToken(user);
        return AuthDTO.AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


}
