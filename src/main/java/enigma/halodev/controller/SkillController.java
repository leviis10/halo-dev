package enigma.halodev.controller;

import enigma.halodev.dto.SkillDTO;
import enigma.halodev.model.Skill;
import enigma.halodev.service.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {
    private final SkillService skillService;

    @PostMapping
    public Skill create(
            @Valid @RequestBody SkillDTO dto
    ) {
        return skillService.create(dto);
    }

    @GetMapping
    public Page<Skill> getAll(
            @PageableDefault Pageable pageable
    ) {
        return skillService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public Skill getOne(
            @PathVariable Long id
    ) {
        return skillService.getById(id);
    }

    @PutMapping("/{id}")
    public Skill update(
            @PathVariable Long id,
            @Valid @RequestBody SkillDTO dto
    ) {
        return skillService.updateById(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id
    ) {
        skillService.deleteById(id);
    }
}
