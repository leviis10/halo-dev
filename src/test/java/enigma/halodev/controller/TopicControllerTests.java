package enigma.halodev.controller;

import enigma.halodev.controller.api.TopicController;
import enigma.halodev.model.Topic;
import enigma.halodev.model.User;
import enigma.halodev.service.TopicService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TopicController.class)
public class TopicControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    private TopicService topicService;

    @Value("${jwt.secret}")
    private String secretKey;
    private String jwtToken;
    private final Long topicId = 1L;
    private Topic topic;

    @BeforeEach
    public void setUp() throws Exception {
        topic = Topic.builder()
                .id(topicId)
                .name("Test Item")
                .build();

        jwtToken = Jwts.builder()
                .setSubject("test")
                .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .compact();

        // Mock authentication
        UserDetails userDetails = User.builder()
                .username("testuser")
                .password("{noop}testpassword") // {noop} = no password encoder
                .build();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getAllTopic() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Topic> topics = new PageImpl<>(Collections.singletonList(topic), pageable, 1);

        // when
        when(topicService.getAll(pageable)).thenReturn(topics);

        // then
        RequestBuilder request = get("/api/topics").header("Authorization", "Bearer " + jwtToken);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"message\"=\"OK\",\"status\"=200,\"data\":[{\"id\":1,\"name\":\"Test Item\"}],\"totalPages\":1,\"totalElements\":1,\"page\":0,\"size\":10}"));
    }
}
