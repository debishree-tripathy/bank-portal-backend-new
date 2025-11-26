package com.example.repository;

import com.example.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT COALESCE(SUM(CASE WHEN t.type = 'Credit' THEN t.amount ELSE -t.amount END), 0) " +
            "FROM Transaction t WHERE t.user.id = :userId")
    Double calculateBalance(@Param("userId") Long userId);

    // Get all transactions
    List<Transaction> findByUser_IdOrderByDateDesc(Long userId);

    // Get top 5 recent transactions (CORRECT)
    List<Transaction> findTop5ByUserIdOrderByDateDesc(Long userId);
}

