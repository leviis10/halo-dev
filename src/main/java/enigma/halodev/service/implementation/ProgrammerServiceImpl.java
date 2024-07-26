package enigma.halodev.service.implementation;

import enigma.halodev.dto.ProgrammerDTO;
import enigma.halodev.dto.ProgrammerSkillsDTO;
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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgrammerServiceImpl implements ProgrammerService {
    private final ProgrammerRepository programmerRepository;
    private final SkillService skillService;

    @Override
    public Programmer create(Authentication auth, ProgrammerDTO dto) {
        User user = (User) auth.getPrincipal();

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
    public Programmer getById(Long id) {
        return programmerRepository.findById(id).
                orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Programmer updateById(Authentication auth, ProgrammerDTO dto) {
        User user = (User) auth.getPrincipal();
        Programmer programmer = programmerRepository.findByUserId(user.getId());
        programmer.setPrice(dto.getPrice());

        return programmerRepository.save(programmer);
    }

    @Override
    public Programmer deleteProgrammerSkill(Authentication auth, ProgrammerSkillsDTO dto) {
        User user = (User) auth.getPrincipal();
        Programmer programmer = programmerRepository.findByUserId(user.getId());
        Set<Skill> updatedSkills = new HashSet<>();
        updatedSkills.addAll(programmer.getSkills()
                .stream()
                .filter(value -> !(dto.getSkillsId().contains(value.getId())))
                .collect(Collectors.toSet())
        );

        programmer.setSkills(updatedSkills);

        return programmerRepository.save(programmer);
    }

    @Override
    public Programmer addProgrammerSkill(Authentication auth, ProgrammerSkillsDTO dto) {
        User user = (User) auth.getPrincipal();
        Programmer programmer = programmerRepository.findByUserId(user.getId());
        Set<Skill> foundSkills = skillService.getAllById(dto.getSkillsId());
        programmer.getSkills().addAll(foundSkills);

        return programmerRepository.save(programmer);
    }

    @Override
    public Programmer updateAvailability(Programmer request) {
        if(request.getAvailability().equals(Availability.AVAILABLE)) {
            request.setAvailability(Availability.NOT_AVAILABLE);
        } else {
            request.setAvailability(Availability.AVAILABLE);
        }
        return programmerRepository.save(request);
    }

    @Override
    public void deleteById(Long id) {
        programmerRepository.deleteById(id);
    }
}
