package com.example.projectshop.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "http://localhost:3000") // 👈 เพิ่มตรงนี้!
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Customer customer) {
        // ตรวจสอบว่าอีเมลมีในฐานข้อมูลหรือไม่
        if (customerService.findByEmail(customer.getEmail()) != null) {
            return new ResponseEntity<>("อีเมลนี้มีการสมัครสมาชิกแล้ว", HttpStatus.BAD_REQUEST);
        }
        // บันทึกข้อมูลลูกค้าใหม่
        customerService.saveCustomer(customer);
        return new ResponseEntity<>("สมัครสมาชิกสำเร็จ", HttpStatus.CREATED);
    }

    // เข้าสู่ระบบ
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Customer customer) {
        // ค้นหาลูกค้าในฐานข้อมูล
        Optional<Customer> found = customerService.findByEmailAndPassword(customer.getEmail(), customer.getPassword());
        if (found.isPresent()) {
            return ResponseEntity.ok("Login success");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    // เพิ่ม API สำหรับดึงข้อมูลโปรไฟล์ของลูกค้า
    @GetMapping("/api/customer/profile")
    public ResponseEntity<Customer> getProfile(@RequestParam String email) {
        // ค้นหาลูกค้าจากอีเมล
        Optional<Customer> customer = customerService.findByEmail(email);
        if (customer.isPresent()) {
            return ResponseEntity.ok(customer.get()); // ส่งข้อมูลโปรไฟล์ลูกค้ากลับไป
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // หากไม่พบลูกค้า
        }
    }
}