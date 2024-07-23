package enigma.halodev.service;

import enigma.halodev.dto.LoginDTO;
import enigma.halodev.dto.RegisterDTO;

public interface AuthService {
    String register(RegisterDTO dto);

    String login(LoginDTO dto);
}
