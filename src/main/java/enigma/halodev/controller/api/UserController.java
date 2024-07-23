package enigma.halodev.controller.api;

import enigma.halodev.dto.UserDTO;
import enigma.halodev.response.PageResponse;
import enigma.halodev.response.Response;
import enigma.halodev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping
    public ResponseEntity<?> getAll(
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) String name
    ) {
        return Response.renderJSON(new PageResponse<>(userService.getAll(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return Response.renderJSON(userService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable Integer id, @RequestBody UserDTO updatedUser) {
        return Response.renderJSON(userService.updateById(id, updatedUser), "Updated");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        userService.deleteById(id);
    }

    @PutMapping("/{id}/topup")
    public ResponseEntity<?> topup(@PathVariable Integer id, @RequestParam Integer amount) {
        return Response.renderJSON(userService.topup(id, amount), "Success Top Up");
    }

}
