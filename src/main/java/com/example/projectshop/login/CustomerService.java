package com.example.projectshop.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.projectshop.login.Customer;  // อัปเดตให้ถูกต้อง
import com.example.projectshop.login.CustomerRepository;  // อัปเดตให้ถูกต้อง

import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    // ค้นหาลูกค้าตามอีเมลและคืนค่า Optional
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    // ค้นหาลูกค้าตามอีเมลและรหัสผ่าน
    public Optional<Customer> findByEmailAndPassword(String email, String password) {
        return customerRepository.findByEmailAndPassword(email, password);
    }

    // บันทึกข้อมูลลูกค้าใหม่
    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }
}
