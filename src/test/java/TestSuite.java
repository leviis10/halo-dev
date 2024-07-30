import enigma.halodev.controller.SkillControllerTests;
import enigma.halodev.controller.TopicControllerTests;
import enigma.halodev.repository.SkillRepositoryTests;
import enigma.halodev.repository.TopicRepositoryTests;
import enigma.halodev.service.AuthServiceTests;
import enigma.halodev.service.SkillServiceTests;
import enigma.halodev.service.TopicServiceTests;
import enigma.halodev.service.TransactionServiceTests;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        TopicServiceTests.class,
        TopicRepositoryTests.class,
        TopicControllerTests.class,
        SkillServiceTests.class,
        SkillRepositoryTests.class,
        SkillControllerTests.class,
        TransactionServiceTests.class,
        AuthServiceTests.class
})

public class TestSuite {
}
