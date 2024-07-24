package enigma.halodev.controller.api;

import enigma.halodev.dto.TopicDTO;
import enigma.halodev.dto.response.PageResponse;
import enigma.halodev.dto.response.Response;
import enigma.halodev.dto.response.SuccessResponse;
import enigma.halodev.model.Topic;
import enigma.halodev.service.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;

    @PostMapping
    public ResponseEntity<SuccessResponse<Topic>> create(
            @Valid @RequestBody TopicDTO dto
    ) {
        return Response.success(topicService.create(dto), "Topic Created", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PageResponse<Topic>> getAll(
            @PageableDefault Pageable pageable
    ) {
        return Response.page(
                topicService.getAll(pageable)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<Topic>> getById(
            @PathVariable Long id
    ) {
        return Response.success(topicService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<Topic>> updateById(
            @PathVariable Long id,
            @Valid @RequestBody TopicDTO dto
    ) {
        return Response.success(topicService.updateById(id, dto), "Topic updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<String>> deleteById(
            @PathVariable Long id
    ) {
        topicService.deleteById(id);
        return Response.success("Topic deleted");
    }
}
