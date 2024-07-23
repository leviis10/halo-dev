package enigma.halodev.exceptions;

public class TopicNotFoundException extends RuntimeException {
    public TopicNotFoundException() {
        super("Topic not found");
    }
}
