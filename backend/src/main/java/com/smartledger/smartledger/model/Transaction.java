package com.smartledger.smartledger.model;

import jakarta.persistence.*;

@Entity
@Table(name = "transactions")
public class Transaction
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String senderAccount;
    private String receiverAccount;
    private Double amount;
    private String transactionType;
    private String timestamp;
    private String fraudStatus;
    private String explanation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getSenderAccount()
    {
        return senderAccount;
    }

    public void setSenderAccount(String senderAccount)
    {
        this.senderAccount = senderAccount;
    }

    public String getReceiverAccount()
    {
        return receiverAccount;
    }

    public void setReceiverAccount(String receiverAccount)
    {
        this.receiverAccount = receiverAccount;
    }

    public Double getAmount()
    {
        return amount;
    }

    public void setAmount(Double amount)
    {
        this.amount = amount;
    }

    public String getTransactionType()
    {
        return transactionType;
    }

    public void setTransactionType(String transactionType)
    {
        this.transactionType = transactionType;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getFraudStatus()
    {
        return fraudStatus;
    }

    public void setFraudStatus(String fraudStatus)
    {
        this.fraudStatus = fraudStatus;
    }

    public String getExplanation()
    {
        return explanation;
    }

    public void setExplanation(String explanation)
    {
        this.explanation = explanation;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}
