package enigma.halodev.controller.api;

import enigma.halodev.dto.ProgrammerDTO;
import enigma.halodev.dto.ProgrammerSkillsDTO;
import enigma.halodev.dto.response.PageResponse;
import enigma.halodev.dto.response.Response;
import enigma.halodev.dto.response.SuccessResponse;
import enigma.halodev.model.Programmer;
import enigma.halodev.service.ProgrammerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/programmers")
@RequiredArgsConstructor
public class ProgrammerController {
    private final ProgrammerService programmerService;

    @PostMapping
    public ResponseEntity<SuccessResponse<Programmer>> create(
            Authentication auth,
            @Valid @RequestBody ProgrammerDTO dto
    ) {
        return Response.success(programmerService.create(auth, dto), "Programmer created", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PageResponse<Programmer>> getAll(
            @PageableDefault Pageable pageable
    ) {
        return Response.page(programmerService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<Programmer>> getById(
            @PathVariable Long id
    ) {
        return Response.success(programmerService.getById(id));
    }

    @PutMapping
    public ResponseEntity<SuccessResponse<Programmer>> updateById(
            Authentication auth,
            @Valid @RequestBody ProgrammerDTO dto
    ) {
        return Response.success(programmerService.updateById(auth, dto), "Programmer updated");
    }

    @PostMapping("/skills")
    public ResponseEntity<SuccessResponse<Programmer>> addProgrammerSkill(
            Authentication auth,
            @Valid @RequestBody ProgrammerSkillsDTO dto
            ) {
        return Response.success(programmerService.addProgrammerSkill(auth, dto), "Programmer skills updated");
    }

    @DeleteMapping("/skills")
    public ResponseEntity<SuccessResponse<Programmer>> removeProgrammerSkill(
            Authentication auth,
            @Valid @RequestBody ProgrammerSkillsDTO dto
    ) {
        return Response.success(programmerService.deleteProgrammerSkill(auth, dto), "Programmer skills deleted");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<String>> deleteById(
            @PathVariable Long id
    ) {
        programmerService.deleteById(id);
        return Response.success("Programmer deleted");
    }
}
