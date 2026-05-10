package org.banking.challenge.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.banking.challenge.dtos.CreateTransactionRequest;
import org.banking.challenge.dtos.TransactionResponse;
import org.banking.challenge.services.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public TransactionResponse createTransaction(
            @Valid
            @RequestBody
            CreateTransactionRequest request,

            Authentication authentication
    ) {

        return transactionService.createTransaction(
                request,
                authentication
        );
    }

    @GetMapping
    public Page<TransactionResponse> getTransactions(

            Authentication authentication,

            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME
            )
            Instant from,

            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME
            )
            Instant to,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ) {

        return transactionService.getTransactions(
                authentication,
                from,
                to,
                page,
                size
        );
    }
}