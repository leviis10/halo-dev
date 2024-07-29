package enigma.halodev.service;

import enigma.halodev.dto.ProgrammerDTO;
import enigma.halodev.model.Programmer;
import enigma.halodev.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProgrammerService {
    Programmer create(User user, ProgrammerDTO dto);

    Page<Programmer> getAll(Pageable pageable);

    Programmer getCurrent(User user);

    Programmer getById(Long id);

    Programmer updateAvailability(User user, ProgrammerDTO.ChangeAvailabilityDTO changeAvailabilityDTO);

    Programmer updatePrice(User user, ProgrammerDTO.ChangePriceDTO changePriceDTO);

    Programmer updateSkills(User user, ProgrammerDTO.ChangeSkillsDTO changeSkillsDTO);

    void deleteProgrammer(User user);
}
