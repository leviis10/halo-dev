package enigma.halodev.controller;

import enigma.halodev.dto.SkillDTO;
import enigma.halodev.dto.response.PageResponse;
import enigma.halodev.dto.response.Response;
import enigma.halodev.dto.response.SuccessResponse;
import enigma.halodev.model.Skill;
import enigma.halodev.service.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {
    private final SkillService skillService;

    @PostMapping
    public ResponseEntity<SuccessResponse<Skill>> create(
            @Valid @RequestBody SkillDTO dto
    ) {
        return Response.success(skillService.create(dto), "Skill Created", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PageResponse<Skill>> getAll(
            @PageableDefault Pageable pageable
    ) {
        return Response.page(skillService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<Skill>> getById(
            @PathVariable Long id
    ) {
        return Response.success(skillService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<Skill>> updateById(
            @PathVariable Long id,
            @Valid @RequestBody SkillDTO dto
    ) {
        return Response.success(skillService.updateById(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<String>> deleteById(
            @PathVariable Long id
    ) {
        skillService.deleteById(id);
        return Response.success("Skill deleted");
    }
}
