package enigma.halodev.service.implementation;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import enigma.halodev.service.CloudinaryService;
import enigma.halodev.utils.ConvertMultipartToFile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;

    @Override
    public String upload(MultipartFile image) throws IOException {
        try{

            File base64Image = ConvertMultipartToFile.convert(image);
            Map uploadResult = cloudinary.uploader().upload(base64Image, ObjectUtils.asMap("resource_type", "image", "folder", "halodev"));
            HashMap<String, String> resultResponse = new HashMap<>();

            resultResponse.put("secure_url", (String) uploadResult.get("secure_url"));
            resultResponse.put("public_id", (String) uploadResult.get("public_id"));

            return resultResponse.toString();
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public void delete(String publicId) throws IOException {
        try{
            cloudinary.uploader().destroy(publicId, Map.of());
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }
}
