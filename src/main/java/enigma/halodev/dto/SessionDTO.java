package enigma.halodev.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionDTO {
    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank
    private String description;

    @NotNull(message = "Programmer id must not be blank")
    private Long programmerId;

    @NotNull(message = "Topic id must not be blank")
    private Long topicId;
}
