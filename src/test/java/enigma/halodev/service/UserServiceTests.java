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
    void TestService_ChangePassword_ChangePasswordSuccess() {
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
    public void testUploadProfilePicture() throws IOException {
        when(authentication.getPrincipal()).thenReturn(user);
        when(cloudinaryService.upload(user, image)).thenReturn("newProfilePictureUrl");
        when(userRepository.save(user)).thenReturn(user);

        User updatedUser = userService.uploadProfilePicture(authentication, image);

        assertEquals("newProfilePictureUrl", updatedUser.getProfilePicture());
        verify(cloudinaryService).upload(user, image);
        verify(userRepository).save(user);
    }

    @Test
    public void testDeleteUser() {
        doNothing().when(userRepository).delete(user);

        userService.delete(user);

        verify(userRepository).delete(user);
    }

    @Test
    public void testAddBalanceAfterTransaction() {
        double amountToAdd = 50.0;
        when(userRepository.save(user)).thenReturn(user);

        userService.addBalanceAfterTransaction(user, amountToAdd);

        assertEquals(150.0, user.getBalance());
        verify(userRepository).save(user);
    }

    @Test
    public void testChargeUserAfterTransaction() {
        double amountToCharge = 30.0;
        when(userRepository.save(user)).thenReturn(user);

        userService.chargeUserAfterTransaction(user, amountToCharge);

        assertEquals(70.0, user.getBalance());
        verify(userRepository).save(user);
    }

    @Test
    public void testAddProgrammerBalanceAfterTransaction() {
        Programmer programmer = new Programmer();
        programmer.setUser(user);

        double amountToAdd = 20.0;
        when(userRepository.save(user)).thenReturn(user);

        userService.addProgrammerBalanceAfterTransaction(programmer, amountToAdd);

        assertEquals(120.0, user.getBalance());
        verify(userRepository).save(user);
    }

    @Test
    void UserService_UpdateUser_Fail(){
        // given
        User incorrectUser = new User();
        incorrectUser.setFirstName(null);
        incorrectUser.setEmail(null);

        when(userRepository.save(any(User.class))).thenReturn(incorrectUser);

        // when
        User result = userService.updateUser(user, userDTO);

        // then
        assertNull(incorrectUser.getFirstName());
        assertNull(incorrectUser.getEmail());
        verify(userRepository, times(0)).save(incorrectUser);
    }

    @Test
    void UserService_GetCurrentAuthenticatedUser_NotAthenticated() {
        // given
        SecurityContextHolder.getContext().setAuthentication(null);

        // when
        User actualUser = userService.getCurrentAuthenticatedUser(null);

        // then
        assertEquals(null, actualUser);
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
}
