package com.example.projectshop.login;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    // ✅ Constructor Injection แทน Autowired แบบ field
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // 🔍 ค้นหาลูกค้าตามอีเมล
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    // 🔍 ค้นหาลูกค้าตามอีเมลและรหัสผ่าน
    public Optional<Customer> findByEmailAndPassword(String email, String password) {
        return customerRepository.findByEmailAndPassword(email, password);
    }

    // ✅ สำหรับลงทะเบียนลูกค้าใหม่ พร้อมกำหนด Role USER
    public Customer registerNewCustomer(Customer customer) {
        customer.setRole(Role.USER); // กำหนด Role เริ่มต้น
        return customerRepository.save(customer);
    }

    // ✅ สำหรับอัปเดต Role ของลูกค้า (เช่น Admin ใช้งาน)
    public Optional<Customer> updateCustomerRole(Long customerId, Role newRole) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            customer.setRole(newRole);
            customerRepository.save(customer);
            return Optional.of(customer);
        } else {
            return Optional.empty();
        }
    }

    // ✅ อัปเดตโปรไฟล์ลูกค้า (ล็อกไม่ให้เปลี่ยน Role โดยไม่ตั้งใจ)
    public boolean updateUserProfile(String name, String email, String address, String phone, MultipartFile profileImage) {
        Optional<Customer> customerOptional = customerRepository.findByEmail(email);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();

            Role currentRole = customer.getRole(); // 🔐 เก็บ Role เดิม

            // อัปเดตข้อมูลทั่วไป
            customer.setName(name);
            customer.setAddress(address);
            customer.setPhone(phone);

            if (profileImage != null && !profileImage.isEmpty()) {
                try {
                    String fileName = profileImage.getOriginalFilename();
                    String uploadDir = "uploads/";
                    File uploadPath = new File(uploadDir);
                    if (!uploadPath.exists()) {
                        uploadPath.mkdirs();
                    }

                    String filePath = uploadDir + fileName;
                    File dest = new File(filePath);
                    profileImage.transferTo(dest);

                    customer.setProfileImage(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            customer.setRole(currentRole); // 🔐 ใส่ Role เดิมกลับ
            customerRepository.save(customer);
            return true;
        }
        return false;
    }

}