package enigma.halodev.exception;

public class NotEnoughBalanceException extends RuntimeException {
    public NotEnoughBalanceException() {
        super("Your balance is not enough to make this session");
    }
}
