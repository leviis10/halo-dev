package enigma.halodev.service;

import enigma.halodev.dto.ProgrammerDTO;
import enigma.halodev.dto.SessionDTO;
import enigma.halodev.exception.NotEnoughBalanceException;
import enigma.halodev.exception.SessionNotFoundException;
import enigma.halodev.model.*;
import enigma.halodev.repository.SessionRepository;
import enigma.halodev.service.implementation.SessionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTests {

    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private UserService userService;
    @Mock
    private ProgrammerService programmerService;
    @Mock
    private TopicService topicService;

    @InjectMocks
    private SessionServiceImpl sessionService;

    private User user;
    private Programmer programmer;
    private Topic topic;
    private Session session1;
    private Session session2;

    @BeforeEach
    public void beforeEach() {
        user = User.builder().id(1L).balance(100.0).build();
        programmer = Programmer.builder()
                .id(1L)
                .user(User.builder().id(2L).build())
                .price(50.0)
                .availability(Availability.AVAILABLE)
                .build();
        topic = Topic.builder().id(1L).build();

        session1 = Session.builder()
                .id(1L)
                .name("session 1")
                .programmer(programmer)
                .user(user)
                .topic(topic)
                .completed(SessionStatus.NOT_COMPLETED)
                .build();
        session2 = Session.builder()
                .id(2L)
                .name("session 2")
                .programmer(programmer)
                .user(user)
                .topic(topic)
                .completed(SessionStatus.NOT_COMPLETED)
                .build();
    }

    @Test
    public void SessionService_GetAllSessions_ReturnAllSessions() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Session> sessions = new PageImpl<>(List.of(session1, session2), pageable, 2);
        given(sessionRepository.findAllByUser(pageable, user)).willReturn(sessions);

        Page<Session> foundSessions = sessionService.getAll(pageable, user);

        assertThat(foundSessions).hasSize(2);
        assertThat(foundSessions.getContent()).containsExactly(session1, session2);
    }

    @Test
    public void SessionService_CreateSession_ReturnCreatedSession() {
        SessionDTO sessionDto = SessionDTO.builder()
                .programmerId(programmer.getId())
                .topicId(topic.getId())
                .name("New Session")
                .description("Session Description")
                .build();

        given(programmerService.getById(programmer.getId())).willReturn(programmer);
        given(topicService.getById(topic.getId())).willReturn(topic);
        given(sessionRepository.save(any(Session.class))).willReturn(session1);

        Session createdSession = sessionService.create(user, sessionDto);

        assertThat(createdSession).isEqualTo(session1);
    }

    @Test
    public void SessionService_CreateSession_NotEnoughBalance_ShouldThrowException() {
        user.setBalance(10.0);
        SessionDTO sessionDto = SessionDTO.builder()
                .programmerId(programmer.getId())
                .topicId(topic.getId())
                .name("New Session")
                .description("Session Description")
                .build();

        given(programmerService.getById(programmer.getId())).willReturn(programmer);
        given(topicService.getById(topic.getId())).willReturn(topic);

        assertThatThrownBy(() -> sessionService.create(user, sessionDto))
                .isInstanceOf(NotEnoughBalanceException.class);
    }

    @Test
    public void SessionService_CreateSession_WithSelf_ShouldThrowException() {
        programmer.getUser().setId(user.getId());
        SessionDTO sessionDto = SessionDTO.builder()
                .programmerId(programmer.getId())
                .topicId(topic.getId())
                .name("New Session")
                .description("Session Description")
                .build();

        given(programmerService.getById(programmer.getId())).willReturn(programmer);
        given(topicService.getById(topic.getId())).willReturn(topic);

        assertThatThrownBy(() -> sessionService.create(user, sessionDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Cannot create session with yourself");
    }

    @Test
    public void SessionService_CreateSession_ProgrammerNotAvailable_ShouldThrowException() {
        programmer.setAvailability(Availability.NOT_AVAILABLE);
        SessionDTO sessionDto = SessionDTO.builder()
                .programmerId(programmer.getId())
                .topicId(topic.getId())
                .name("New Session")
                .description("Session Description")
                .build();

        given(programmerService.getById(programmer.getId())).willReturn(programmer);
        given(topicService.getById(topic.getId())).willReturn(topic);

        assertThatThrownBy(() -> sessionService.create(user, sessionDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("programmer is not available");
    }

    @Test
    public void SessionService_GetSessionById_ShouldReturnSession() {
        Long sessionId = session1.getId();
        given(sessionRepository.findByUserAndId(user, sessionId)).willReturn(Optional.of(session1));

        Session foundSession = sessionService.getById(user, sessionId);

        assertThat(foundSession).isEqualTo(session1);
    }

    @Test
    public void SessionService_GetSessionById_NotFound_ShouldThrowException() {
        Long sessionId = 1L;
        given(sessionRepository.findByUserAndId(user, sessionId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sessionService.getById(user, sessionId))
                .isInstanceOf(SessionNotFoundException.class);
    }

    @Test
    public void SessionService_CompleteSession_ShouldCompleteSession() {
        Long sessionId = session1.getId();
        session1.setCompleted(SessionStatus.NOT_COMPLETED);
        given(sessionRepository.findByUserAndId(user, sessionId)).willReturn(Optional.of(session1));
        given(sessionRepository.save(session1)).willReturn(session1);

        Session completedSession = sessionService.completeSession(user, sessionId);

        assertThat(completedSession.getCompleted()).isEqualTo(SessionStatus.COMPLETED);
        verify(programmerService, times(1)).updateAvailability(any(User.class), any(ProgrammerDTO.ChangeAvailabilityDTO.class));
    }

    @Test
    public void SessionService_CompleteSession_AlreadyCompleted_ShouldThrowException() {
        Long sessionId = session1.getId();
        session1.setCompleted(SessionStatus.COMPLETED);
        given(sessionRepository.findByUserAndId(user, sessionId)).willReturn(Optional.of(session1));

        assertThatThrownBy(() -> sessionService.completeSession(user, sessionId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Already completed");
    }
}
