package com.example.projectshop.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

    // อัพเดตข้อมูลโปรไฟล์
    public boolean updateUserProfile(String name, String email, String address, String phone, MultipartFile profileImage) {
        Optional<Customer> customerOptional = customerRepository.findByEmail(email);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();

            // อัปเดตข้อมูลทั่วไป
            customer.setName(name);
            customer.setAddress(address);
            customer.setPhone(phone);

            if (profileImage != null && !profileImage.isEmpty()) {
                try {
                    String fileName = profileImage.getOriginalFilename();
                    String uploadDir = "uploads/"; // 📂 สร้างโฟลเดอร์นี้ใน root ของโปรเจกต์
                    File uploadPath = new File(uploadDir);
                    if (!uploadPath.exists()) {
                        uploadPath.mkdirs();
                    }

                    String filePath = uploadDir + fileName;
                    File dest = new File(filePath);
                    profileImage.transferTo(dest);

                    customer.setProfileImage(filePath); // เก็บ path ไว้ใน DB
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            customerRepository.save(customer);
            return true;
        }
        return false;
    }
}
