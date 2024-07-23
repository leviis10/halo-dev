package enigma.halodev.exception;

public class TopicNotFoundException extends RuntimeException {
    public TopicNotFoundException() {
        super("Topic not found");
    }
}
