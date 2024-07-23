package enigma.halodev.controller;

import enigma.halodev.model.Skill;
import enigma.halodev.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {
    private final SkillService skillService;

    @PostMapping
    public Skill create(@RequestBody Skill request){
        return skillService.create(request);
    }

    @GetMapping
    public Page<Skill> getAll(Pageable pageable){
        return skillService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public Skill getOne(@PathVariable Long id){
        return skillService.getOne(id);
    }

    @PutMapping("/{id}")
    public Skill update(@PathVariable Long id, @RequestBody Skill request){
        return skillService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        skillService.delete(id);
    }
}
