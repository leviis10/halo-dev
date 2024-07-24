package enigma.halodev.controller.api;

import enigma.halodev.dto.response.Response;
import enigma.halodev.dto.response.SuccessResponse;
import enigma.halodev.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/cloudinary")
@RequiredArgsConstructor
public class CloudinaryController {
    private final CloudinaryService cloudinaryService;

    @PostMapping(consumes = "multipart/form-data", path = "/upload")
    public ResponseEntity<SuccessResponse<String>> upload(@RequestPart("image") MultipartFile image) throws IOException {
        String result = cloudinaryService.upload(image);
        return Response.success(result, "Upload Success", HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/")
    public ResponseEntity<SuccessResponse<String>> delete(@RequestParam String publicId) throws IOException {
        cloudinaryService.delete(publicId);

        return Response.success(publicId, "Delete Success", HttpStatus.OK);
    }
}
