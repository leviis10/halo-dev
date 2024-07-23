package enigma.halodev.controller;

import enigma.halodev.model.Topic;
import enigma.halodev.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;

    @PostMapping
    public Topic createTopic(Topic topic) {
        return topicService.createTopic(topic);
    }

    @GetMapping
    public Page<Topic> getAllTopic(Pageable pageable) {
        return topicService.getAllTopic(pageable);
    }

    @GetMapping("/{id}")
    public Topic getTopic(Long id) {
        return topicService.getTopic(id);
    }

    @PatchMapping
    public Topic updateTopic(Long id, Topic topic) {
        return topicService.updateTopic(id, topic);
    }

    @DeleteMapping("/{id}")
    public void deleteTopic(Long id) {
        topicService.deleteTopic(id);
    }
}
