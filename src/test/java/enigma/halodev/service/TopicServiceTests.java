package enigma.halodev.service;

import enigma.halodev.repository.TopicRepository;
import enigma.halodev.service.implementation.TopicServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TopicServiceTests {
    @Mock
    private TopicRepository repository;

    @InjectMocks
    private TopicServiceImpl topicService;
}
