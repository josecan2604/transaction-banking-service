package org.banking.challenge.services;


import lombok.RequiredArgsConstructor;

import org.banking.challenge.dtos.CreateTransactionRequest;
import org.banking.challenge.dtos.TransactionResponse;
import org.banking.challenge.entities.LedgerTransaction;
import org.banking.challenge.entities.User;
import org.banking.challenge.repository.LedgerTransactionRepository;
import org.banking.challenge.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final LedgerTransactionRepository repository;
    private final UserRepository userRepository;

    @Transactional
    public TransactionResponse createTransaction(
            CreateTransactionRequest request,
            Authentication authentication
    ) {

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );

        LedgerTransaction transaction =
                LedgerTransaction.builder()
                        .user(user)
                        .amount(request.getAmount())
                        .currency(
                                request.getCurrency()
                                        .toUpperCase()
                        )
                        .description(
                                request.getDescription()
                                        .trim()
                        )
                        .counterpartyIban(
                                request.getCounterpartyIban()
                                        .replace(" ", "")
                        )
                        .build();

        LedgerTransaction saved =
                repository.save(transaction);

        return map(saved);
    }

    public Page<TransactionResponse> getTransactions(
            Authentication authentication,
            Instant from,
            Instant to,
            int page,
            int size
    ) {

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );

        return repository.findByUserIdAndCreatedAtBetween(
                        user.getId(),
                        from,
                        to,
                        PageRequest.of(page, size)
                )
                .map(this::map);
    }

    private TransactionResponse map(
            LedgerTransaction transaction
    ) {

        return TransactionResponse.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .description(transaction.getDescription())
                .counterpartyIban(
                        transaction.getCounterpartyIban()
                )
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}