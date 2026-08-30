package com.smartledger.smartledger.service;

import com.smartledger.smartledger.model.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Service
public class FraudDetectionService
{
    private static final double LARGE_AMOUNT_THRESHOLD = 50000;
    private static final double ROUND_NUMBER_DIVISOR = 10000;
    private static final int SUSPICIOUS_SCORE_THRESHOLD = 50;

    public FraudResult evaluate(Transaction transaction)
    {
        int score = 0;

        if (isLargeAmount(transaction))
        {
            score += 30;
        }

        if (isSelfTransfer(transaction))
        {
            score += 30;
        }

        if (isOddHour(transaction))
        {
            score += 20;
        }

        if (isRoundNumber(transaction))
        {
            score += 20;
        }

        String status =
                score >= SUSPICIOUS_SCORE_THRESHOLD
                        ? "SUSPICIOUS"
                        : "SAFE";

        return new FraudResult(status, score);
    }

    private boolean isLargeAmount(Transaction transaction)
    {
        return transaction.getAmount() != null
                && transaction.getAmount() > LARGE_AMOUNT_THRESHOLD;
    }

    private boolean isSelfTransfer(Transaction transaction)
    {
        return transaction.getSenderAccount() != null
                && transaction.getSenderAccount()
                        .equals(transaction.getReceiverAccount());
    }

    private boolean isRoundNumber(Transaction transaction)
    {
        return transaction.getAmount() != null
                && transaction.getAmount()
                        % ROUND_NUMBER_DIVISOR == 0;
    }

    private boolean isOddHour(Transaction transaction)
    {
        if (transaction.getTimestamp() == null)
        {
            return false;
        }

        try
        {
            LocalDateTime dateTime =
                    LocalDateTime.parse(
                            transaction.getTimestamp()
                    );

            int hour =
                    dateTime.getHour();

            return hour >= 0 && hour < 5;
        }
        catch (DateTimeParseException e)
        {
            return false;
        }
    }

    public record FraudResult(
            String status,
            int score)
    {
    }
}
