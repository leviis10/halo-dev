package enigma.halodev.exception;

public class PasswordNotMatchException extends RuntimeException {
    public PasswordNotMatchException() {
        super("Password doesn't match");
    }
}
