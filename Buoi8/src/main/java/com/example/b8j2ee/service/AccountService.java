package com.example.b8j2ee.service;

import com.example.b8j2ee.entity.Account;
import com.example.b8j2ee.entity.Role;
import com.example.b8j2ee.repository.AccountRepository;
import com.example.b8j2ee.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    public Account register(String username, String password, String fullName, String email, String roleName) {
        if (existsByUsername(username)) {
            throw new RuntimeException("Tên tài khoản đã tồn tại: " + username);
        }
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy role: " + roleName));
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(password));
        account.setFullName(fullName);
        account.setEmail(email);
        account.setEnabled(true);
        account.setRole(role);
        return accountRepository.save(account);
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }
}
