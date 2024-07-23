package enigma.halodev.exceptions;

public class SkillNotFoundException extends RuntimeException{
    public SkillNotFoundException() {
        super("Skill Not Found");
    }
}
