package enigma.halodev.service.implementation;

import enigma.halodev.dto.TopicDTO;
import enigma.halodev.exception.TopicNotFoundException;
import enigma.halodev.model.Topic;
import enigma.halodev.repository.TopicRepository;
import enigma.halodev.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;

    @Override
    public Topic create(TopicDTO dto) {
        return topicRepository.save(Topic.builder()
                .name(dto.getName())
                .build()
        );
    }

    @Override
    public Page<Topic> getAll(Pageable pageable) {
        return topicRepository.findAll(pageable);
    }

    @Override
    public Topic getById(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(TopicNotFoundException::new);
    }

    @Override
    public Topic updateById(Long id, TopicDTO dto) {
        Topic foundTopic = getById(id);
        foundTopic.setName(dto.getName());

        return topicRepository.save(foundTopic);
    }

    @Override
    public void deleteById(Long id) {
        topicRepository.deleteById(id);
    }
}
