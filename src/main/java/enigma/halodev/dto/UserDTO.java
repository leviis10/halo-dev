package enigma.halodev.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @Email
    private String email;

    @Getter
    public static class ChangePasswordDTO {
        @NotBlank
        private String oldPassword;

        @NotBlank
        private String newPassword;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class TopUpDto {
        private String payment_url;

        @NotNull
        private Double amount;
    }
}