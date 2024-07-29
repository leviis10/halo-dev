package enigma.halodev.controller.api;

import enigma.halodev.dto.response.PageResponse;
import enigma.halodev.dto.response.Response;
import enigma.halodev.model.Skill;
import enigma.halodev.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {
    private final SkillService skillService;

    @GetMapping
    public ResponseEntity<PageResponse<Skill>> getAll(
            @PageableDefault Pageable pageable
    ) {
        return Response.page(skillService.getAll(pageable));
    }
}
