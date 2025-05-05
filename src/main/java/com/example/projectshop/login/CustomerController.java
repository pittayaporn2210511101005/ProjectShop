package com.example.projectshop.login;

import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "http://localhost:3000")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService; // เพิ่ม OrderService เพื่อให้สามารถดึงข้อมูลการสั่งซื้อ

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
    public ResponseEntity<Map<String, Object>> login(@RequestBody Customer customer) {
        Optional<Customer> found = customerService.findByEmailAndPassword(customer.getEmail(), customer.getPassword());

        if (found.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("token", "abc123"); // ในอนาคตอาจเปลี่ยนเป็น JWT จริง
            response.put("userId", found.get().getId()); // ดึง userId จริง ๆ จากฐานข้อมูล
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "email หรือ password ของคุณผิดพลาด");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    // ดึงข้อมูลโปรไฟล์
    @GetMapping("/profile")
    public ResponseEntity<Customer> getProfile(@RequestParam String email) {
        Optional<Customer> customer = customerService.findByEmail(email);
        return customer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // แก้ไขโปรไฟล์ลูกค้า
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

    // ดึงข้อมูลลูกค้าตามอีเมล
    @GetMapping("/email/{email}")
    public ResponseEntity<Customer> getCustomerByEmail(@PathVariable String email) {
        Optional<Customer> optionalCustomer = customerService.findByEmail(email);
        if (optionalCustomer.isPresent()) {
            return ResponseEntity.ok(optionalCustomer.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // ดึงข้อมูลประวัติการสั่งซื้อ
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getOrders(@RequestParam String email) {
        // ดึงข้อมูลจากฐานข้อมูลหรือบริการที่เก็บข้อมูลการสั่งซื้อ
        List<Order> orders = orderService.getOrdersByEmail(email);

        if (orders != null && !orders.isEmpty()) {
            return ResponseEntity.ok(orders);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
