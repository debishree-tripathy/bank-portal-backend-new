package com.example.service;

import com.example.model.Transaction;
import com.example.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Get user balance (sum of credits - debits)
    public double getBalanceByUserId(Long userId) {
        Double balance = transactionRepository.getUserBalance(userId);
        return balance != null ? balance : 0.0;
    }

    // Get last 5 transactions
    public List<Transaction> getRecentTransactions(Long userId) {
        return transactionRepository.findTop5ByUser_IdOrderByDateDesc(userId);
    }
}
