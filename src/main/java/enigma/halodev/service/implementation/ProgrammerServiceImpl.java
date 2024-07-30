package enigma.halodev.service.implementation;

import enigma.halodev.dto.ProgrammerDTO;
import enigma.halodev.exception.UserNotFoundException;
import enigma.halodev.model.Availability;
import enigma.halodev.model.Programmer;
import enigma.halodev.model.Skill;
import enigma.halodev.model.User;
import enigma.halodev.repository.ProgrammerRepository;
import enigma.halodev.service.ProgrammerService;
import enigma.halodev.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProgrammerServiceImpl implements ProgrammerService {
    private final ProgrammerRepository programmerRepository;
    private final SkillService skillService;

    @Override
    public Programmer create(User user, ProgrammerDTO dto) {
        Set<Skill> skills = skillService.getAllById(dto.getSkillsId());

        return programmerRepository.save(Programmer.builder()
                .user(user)
                .availability(Availability.AVAILABLE)
                .price(dto.getPrice())
                .skills(skills)
                .build()
        );
    }

    @Override
    public Page<Programmer> getAll(Pageable pageable) {
        return programmerRepository.findAll(pageable);
    }

    @Override
    public Programmer getCurrent(User user) {
        if (user.getProgrammer() == null) {
            throw new UserNotFoundException();
        }

        return user.getProgrammer();
    }

    @Override
    public Programmer getById(Long id) {
        return programmerRepository.findById(id).
                orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Programmer updateAvailability(
            User user,
            ProgrammerDTO.ChangeAvailabilityDTO changeAvailabilityDTO
    ) {
        Programmer currentProgrammer = getCurrent(user);
        currentProgrammer.setAvailability(changeAvailabilityDTO.getAvailability());
        return programmerRepository.save(currentProgrammer);
    }

    @Override
    public Programmer updatePrice(User user, ProgrammerDTO.ChangePriceDTO changePriceDTO) {
        Programmer currentProgrammer = getCurrent(user);
        user.getProgrammer().setPrice(changePriceDTO.getPrice());
        return programmerRepository.save(currentProgrammer);
    }

    @Override
    public Programmer updateSkills(
            User user,
            ProgrammerDTO.ChangeSkillsDTO changeSkillsDTO
    ) {
        Programmer currentProgrammer = getCurrent(user);
        Set<Skill> foundSkills = skillService.getAllById(changeSkillsDTO.getSkillsId());

        user.getProgrammer().setSkills(foundSkills);

        return programmerRepository.save(currentProgrammer);
    }

    @Override
    public void deleteProgrammer(User user) {
        Programmer currentProgrammer = getCurrent(user);
        programmerRepository.delete(currentProgrammer);
    }
}
