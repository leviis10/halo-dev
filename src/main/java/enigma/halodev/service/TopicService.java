package enigma.halodev.service;

import enigma.halodev.dto.TopicDTO;
import enigma.halodev.model.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface TopicService {
    Topic create(TopicDTO topic);

    Page<Topic> getAll(Pageable pageable);

    Topic getById(Long id);

    Topic updateById(Long id, TopicDTO topic);

    void deleteById(Long id);
}
