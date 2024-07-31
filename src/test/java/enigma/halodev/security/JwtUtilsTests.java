package enigma.halodev.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtUtilsTests {
    @Mock
    private JwtUtils jwtUtils;

    @Test
    void JwtUtils_GenerateToken_Success() {
        // given
        String username = "testUser";
        String token = "mockedToken";

        when(jwtUtils.generateToken(username)).thenReturn(token);

        // when
        String generatedToken = jwtUtils.generateToken(username);

        // then
        assertNotNull(generatedToken);
        assertEquals(token, generatedToken);
    }

    @Test
    void JwtUtils_ValidateToken_Success() {
        // given
        String token = "mockedToken";

        // when
        when(jwtUtils.validateToken(token)).thenReturn(true);

        // then
        assertTrue(jwtUtils.validateToken(token));
    }

    @Test
    void JwtUtils_ValidateToken_InvalidToken() {
        // given
        String invalidToken = "invalid.token.value";

        // when
        when(jwtUtils.validateToken(invalidToken)).thenReturn(false);

        // then
        assertFalse(jwtUtils.validateToken(invalidToken));
    }

    @Test
    void JwtUtils_GetUsernameFromToken_Success() {
        // given
        String token = "mockedToken";
        String username = "testUser";

        when(jwtUtils.getUsernameFromToken(token)).thenReturn(username);

        // when
        String extractedUsername = jwtUtils.getUsernameFromToken(token);

        // then
        assertEquals(username, extractedUsername);
    }
}
