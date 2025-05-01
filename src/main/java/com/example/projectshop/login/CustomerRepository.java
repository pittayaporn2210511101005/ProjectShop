package com.example.projectshop.login;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // ค้นหาลูกค้าตามอีเมล
    Optional<Customer> findByEmail(String email);

    // ค้นหาลูกค้าตามอีเมลและรหัสผ่าน
    Optional<Customer> findByEmailAndPassword(String email, String password);
}
