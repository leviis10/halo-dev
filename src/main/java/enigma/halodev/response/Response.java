package enigma.halodev.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class Response {
    public static <T> ResponseEntity<?> renderJSON(T data, String message, HttpStatus httpStatus) {
        WebResponse<T> response = WebResponse.<T>builder()
                .message(message)
                .status(httpStatus.getReasonPhrase())
                .data(data)
                .build();
        return ResponseEntity.status(httpStatus).body(response);
    }

    public static <T> ResponseEntity<?> renderJSON(T data, String message) {
        return renderJSON(data, message, HttpStatus.OK);
    }

    public static <T> ResponseEntity<?> renderJSON(T data) {
        return renderJSON(data, "Success");
    }

    public static <T> ResponseEntity<?> renderError(String message, HttpStatus httpStatus, List<T> errors) {
        WebResponseError<T> response = WebResponseError.<T>builder()
                .message(message)
                .status(httpStatus.getReasonPhrase())
                .errors(errors)
                .build();
        return ResponseEntity.status(httpStatus).body(response);
    }
}
