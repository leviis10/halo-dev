package enigma.halodev.repository;

import enigma.halodev.exception.TopicNotFoundException;
import enigma.halodev.model.Topic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TopicRepositoryTests {
    private final TopicRepository repository;
    private Topic topic1;
    private Topic topic2;
    private Topic duplicateTopic1;
    private Topic nullNameTopic;

    @Autowired
    public TopicRepositoryTests(TopicRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    public void beforeEach() {
        topic1 = Topic.builder()
                .name("topic 1")
                .build();
        topic2 = Topic.builder()
                .name("topic 2")
                .build();
        duplicateTopic1 = Topic.builder()
                .name("topic 1")
                .build();
        nullNameTopic = Topic.builder()
                .build();
    }

    @Test
    public void TopicRepository_CreateTopic_ReturnTopic() {
        Topic savedTopic = repository.save(topic1);
        Topic foundTopic = repository.findById(savedTopic.getId())
                .orElseThrow(TopicNotFoundException::new);

        assertThat(foundTopic).isNotNull();
    }

    @Test
    public void TopicRepository_CreateTopicWithoutName_ThrowDataIntegrityViolationException() {
        assertThrows(DataIntegrityViolationException.class, () -> repository.save(nullNameTopic));
    }

    @Test
    public void TopicRepository_CreateTopicWithDuplicateName_ThrowDataIntegrityViolationException() {
        repository.save(topic1);
        assertThrows(DataIntegrityViolationException.class, () -> repository.save(duplicateTopic1));
    }

    @Test
    public void TopicRepository_GetAllTopics_ReturnAllTopics() {
        repository.saveAll(List.of(topic1, topic2));

        List<Topic> foundTopics = repository.findAll();

        assertThat(foundTopics).isNotNull();
        assertThat(foundTopics).hasSize(2);
    }

    @Test
    public void TopicRepository_GetTopicById_ReturnSpecificTopic() {
        Topic savedTopic = repository.save(topic1);

        Topic foundTopic = repository.findById(savedTopic.getId())
                .orElseThrow(TopicNotFoundException::new);

        assertThat(foundTopic).isNotNull();
    }

    @Test
    public void TopicRepository_GetTopicByNonExistentId_ThrowTopicNotFoundException() {
        repository.save(topic1);
        Long nonExistentId = 999L;

        assertThrows(TopicNotFoundException.class, () -> repository.findById(nonExistentId)
                .orElseThrow(TopicNotFoundException::new)
        );
    }

    @Test
    public void TopicRepository_DeleteByIdAndGetById_ThrowTopicNotFoundException() {
        Topic savedTopic = repository.save(topic1);

        repository.deleteById(savedTopic.getId());

        assertThrows(TopicNotFoundException.class, () -> repository.findById(savedTopic.getId())
                .orElseThrow(TopicNotFoundException::new));
    }

    @Test
    public void TopicRepository_DeleteByNonExistentIdAndGetById_ThrowTopicNotFoundException() {
        repository.save(topic1);
        Long nonExistentId = 999L;

        repository.deleteById(nonExistentId);

        assertThrows(TopicNotFoundException.class, () -> repository.findById(nonExistentId)
                .orElseThrow(TopicNotFoundException::new));
    }
}
