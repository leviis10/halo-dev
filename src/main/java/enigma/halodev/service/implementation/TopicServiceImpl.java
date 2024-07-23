package enigma.halodev.service.implementation;

import enigma.halodev.model.Topic;
import enigma.halodev.repository.TopicRepository;
import enigma.halodev.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;

    @Override
    public Topic createTopic(Topic topic) {
        return topicRepository.save(topic);
    }

    @Override
    public Page<Topic> getAllTopic(Pageable pageable) {
        return topicRepository.findAll(pageable);
    }

    @Override
    public Topic getTopic(Long id) {
        return topicRepository.findById(id).orElse(null);
    }

    @Override
    public Topic updateTopic(Long id, Topic topic) {
        Topic updatedTopic = getTopic(id);

        updatedTopic.setName(topic.getName());

        return topicRepository.save(updatedTopic);
    }

    @Override
    public void deleteTopic(Long id) {
        topicRepository.deleteById(id);
    }
}
