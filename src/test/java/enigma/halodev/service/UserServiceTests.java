package enigma.halodev.service;

import enigma.halodev.dto.UserDTO;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    private UserDetails userDetails;

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
}
