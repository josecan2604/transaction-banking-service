package org.banking.challenge.dtos;


import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionRequest {

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{3}$")
    private String currency;

    @NotBlank
    @Size(max = 500)
    private String description;

    @NotBlank
    @Pattern(regexp = "^[A-Z0-9]{15,34}$")
    private String counterpartyIban;
}