package enigma.halodev.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtUtilsTests {
    @Mock
    private JwtUtils jwtUtils;

    private String username;
    private String token;

    @BeforeEach
    void beforeEach(){
        username = "testUser";
        token = "mockedToken";
    }

    @Test
    void JwtUtils_ValidateToken_Success() {
        // given
        when(jwtUtils.validateToken(token)).thenReturn(true);

        // when
        boolean isValid = jwtUtils.validateToken(token);

        // then
        assertTrue(isValid);
        verify(jwtUtils).validateToken(token);
    }

    @Test
    void JwtUtils_ValidateToken_InvalidToken() {
        // given
        String invalidToken = "invalid.token.value";
        when(jwtUtils.validateToken(invalidToken)).thenReturn(false);

        // when
        boolean isValid = jwtUtils.validateToken(invalidToken);

        // then
        assertFalse(isValid);
        verify(jwtUtils).validateToken(invalidToken);
    }

    @Test
    void JwtUtils_GetUsernameFromToken_Success() {
        // given
        when(jwtUtils.getUsernameFromToken(token)).thenReturn(username);

        // when
        String extractedUsername = jwtUtils.getUsernameFromToken(token);

        // then
        assertEquals(username, extractedUsername);
        verify(jwtUtils).getUsernameFromToken(token);
    }
}
