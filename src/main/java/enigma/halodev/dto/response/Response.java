package enigma.halodev.dto.response;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class Response {
    public static <T> ResponseEntity<SuccessResponse<T>> success(T data, String message, HttpStatus status) {
        SuccessResponse<T> response = SuccessResponse.<T>builder()
                .message(message)
                .status(status.value())
                .data(data)
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    public static <T> ResponseEntity<SuccessResponse<T>> success(T data, String message) {
        return success(data, message, HttpStatus.OK);
    }

    public static <T> ResponseEntity<SuccessResponse<T>> success(T data) {
        return success(data, HttpStatus.OK.getReasonPhrase());
    }

    public static ResponseEntity<ErrorResponse> error(List<String> errors, String message, HttpStatus status) {
        ErrorResponse response = ErrorResponse.builder()
                .message(message)
                .status(status.value())
                .errors(errors)
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    public static ResponseEntity<ErrorResponse> error(List<String> errors, String message) {
        return error(errors, message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity<ErrorResponse> error(List<String> errors) {
        return error(errors, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    public static <T> ResponseEntity<PageResponse<T>> page(Page<T> page, String message, HttpStatus status) {
        PageResponse<T> response = PageResponse.<T>builder()
                .message(message)
                .status(status.value())
                .data(page.getContent())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .page(page.getNumber())
                .size(page.getSize())
                .build();
        return ResponseEntity.status(status).body(response);
    }

    public static <T> ResponseEntity<PageResponse<T>> page(Page<T> page, String message) {
        return page(page, message, HttpStatus.OK);
    }

    public static <T> ResponseEntity<PageResponse<T>> page(Page<T> page) {
        return page(page, HttpStatus.OK.getReasonPhrase());
    }
}
