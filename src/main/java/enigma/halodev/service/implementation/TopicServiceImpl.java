package enigma.halodev.service.implementation;

import enigma.halodev.exception.TopicNotFoundException;
import enigma.halodev.model.Topic;
import enigma.halodev.repository.TopicRepository;
import enigma.halodev.service.TopicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;

    @Override
    public Page<Topic> getAll(Pageable pageable) {
        return topicRepository.findAll(pageable);
    }

    @Override
    public Topic getById(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(TopicNotFoundException::new);
    }
}
