package com.example.projectshop.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "http://localhost:3000")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // สมัครสมาชิก
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Customer customer) {
        if (customerService.findByEmail(customer.getEmail()).isPresent()) {
            return new ResponseEntity<>("อีเมลนี้มีการสมัครสมาชิกแล้ว", HttpStatus.BAD_REQUEST);
        }
        customerService.saveCustomer(customer);
        return new ResponseEntity<>("สมัครสมาชิกสำเร็จ", HttpStatus.CREATED);
    }

    // เข้าสู่ระบบ
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Customer customer) {
        Optional<Customer> found = customerService.findByEmailAndPassword(customer.getEmail(), customer.getPassword());
        if (found.isPresent()) {
            return ResponseEntity.ok("เข้าสู่ระบบสำเร็จ");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("email หรือ passwordของคุณผิดพลาด");
        }
    }

    // ดึงข้อมูลโปรไฟล์
    @GetMapping("/profile")
    public ResponseEntity<Customer> getProfile(@RequestParam String email) {
        Optional<Customer> customer = customerService.findByEmail(email);
        return customer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    //  แก้ไขโปรไฟล์ลูกค้า
    @PostMapping("/update")
    public ResponseEntity<String> updateUserProfile(@RequestParam String name,
                                                    @RequestParam String email,
                                                    @RequestParam String address,
                                                    @RequestParam String phone,
                                                    @RequestParam(required = false) MultipartFile profileImage) {
        boolean updated = customerService.updateUserProfile(name, email, address, phone, profileImage);
        if (!updated) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ไม่สามารถอัพเดตข้อมูลได้");
        }
        return ResponseEntity.ok("โปรไฟล์ได้รับการอัพเดตแล้ว");
    }
}
