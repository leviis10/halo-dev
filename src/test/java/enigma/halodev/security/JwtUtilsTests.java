package enigma.halodev.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilsTests {
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
    }

    @Test
    void JwtUtils_ValidateToken_InvalidToken() {
        // given
        String invalidToken = "invalidToken"; // Use an invalid token value

        // when
        boolean isValid = jwtUtils.validateToken(invalidToken);

        // then
        assertFalse(isValid);
    }
}
