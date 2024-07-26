package enigma.halodev.dto.midtrans;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionBody {
    private TransactionDetails transaction_details;
    private CustomerDetails customer_details;
    private PageExpiry page_expiry;
    private List<String> enabled_payments;
}
