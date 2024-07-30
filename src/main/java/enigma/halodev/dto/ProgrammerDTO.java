package enigma.halodev.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import enigma.halodev.model.Availability;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProgrammerDTO {
    @NotNull(message = "Programmer price must not be blank")
    @Min(value = 0, message = "Price cannot be negative number")
    private Double price;

    @NotNull(message = "Skill must not be blank")
    private Set<Long> skillsId;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ChangeAvailabilityDTO {
        @NotNull
        private Availability availability;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ChangePriceDTO {
        @NotNull(message = "Programmer price must not be blank")
        @Min(value = 0, message = "Price cannot be negative number")
        private Double price;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ChangeSkillsDTO {
        @NotNull(message = "Skill must not be blank")
        private Set<Long> skillsId;
    }
}
