package enigma.halodev.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SecurityConfigTests {
    @InjectMocks
    SecurityConfig securityConfig;

    @Test
    void SecurityConfig_JwtAuthenticationFilter() {
        // when
        JwtAuthenticationFilter filter = securityConfig.jwtAuthenticationFilter();

        // then
        assertNotNull(filter, "JwtAuthenticationFilter should not be null");
    }

    @Test
    void SecurityConfig_AuthenticationManager() throws Exception {
        // given
        AuthenticationConfiguration mockConfig = mock(AuthenticationConfiguration.class);
        AuthenticationManager mockManager = mock(AuthenticationManager.class);

        // when
        when(mockConfig.getAuthenticationManager()).thenReturn(mockManager);
        AuthenticationManager manager = securityConfig.authenticationManager(mockConfig);

        // then
        assertNotNull(manager, "AuthenticationManager should not be null");
    }

    @Test
    void SecurityConfig_PasswordEncoder() {
        // when
        PasswordEncoder encoder = securityConfig.passwordEncoder();

        // then
        assertNotNull(encoder, "PasswordEncoder should not be null");
        assertTrue(encoder instanceof BCryptPasswordEncoder, "PasswordEncoder should be an instance of BCryptPasswordEncoder");
    }

}
