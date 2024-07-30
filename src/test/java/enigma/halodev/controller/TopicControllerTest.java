package enigma.halodev.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import enigma.halodev.controller.api.TopicController;
import enigma.halodev.model.Topic;
import enigma.halodev.service.TopicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TopicController.class)
public class TopicControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TopicService topicService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Long topicId = 1L;
    private Topic topic;

    @BeforeEach
    void setUp(){
        // init topic
        topic = new Topic();
        topic.setId(topicId);
        topic.setName("Test Item");
    }

    @Test
    void getAllTopic() throws Exception{
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Topic> topics = new PageImpl<>(Collections.singletonList(topic), pageable, 1);

        // when
        when(topicService.getAll(pageable)).thenReturn(topics);

        // then
        mockMvc.perform(get("/api/topics?page=0&size=10"))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", equalTo("Test Item")));

        verify(topicService, times(1)).getAll(pageable);
    }
}
