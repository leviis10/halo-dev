package enigma.halodev.service.implementation;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import enigma.halodev.dto.UserDTO;
import enigma.halodev.exception.UserNotFoundException;
import enigma.halodev.model.User;
import enigma.halodev.repository.UserRepository;
import enigma.halodev.service.UserService;
import enigma.halodev.utils.ConvertMultipartToFile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Cloudinary cloudinary;

    @Override
    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User updateById(Long id, UserDTO dto) {
        User foundUser = getById(id);
        foundUser.setFirstName(dto.getFirstName());
        foundUser.setLastName(dto.getLastName());
        foundUser.setUsername(dto.getUsername());
        foundUser.setEmail(dto.getEmail());
        return userRepository.save(foundUser);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User uploadProfilePicture(Authentication auth, MultipartFile image) throws IOException {
        User currentUser = (User) auth.getPrincipal();

        File convertedImage = ConvertMultipartToFile.convert(image);
        Map uploadResult = cloudinary.uploader().upload(convertedImage, ObjectUtils.asMap(
                "overwrite", true, // Replace foto profil jika sudah ada sebelumnya
                "public_id", currentUser.getUsername() + "_" + currentUser.getId(),
                "resource_type", "image",
                "folder", "halodev/profile_picture"
        ));
        currentUser.setProfilePicture(uploadResult.get("secure_url").toString());
        convertedImage.delete(); // Hapus gambar dari local setelah upload ke cloudinary

        return userRepository.save(currentUser);
    }
}
