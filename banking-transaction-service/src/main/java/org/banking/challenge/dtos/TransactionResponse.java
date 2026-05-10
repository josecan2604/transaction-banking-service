
package org.banking.challenge.dtos;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {

    private UUID id;
    private BigDecimal amount;
    private String currency;
    private String description;
    private String counterpartyIban;
    private Instant createdAt;
}