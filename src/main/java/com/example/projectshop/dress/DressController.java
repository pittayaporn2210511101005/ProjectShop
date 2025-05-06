package com.example.projectshop.dress;

import com.example.projectshop.login.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/Dress")
@CrossOrigin(origins = "http://localhost:3000")
public class DressController {

    @Autowired
    private DressService dressService;

    // Endpoint สำหรับบันทึกเสื้อผ้า
    @PostMapping("/save")
    public Dress addDress(@RequestBody Dress dress) {
        return dressService.saveDress(dress);
    }

    // Endpoint สำหรับอัปเดตข้อมูลเสื้อผ้า
    @PutMapping("/update/{id}")
    public Dress updateDress(@PathVariable("id") String id, @RequestBody Dress dress) {
        return dressService.updateDress(id, dress); // เรียกใช้ service เพื่ออัปเดตข้อมูล
    }

    // Endpoint สำหรับดึงข้อมูลเสื้อผ้าทั้งหมด
    @GetMapping("/dress")
    public List<Dress> getAllDresses() {
        return dressService.getAllDresses();
    }
}



