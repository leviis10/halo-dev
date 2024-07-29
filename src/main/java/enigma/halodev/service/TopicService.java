package enigma.halodev.service;

import enigma.halodev.model.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TopicService {
    Page<Topic> getAll(Pageable pageable);

    Topic getById(Long id);
}
