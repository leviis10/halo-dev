import enigma.halodev.controller.SessionControllerTests;
import enigma.halodev.controller.SkillControllerTests;
import enigma.halodev.controller.TopicControllerTests;
import enigma.halodev.repository.SkillRepositoryTests;
import enigma.halodev.repository.TopicRepositoryTests;
import enigma.halodev.security.CustomUserDetailsServiceTests;
import enigma.halodev.security.JwtUtilsTests;
import enigma.halodev.security.SecurityConfigTests;
import enigma.halodev.service.*;
import enigma.halodev.utils.ConvertMultipartToFileTests;
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
        SessionControllerTests.class,
        SessionServiceTests.class,
        ProgrammmerServiceTests.class,
        AuthServiceTests.class,
        UserServiceTests.class,
        CustomUserDetailsServiceTests.class,
        SecurityConfigTests.class,
        JwtUtilsTests.class,
        ConvertMultipartToFileTests.class
})

public class TestSuite {
}
