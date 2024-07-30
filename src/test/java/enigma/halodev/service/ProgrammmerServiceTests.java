package enigma.halodev.service;

import enigma.halodev.dto.ProgrammerDTO;
import enigma.halodev.model.*;
import enigma.halodev.repository.ProgrammerRepository;
import enigma.halodev.service.implementation.ProgrammerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProgrammmerServiceTests {
    @InjectMocks
    ProgrammerServiceImpl programmerService;

    @Mock
    ProgrammerRepository programmerRepository;

    @Mock
    SkillService skillService;

    @Mock
    User mockedUser;

    @Mock
    Programmer mockedProgrammer;

    @Mock
    ProgrammerDTO.ChangeAvailabilityDTO changeAvailabilityDTO;

    @Mock
    ProgrammerDTO.ChangePriceDTO changePriceDTO;

    @Mock
    ProgrammerDTO.ChangeSkillsDTO changeSkillsDTO;

    private final Long programmerId = 1L;
    private ProgrammerDTO programmerDTO;
    private HashSet<Skill> skills;
    private Programmer savedProgrammer;

    @BeforeEach
    void beforeEach() {
        programmerDTO = new ProgrammerDTO();
        programmerDTO.setPrice(5000.0);

        savedProgrammer = Programmer.builder()
                .user(mockedUser)
                .availability(Availability.AVAILABLE)
                .price(programmerDTO.getPrice())
                .skills(skills)
                .build();
    }

    @Test
    void ProgrammerService_CreateProgrammer_ReturnProgrammerSuccess() {
        // given
        when(programmerRepository.save(any(Programmer.class))).thenReturn(savedProgrammer);

        // When
        Programmer result = programmerService.create(mockedUser, programmerDTO);

        // Then
        assertNotNull(result);
        assertEquals(mockedUser, result.getUser());
        assertEquals(Availability.AVAILABLE, result.getAvailability());
        assertEquals(programmerDTO.getPrice(), result.getPrice());
        assertEquals(skills, result.getSkills());

        verify(skillService, times(1)).getAllById(programmerDTO.getSkillsId());
        verify(programmerRepository, times(1)).save(any(Programmer.class));
    }

    @Test
    void ProgrammerService_GetAllProgrammer_ReturnAllProgrammer() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Programmer> programmers = new PageImpl<>(Collections.singletonList(mockedProgrammer), pageable, 1);
        when(programmerRepository.findAll(pageable)).thenReturn(programmers);

        // when
        Page<Programmer> result = programmerService.getAll(pageable);

        // then
        assertEquals(programmers, result);
        verify(programmerRepository, times(1)).findAll(pageable);
    }

    @Test
    void ProgrammerService_GetCurrentProgrammer_ReturnSpecificProgrammer(){
        // given
        when(mockedUser.getProgrammer()).thenReturn(mockedProgrammer);

        // When
        Programmer result = programmerService.getCurrent(mockedUser);

        // Then
        assertNotNull(result);
        assertEquals(mockedProgrammer, result);
    }

    @Test
    void ProgrammerService_GetProgrammerById_ReturnSpecificProgrammer(){
        // given
        when(programmerRepository.findById(programmerId)).thenReturn(Optional.of(mockedProgrammer));

        // When
        Programmer result = programmerService.getById(programmerId);

        // Then
        assertEquals(mockedProgrammer, result);
        verify(programmerRepository, times(1)).findById(programmerId);
    }

    @Test
    void ProgrammerService_ProgrammerUpdateAvailability_ReturnUpdateSuccess(){
        // given
        Availability newAvailability = Availability.AVAILABLE;

        when(changeAvailabilityDTO.getAvailability()).thenReturn(newAvailability);
        when(mockedUser.getProgrammer()).thenReturn(mockedProgrammer);
        when(programmerRepository.save(mockedProgrammer)).thenReturn(mockedProgrammer);

        // when
        Programmer result = programmerService.updateAvailability(mockedUser, changeAvailabilityDTO);

        // Then
        verify(mockedProgrammer).setAvailability(newAvailability);
        verify(programmerRepository, times(1)).save(mockedProgrammer);
        assertEquals(mockedProgrammer, result);
    }

    @Test
    void ProgrammerService_ProgrammerUpdatePrice_ReturnUpdateSuccess(){
        // given
        double newPrice = 1000.00;

        when(changePriceDTO.getPrice()).thenReturn(newPrice);
        when(mockedUser.getProgrammer()).thenReturn(mockedProgrammer);
        when(programmerRepository.save(mockedProgrammer)).thenReturn(mockedProgrammer);

        // When
        Programmer result = programmerService.updatePrice(mockedUser, changePriceDTO);

        // Then
        verify(mockedProgrammer).setPrice(newPrice);
        verify(programmerRepository, times(1)).save(mockedProgrammer);
        assertEquals(mockedProgrammer, result);
    }

    @Test
    void ProgrammerService_ProgrammerUpdateSkills_ReturnUpdateSuccess(){
        // given
        Set<Long> skillIds = Set.of(1L, 2L);

        when(changeSkillsDTO.getSkillsId()).thenReturn(skillIds);
        when(skillService.getAllById(skillIds)).thenReturn(skills);
        when(mockedUser.getProgrammer()).thenReturn(mockedProgrammer);
        when(programmerRepository.save(mockedProgrammer)).thenReturn(mockedProgrammer);

        // When
        Programmer result = programmerService.updateSkills(mockedUser, changeSkillsDTO);

        // Then
        verify(mockedProgrammer).setSkills(skills);
        verify(programmerRepository, times(1)).save(mockedProgrammer);
        assertEquals(mockedProgrammer, result);
    }

    @Test
    void ProgrammerService_ProgrammerDeleteById_ReturnDeleteSuccess(){
        // given
        when(mockedUser.getProgrammer()).thenReturn(mockedProgrammer);

        // When
        programmerService.deleteProgrammer(mockedUser);

        // Then
        verify(programmerRepository, times(1)).delete(mockedProgrammer);
        Optional<Programmer> deletedTransaction = programmerRepository.findById(programmerId);
        assertThat(deletedTransaction).isEmpty();
    }
}
