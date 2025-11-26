package com.example.config;

import com.example.model.User;
import com.example.model.Transaction;
import com.example.repository.UserRepository;
import com.example.repository.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public DataLoader(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // ===== Admin =====
        Optional<User> adminOpt = userRepository.findByUsername("admin");
        User admin;
        if (adminOpt.isPresent()) {
            admin = adminOpt.get();
            admin.setPassword("Admin@123");
            admin.setEmail("admin@gmail.com");
        } else {
            admin = new User();
            admin.setUsername("admin");
            admin.setPassword("Admin@123");
            admin.setEmail("admin@gmail.com");
            admin.setFirstName("John");
            admin.setLastName("Doe");
            admin.setPhoneNumber("1234567890");
            admin.setRole("ADMIN");
            admin = userRepository.save(admin);
        }

        // Create default admin transaction if none exist
        if (transactionRepository.findTop5ByUserIdOrderByDateDesc(admin.getId()).isEmpty()) {
            Transaction tx = new Transaction();
            tx.setUser(admin);
            tx.setType("Credit");
            tx.setAmount(0.0);
            transactionRepository.save(tx);
        }

        // ===== User =====
        Optional<User> userOpt = userRepository.findByUsername("user1");
        User user;
        if (userOpt.isPresent()) {
            user = userOpt.get();
            user.setPassword("User@123");
            user.setEmail("user1@gmail.com");
        } else {
            user = new User();
            user.setUsername("user1");
            user.setPassword("User@123");
            user.setEmail("user1@gmail.com");
            user.setFirstName("Jane");
            user.setLastName("Smith");
            user.setPhoneNumber("9876543210");
            user.setRole("USER");
            user = userRepository.save(user);
        }

        // Add multiple sample transactions if none exist
        if (transactionRepository.findTop5ByUserIdOrderByDateDesc(user.getId()).isEmpty()) {

            double[] amounts = {1000, -200, 500, -100, 700, -50};
            String[] types = {"Credit", "Debit", "Credit", "Debit", "Credit", "Debit"};

            for (int i = 0; i < amounts.length; i++) {
                Transaction tx = new Transaction();
                tx.setUser(user);
                tx.setAmount(Math.abs(amounts[i]));
                tx.setType(types[i]);
                transactionRepository.save(tx);
            }
        }
    }
}
