package com.smartledger.smartledger.controller;

import com.smartledger.smartledger.model.Transaction;
import com.smartledger.smartledger.model.User;
import com.smartledger.smartledger.repository.TransactionRepository;
import com.smartledger.smartledger.repository.UserRepository;
import com.smartledger.smartledger.service.FraudDetectionService;
import com.smartledger.smartledger.service.GeminiService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/transactions")
public class TransactionController
{
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final FraudDetectionService fraudDetectionService;
    private final GeminiService geminiService;

    public TransactionController(
            TransactionRepository transactionRepository,
            UserRepository userRepository,
            FraudDetectionService fraudDetectionService,
            GeminiService geminiService)
    {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.fraudDetectionService = fraudDetectionService;
        this.geminiService = geminiService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTransaction(
            @RequestBody Transaction transaction,
            Authentication authentication)
    {
        User user = getLoggedInUser(authentication);

        if (user == null)
        {
            return ResponseEntity.status(401)
                    .body("Unauthorized");
        }

        if (transaction.getAmount() == null ||
                transaction.getAmount() <= 0)
        {
            return ResponseEntity.badRequest()
                    .body(
                            "Transaction amount must be greater than zero."
                    );
        }

        if (transaction.getSenderAccount() == null ||
                transaction.getSenderAccount().isBlank())
        {
            return ResponseEntity.badRequest()
                    .body("Sender account is required.");
        }

        if (transaction.getReceiverAccount() == null ||
                transaction.getReceiverAccount().isBlank())
        {
            return ResponseEntity.badRequest()
                    .body("Receiver account is required.");
        }

        FraudDetectionService.FraudResult result =
                fraudDetectionService.evaluate(transaction);

        transaction.setFraudStatus(result.status());

        String explanation =
                geminiService.explainFlag(
                        transaction,
                        result.score()
                );

        transaction.setExplanation(explanation);

        transaction.setUser(user);

        return ResponseEntity.ok(
                transactionRepository.save(transaction)
        );
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTransactions(
            Authentication authentication)
    {
        User user = getLoggedInUser(authentication);

        if (user == null)
        {
            return ResponseEntity.status(401)
                    .body("Unauthorized");
        }

        List<Transaction> transactions =
                transactionRepository.findByUser(user);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/one/{id}")
    public ResponseEntity<?> getTransactionById(
            @PathVariable Long id,
            Authentication authentication)
    {
        User user = getLoggedInUser(authentication);

        if (user == null)
        {
            return ResponseEntity.status(401)
                    .body("Unauthorized");
        }

        return transactionRepository
                .findByIdAndUser(id, user)
                .<ResponseEntity<?>>map(
                        ResponseEntity::ok
                )
                .orElseGet(() ->
                        ResponseEntity.status(404)
                                .body(
                                        "No transaction found with id "
                                                + id
                                )
                );
    }

    private User getLoggedInUser(
            Authentication authentication)
    {
        if (authentication == null)
        {
            return null;
        }

        return userRepository
                .findByEmail(authentication.getName())
                .orElse(null);
    }
}
