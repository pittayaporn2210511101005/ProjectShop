package com.example.projectshop.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "http://localhost:3001")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // ✅ สมัครสมาชิก
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Customer customer) {
        if (customerService.findByEmail(customer.getEmail()).isPresent()) {
            return new ResponseEntity<>("อีเมลนี้มีการสมัครสมาชิกแล้ว", HttpStatus.BAD_REQUEST);
        }

        // ใช้ method ที่ตั้งค่า role ให้เป็น USER
        customerService.registerNewCustomer(customer);

        return new ResponseEntity<>("สมัครสมาชิกสำเร็จ", HttpStatus.CREATED);
    }

    // ✅ เข้าสู่ระบบ
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        Optional<Customer> found = customerService.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());

        if (found.isPresent()) {
            Customer user = found.get();
            Map<String, Object> response = new HashMap<>();
            response.put("token", "abc123"); // ในระบบจริงควรใช้ JWT
            response.put("userId", user.getId());
            response.put("email", user.getEmail());
            response.put("name", user.getName());
            response.put("role", user.getRole().name());
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "email หรือ password ของคุณผิดพลาด");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    // ✅ ดึงข้อมูลโปรไฟล์
    @GetMapping("/profile")
    public ResponseEntity<Customer> getProfile(@RequestParam String email) {
        Optional<Customer> customer = customerService.findByEmail(email);
        return customer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // ✅ แก้ไขโปรไฟล์ลูกค้า
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

    // ✅ ดึงข้อมูลลูกค้าตามอีเมล (แบบ PathVariable)
    @GetMapping("/email/{email}")
    public ResponseEntity<Customer> getCustomerByEmail(@PathVariable String email) {
        Optional<Customer> optionalCustomer = customerService.findByEmail(email);
        return optionalCustomer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // ✅ class login request สำหรับรับข้อมูลเข้าสู่ระบบ
    static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
