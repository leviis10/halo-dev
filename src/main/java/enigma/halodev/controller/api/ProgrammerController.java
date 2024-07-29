package enigma.halodev.controller.api;

import enigma.halodev.dto.ProgrammerDTO;
import enigma.halodev.dto.response.PageResponse;
import enigma.halodev.dto.response.Response;
import enigma.halodev.dto.response.SuccessResponse;
import enigma.halodev.model.Programmer;
import enigma.halodev.model.User;
import enigma.halodev.service.ProgrammerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/programmers")
@RequiredArgsConstructor
public class ProgrammerController {
    private final ProgrammerService programmerService;

    @PostMapping
    public ResponseEntity<SuccessResponse<Programmer>> create(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ProgrammerDTO dto
    ) {
        return Response.success(programmerService.create(user, dto), "Programmer created", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PageResponse<Programmer>> getAll(
            @PageableDefault Pageable pageable
    ) {
        return Response.page(programmerService.getAll(pageable));
    }

    @GetMapping("/me")
    public ResponseEntity<SuccessResponse<Programmer>> getCurrentProgrammer(
            @AuthenticationPrincipal User user
    ) {
        return Response.success(programmerService.getCurrent(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<Programmer>> getById(
            @PathVariable Long id
    ) {
        return Response.success(programmerService.getById(id));
    }

    @PatchMapping("/available")
    public ResponseEntity<SuccessResponse<Programmer>> updateAvailability (
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ProgrammerDTO.ChangeAvailabilityDTO changeAvailabilityDTO
    ) {
        return Response.success(programmerService.updateAvailability(user, changeAvailabilityDTO));
    }

    @PatchMapping("/price")
    public ResponseEntity<SuccessResponse<Programmer>> updatePrice(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ProgrammerDTO.ChangePriceDTO changePriceDTO
    ) {
        return Response.success(programmerService.updatePrice(user, changePriceDTO));
    }

    @PatchMapping("/skills")
    public ResponseEntity<SuccessResponse<Programmer>> updateSkill(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ProgrammerDTO.ChangeSkillsDTO changeSkillsDTO

    ) {
        return Response.success(programmerService.updateSkills(user, changeSkillsDTO));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteProgrammer(
            @AuthenticationPrincipal User user
    ) {
        programmerService.deleteProgrammer(user);
        return Response.success("Programmer deleted");
    }
}
