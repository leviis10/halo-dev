import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        enigma.halodev.service.TopicServiceTests.class,
        enigma.halodev.repository.TopicRepositoryTests.class,
        enigma.halodev.controller.TopicControllerTests.class,
        enigma.halodev.service.SkillServiceTests.class,
        enigma.halodev.repository.SkillRepositoryTests.class,
        enigma.halodev.controller.SkillControllerTests.class,
        enigma.halodev.service.TransactionServiceTests.class,
        enigma.halodev.service.ProgrammmerServiceTests.class
})

public class TestSuite {
}
