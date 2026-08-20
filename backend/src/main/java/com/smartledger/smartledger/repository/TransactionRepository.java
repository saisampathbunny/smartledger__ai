package com.smartledger.smartledger.repository;

import com.smartledger.smartledger.model.Transaction;
import com.smartledger.smartledger.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long>
{
    List<Transaction> findByUser(User user);

    Optional<Transaction> findByIdAndUser(
            Long id,
            User user
    );
}
