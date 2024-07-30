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
