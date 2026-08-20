package com.smartledger.smartledger.service;

import com.smartledger.smartledger.model.Transaction;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class GeminiService
{
    @Value("${gemini.api.key}")
    private String apiKey;

    public String explainFlag(
            Transaction transaction,
            int score)
    {
        try
        {
            String prompt =
                    "Explain why this bank transaction "
                    + "was flagged as suspicious. "
                    + "Fraud score: "
                    + score
                    + ". Amount: "
                    + transaction.getAmount()
                    + ". Transaction type: "
                    + transaction.getTransactionType()
                    + ". Give one short plain-English explanation.";

            String requestBody =
                    """
                    {
                      "contents": [
                        {
                          "parts": [
                            {
                              "text": "%s"
                            }
                          ]
                        }
                      ]
                    }
                    """.formatted(
                            prompt.replace("\"", "\\\"")
                    );

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(URI.create(
                                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key="
                                            + apiKey
                            ))
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest.BodyPublishers
                                            .ofString(requestBody)
                            )
                            .build();

            HttpResponse<String> response =
                    HttpClient.newHttpClient()
                            .send(
                                    request,
                                    HttpResponse.BodyHandlers
                                            .ofString()
                            );

            if (response.statusCode() >= 200 &&
                    response.statusCode() < 300)
            {
                return response.body();
            }
        }
        catch (Exception ignored)
        {
        }

        return "Transaction was flagged as suspicious based on its fraud score.";
    }
}
