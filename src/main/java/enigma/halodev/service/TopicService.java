package enigma.halodev.service;

import enigma.halodev.model.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TopicService {

    Topic createTopic(Topic topic);

    Page<Topic> getAllTopic(Pageable pageable);

    Topic getTopic(Long id);

    Topic updateTopic(Long id, Topic topic);

    void deleteTopic(Long id);
}
