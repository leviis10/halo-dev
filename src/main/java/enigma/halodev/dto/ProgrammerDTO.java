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
public class ProgrammerDTO {
    @NotBlank(message = "User id must not be blank")
    private Long user_id;

    @NotBlank(message = "Programmer price must not be blank")
    private Double price;
}
