package enigma.halodev.service.implementation;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import enigma.halodev.service.CloudinaryService;
import enigma.halodev.utils.ConvertMultipartToFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;

    @Override
    public String upload(String username, MultipartFile image) throws IOException {
        File convertedImage = ConvertMultipartToFile.convert(image);
        Transformation transformation = new Transformation<>()
                .gravity("face").height(100).width(100).crop("thumb").chain()
                .radius("max").chain(); // radius max = rounded
        Map uploadResult = cloudinary.uploader().upload(convertedImage, ObjectUtils.asMap(
                "transformation", transformation,
                "overwrite", true,
                "public_id", username + "_" + new Date().getTime(),
                "resource_type", "image",
                "format", "webp",
                "folder", "halodev/profile_picture"
        ));
        convertedImage.delete();
        return (String) uploadResult.get("url");
    }
}
