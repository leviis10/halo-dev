package enigma.halodev.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponse<T> {
    private String message;
    private int status;
    private List<T> data;
    private int totalPages;
    private long totalElements;
    private int page;
    private int size;
}
