package org.banking.challenge.repository;

import org.banking.challenge.entities.LedgerTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.UUID;

public interface LedgerTransactionRepository extends JpaRepository<LedgerTransaction, UUID> {

    Page<LedgerTransaction> findByUserIdAndCreatedAtBetween(
            UUID userId,
            Instant from,
            Instant to,
            Pageable pageable );
}