package com.example.repository;

import com.example.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Correct nested field mapping for user.id
    List<Transaction> findTop5ByUser_IdOrderByDateDesc(Long userId);

    @Query("SELECT SUM(CASE WHEN t.type = 'Credit' THEN t.amount ELSE -t.amount END) " +
            "FROM Transaction t WHERE t.user.id = :userId")
    Double getUserBalance(Long userId);
}
