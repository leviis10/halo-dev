package enigma.halodev.controller.api;

import enigma.halodev.dto.UserDTO;
import enigma.halodev.dto.response.PageResponse;
import enigma.halodev.dto.response.Response;
import enigma.halodev.dto.response.SuccessResponse;
import enigma.halodev.model.User;
import enigma.halodev.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<PageResponse<User>> getAll(
            @PageableDefault Pageable pageable
    ) {
        return Response.page(userService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<User>> getById(
            @PathVariable Long id
    ) {
        return Response.success(userService.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SuccessResponse<User>> updateById(
            @PathVariable Long id,
            @Valid @RequestBody UserDTO dto
    ) {
        return Response.success(userService.updateById(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<String>> deleteById(
            @PathVariable Long id
    ) {
        userService.deleteById(id);
        return Response.success("User deleted");
    }

    @PostMapping(consumes = "multipart/form-data", path = "/profile-picture")
    public ResponseEntity<SuccessResponse<User>> uploadProfilePicture(Authentication auth,
            @RequestPart("image") MultipartFile image
    ) throws IOException {
        return Response.success(userService.uploadProfilePicture(auth, image), "Image uploaded", HttpStatus.OK);
    }
}
