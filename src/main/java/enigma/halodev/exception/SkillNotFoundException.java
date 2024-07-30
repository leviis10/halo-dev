package enigma.halodev.exception;

public class SkillNotFoundException extends RuntimeException {
    public SkillNotFoundException() {
        super("Skill not found");
    }
}
