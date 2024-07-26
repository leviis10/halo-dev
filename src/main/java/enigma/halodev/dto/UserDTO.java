package enigma.halodev.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    @NotBlank(message = "First name must not be blank")
    private String firstName;

    private String lastName;

    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @Builder
    @Getter
    public static class topUpDto {
        private String payment_url;

        private Double amount;
    }
}