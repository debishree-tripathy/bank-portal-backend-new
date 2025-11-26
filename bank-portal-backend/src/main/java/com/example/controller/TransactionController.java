package com.example.controller;

import com.example.model.Transaction;
import com.example.model.User;
import com.example.service.TransactionService;
import com.example.repository.UserRepository;
import com.example.repository.TransactionRepository;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionController(TransactionService transactionService,
                                 TransactionRepository transactionRepository,
                                 UserRepository userRepository) {
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    // ===== Get Balance =====
    @GetMapping("/balance/{userId}")
    public Map<String, Object> getBalance(@PathVariable Long userId) {
        double balance = transactionService.getBalanceByUserId(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("balance", balance);
        return response;
    }

    // ===== Get recent transactions =====
    @GetMapping("/recent/{userId}")
    public Map<String, Object> getRecentTransactions(@PathVariable Long userId) {
        List<Transaction> transactions = transactionService.getRecentTransactions(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("transactions", transactions);
        return response;
    }

    // ===== Get all transactions for a user (for "View All") =====
    @GetMapping("/all/{userId}")
    public Map<String, Object> getAllTransactions(@PathVariable Long userId) {
        List<Transaction> transactions = transactionService.getAllTransactions(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("transactions", transactions);
        return response;
    }

    // ===== Add a transaction dynamically =====
    @PostMapping("/add/{userId}")
    public Transaction addTransaction(@PathVariable Long userId,
                                      @RequestParam double amount,
                                      @RequestParam String type) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Transaction tx = new Transaction();
        tx.setUser(user);
        tx.setAmount(amount);
        tx.setType(type); // "Credit" or "Debit"
        tx.setDate(LocalDateTime.now());

        return transactionRepository.save(tx);
    }
}
