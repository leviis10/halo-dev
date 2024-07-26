package enigma.halodev.service.implementation;

import enigma.halodev.dto.LoginDTO;
import enigma.halodev.dto.RegisterDTO;
import enigma.halodev.model.User;
import enigma.halodev.repository.UserRepository;
import enigma.halodev.security.JwtUtils;
import enigma.halodev.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public String register(RegisterDTO dto) {
        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .balance(0)
                .build();
        userRepository.save(user);

        return jwtUtils.generateToken(user.getUsername());
    }

    @Override
    public String login(LoginDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(),
                        dto.getPassword()
                )
        );

        return jwtUtils.generateToken(dto.getUsername());
    }
}
