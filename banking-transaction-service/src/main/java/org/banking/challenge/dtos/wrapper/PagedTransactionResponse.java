package org.banking.challenge.dtos.wrapper;

import lombok.*;
import org.banking.challenge.dtos.TransactionResponse;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PagedTransactionResponse {

    private List<TransactionResponse> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}