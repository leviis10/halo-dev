package enigma.halodev.dto;

import enigma.halodev.model.PaymentStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionDTO {
    private Long user_id;

    private Double paymentNominal;

    private String redirectUrl;
}
