package enigma.halodev.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
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

    private String description;

    @NotBlank(message = "Programmer id must not be blank")
    private Long programmerId;

    @NotBlank(message = "Topic id must not be blank")
    private Long topicId;
}
