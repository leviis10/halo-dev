package enigma.halodev.controller;

import enigma.halodev.controller.api.SessionController;
import enigma.halodev.dto.SessionDTO;
import enigma.halodev.model.Session;
import enigma.halodev.model.User;
import enigma.halodev.service.SessionService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessionController.class)
public class SessionControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private SessionService sessionService;

    @Value("${jwt.secret}")
    private String secretKey;

    private String jwtToken;
    private User user;
    private Session session;
    private SessionDTO sessionDTO;

    @BeforeEach
    public void beforeEach() throws Exception {
        user = User.builder()
                .username("testuser")
                .password("{noop}testpassword")
                .build();

        session = Session.builder()
                .id(1L)
                .build();

        sessionDTO = new SessionDTO();
        sessionDTO.setName("testsession");
        sessionDTO.setDescription("testsession");
        sessionDTO.setTopicId(1L);
        sessionDTO.setProgrammerId(1L);

        jwtToken = Jwts.builder()
                .subject("test")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .compact();

        UserDetails userDetails = User.builder()
                .username("testuser")
                .password("{noop}testpassword")
                .build();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void SessionController_CreateSession_ReturnsCreated() throws Exception {
        when(sessionService.create(any(User.class), any(SessionDTO.class))).thenReturn(session);

        String jsonContent = "{"
                + "\"name\":\"testsession\","
                + "\"description\":\"testsession\","
                + "\"programmerId\":1,"
                + "\"topicId\":1"
                + "}";

        RequestBuilder request = post("/api/sessions")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Session created"))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void SessionController_CompleteSession_ReturnsSuccess() throws Exception {
        when(sessionService.completeSession(any(User.class), anyLong())).thenReturn(session);

        RequestBuilder request = patch("/api/sessions/1/complete")
                .header("Authorization", "Bearer " + jwtToken);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.id").value(1));
    }
}
