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

    // ===== Get balance for a user =====
    public double getBalanceByUserId(Long userId) {
        return transactionRepository.calculateBalance(userId);  // Already returns 0 if no transactions
    }

    // ===== Get recent transactions (last 5) =====
    public List<Transaction> getRecentTransactions(Long userId) {
        return transactionRepository.findTop5ByUserIdOrderByDateDesc(userId);
    }

    // ===== Get all transactions for a user =====
    public List<Transaction> getAllTransactions(Long userId) {
        return transactionRepository.findByUser_IdOrderByDateDesc(userId);
    }
}
