package enigma.halodev.service;

import enigma.halodev.exception.TopicNotFoundException;
import enigma.halodev.model.Topic;
import enigma.halodev.repository.TopicRepository;
import enigma.halodev.service.implementation.TopicServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TopicServiceTests {
    @Mock
    private TopicRepository topicRepository;

    @InjectMocks
    private TopicServiceImpl topicService;

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
    void getAllTopic(){
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Topic> topics = new PageImpl<>(Collections.singletonList(topic), pageable, 1);
        when(topicRepository.findAll(pageable)).thenReturn(topics);

        // when
        Page<Topic> result = topicService.getAll(pageable);

        // then
        assertEquals(topics, result);
        verify(topicRepository, times(1)).findAll(pageable);
    }

    @Test
    void getByIdTopic(){
         // given
         when(topicRepository.findById(topicId)).thenReturn(Optional.of(topic));

         // When
         Topic result = topicService.getById(topicId);

         // Then
         assertEquals(topic, result);
         verify(topicRepository, times(1)).findById(topicId);
    }

    @Test
    void getById_fail(){
        // given
        Long nonExistentTopicId = 99L;
        topic.setId(nonExistentTopicId);

        // when
        when(topicRepository.findById(nonExistentTopicId)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> topicService.getById(nonExistentTopicId))
                .isInstanceOf(TopicNotFoundException.class)
                .hasMessageContaining("Topic not found");
        verify(topicRepository, times(1)).findById(nonExistentTopicId);
    }
}
