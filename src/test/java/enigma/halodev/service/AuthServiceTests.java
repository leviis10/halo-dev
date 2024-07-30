package enigma.halodev.service;

import enigma.halodev.dto.LoginDTO;
import enigma.halodev.dto.RegisterDTO;
import enigma.halodev.model.User;
import enigma.halodev.repository.UserRepository;
import enigma.halodev.security.JwtUtils;
import enigma.halodev.service.implementation.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTests {
    @Mock
    private UserRepository repository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    public void AuthService_Register_ReturnJWTToken() {
        RegisterDTO registerDTO = RegisterDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .username("johndoe")
                .email("john@example.com")
                .password("12345678")
                .build();
        User user = new User();
        String token = "token";
        when(repository.save(any(User.class))).thenReturn(user);
        when(jwtUtils.generateToken(registerDTO.getUsername())).thenReturn(token);

        String jwtToken = authService.register(registerDTO);

        assertThat(jwtToken).isEqualTo(token);
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    public void AuthService_Login_ReturnJWTToken() {
        LoginDTO loginDTO = LoginDTO.builder()
                .username("john")
                .password("12345678")
                .build();
        String token = "token";
        when(jwtUtils.generateToken(any(String.class))).thenReturn(token);

        String jwtToken = authService.login(loginDTO);

        assertThat(jwtToken).isEqualTo(token);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils, times(1)).generateToken(any(String.class));
    }
}
