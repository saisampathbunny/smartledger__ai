package com.smartledger.smartledger.service;

import com.smartledger.smartledger.model.Transaction;
import org.springframework.stereotype.Service;

@Service
public class FraudDetectionService
{
    public FraudResult evaluate(Transaction transaction)
    {
        int score = 0;

        if (transaction.getAmount() > 50000)
        {
            score += 30;
        }

        if (transaction.getSenderAccount()
                .equals(transaction.getReceiverAccount()))
        {
            score += 30;
        }

        if (transaction.getTimestamp() != null &&
                isOddHour(transaction.getTimestamp()))
        {
            score += 20;
        }

        if (transaction.getAmount() % 1000 == 0)
        {
            score += 20;
        }

        String status =
                score >= 50
                        ? "SUSPICIOUS"
                        : "SAFE";

        return new FraudResult(score, status);
    }

    private boolean isOddHour(String timestamp)
    {
        try
        {
            String hourPart =
                    timestamp.substring(11, 13);

            int hour =
                    Integer.parseInt(hourPart);

            return hour >= 0 && hour <= 5;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public record FraudResult(
            int score,
            String status)
    {
    }
}
