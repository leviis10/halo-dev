package enigma.halodev.controller;


import enigma.halodev.controller.api.SkillController;
import enigma.halodev.model.Skill;
import enigma.halodev.model.User;
import enigma.halodev.service.SkillService;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.crypto.SecretKey;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;

@WebMvcTest(SkillController.class)
public class SkillControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    private SkillService skillService;

    @Value("${jwt.secret}")
    private String secretKey;
    private Skill skill;
    private String jwtToken;

    @BeforeEach
    public void beforeEach() throws Exception {
        skill = Skill.builder()
                .id(1L)
                .name("skill 1")
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
    public void SkillController_GetAll_ReturnAllSkills() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Skill> skills = new PageImpl<>(List.of(skill), pageable, 1);
        given(skillService.getAll(pageable)).willReturn(skills);

        RequestBuilder request = get("/api/skills").header("Authorization", "Bearer " + jwtToken);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json("{}"))
                .andDo(print());
    }
}
