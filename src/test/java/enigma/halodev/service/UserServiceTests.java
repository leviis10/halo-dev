package enigma.halodev.service;

import enigma.halodev.dto.UserDTO;
import enigma.halodev.exception.PasswordNotMatchException;
import enigma.halodev.model.Programmer;
import enigma.halodev.model.User;
import enigma.halodev.repository.UserRepository;
import enigma.halodev.service.implementation.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private MultipartFile image;

    @Mock
    User user;

    @Mock
    UserDTO.ChangePasswordDTO changePasswordDTO;

    @Mock
    UserDTO userDTO;

    @BeforeEach
    public void beforeEach() {
        SecurityContextHolder.setContext(securityContext);

        user = new User();
        user.setFirstName("OldFirstName");
        user.setLastName("OldLastName");
        user.setEmail("old.email@example.com");

        userDTO = new UserDTO();
        userDTO.setFirstName("NewFirstName");
        userDTO.setLastName("NewLastName");
        userDTO.setEmail("new.email@example.com");

        user.setPassword("encodedOldPassword");
        user.setBalance(100.0);
        user.setProfilePicture("initialProfilePictureUrl");

        changePasswordDTO = new UserDTO.ChangePasswordDTO();
        changePasswordDTO.setOldPassword("oldPassword");
        changePasswordDTO.setNewPassword("newPassword");
    }

    @Test
    void UserService_GetCurrentAuthenticatedUser_UserAuthenticated() {
        // given
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        User actualUser = userService.getCurrentAuthenticatedUser(user);

        // then
        assertEquals(user.getUsername(), actualUser.getUsername());
    }

    @Test
    void UserService_UpdateUser_ReturnUpdatedUser(){
        // given
        User updatedUser = new User();
        updatedUser.setFirstName(userDTO.getFirstName());
        updatedUser.setLastName(userDTO.getLastName());
        updatedUser.setEmail(userDTO.getEmail());

        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // when
        User result = userService.updateUser(user, userDTO);

        // then
        assertEquals(userDTO.getFirstName(), result.getFirstName());
        assertEquals(userDTO.getLastName(), result.getLastName());
        assertEquals(userDTO.getEmail(), result.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void UserService_ChangePassword_ChangePasswordSuccess() {
        // given
        when(passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(changePasswordDTO.getNewPassword())).thenReturn("encodedNewPassword");

        // when
        userService.changePassword(user, changePasswordDTO);

        // then
        verify(userRepository, times(1)).save(user);

        // check if password has been updated
        assertEquals("encodedNewPassword", user.getPassword());
    }

    @Test
    void UserService_UploadProfilePicture_Success() throws IOException {
        // given
        when(authentication.getPrincipal()).thenReturn(user);
        when(cloudinaryService.upload(user, image)).thenReturn("newProfilePictureUrl");
        when(userRepository.save(user)).thenReturn(user);

        // when
        User updatedUser = userService.uploadProfilePicture(authentication, image);

        // then
        assertEquals("newProfilePictureUrl", updatedUser.getProfilePicture());
        verify(cloudinaryService).upload(user, image);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void UserService_DeleteUser_Success() {
        // given
        doNothing().when(userRepository).delete(user);

        // when
        userService.delete(user);

        // then
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void UserService_AddBalanceAfterTransaction_Success() {
        // given
        double amountToAdd = 50.0;
        when(userRepository.save(user)).thenReturn(user);

        // when
        userService.addBalanceAfterTransaction(user, amountToAdd);

        // then
        assertEquals(150.0, user.getBalance());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void UserService_ChargeUserAfterTransaction_Success() {
        // given
        double amountToCharge = 30.0;
        when(userRepository.save(user)).thenReturn(user);

        // when
        userService.chargeUserAfterTransaction(user, amountToCharge);

        // then
        assertEquals(70.0, user.getBalance());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void UserService_AddProgrammerBalanceAfterTransaction_Success() {
        // given
        Programmer programmer = new Programmer();
        programmer.setUser(user);

        double amountToAdd = 20.0;
        when(userRepository.save(user)).thenReturn(user);

        // when
        userService.addProgrammerBalanceAfterTransaction(programmer, amountToAdd);

        // then
        assertEquals(120.0, user.getBalance());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void TestService_ChangePassword_Fail() {
        // when
        when(passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())).thenReturn(false);

        // then
        assertThatThrownBy(() -> userService.changePassword(user, changePasswordDTO))
                .isInstanceOf(PasswordNotMatchException.class)
                .hasMessageContaining("Password doesn't match");

        verify(userRepository, never()).save(any(User.class));

        verify(passwordEncoder).matches(changePasswordDTO.getOldPassword(), user.getPassword());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    public void UserService_UploadProfilePictureThrowsIOException() throws IOException {
        when(authentication.getPrincipal()).thenReturn(user);
        when(cloudinaryService.upload(user, image)).thenThrow(new IOException("Upload failed"));

        IOException thrownException = assertThrows(IOException.class, () -> {
            userService.uploadProfilePicture(authentication, image);
        });

        assertEquals("Upload failed", thrownException.getMessage());
        verify(cloudinaryService).upload(user, image);
        verify(userRepository, never()).save(any(User.class)); // Ensure save is not called
    }
}
