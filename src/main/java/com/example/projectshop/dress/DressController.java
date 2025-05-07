package com.example.projectshop.dress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/dress")
@CrossOrigin(origins = "http://localhost:3000")
public class DressController {

    @Autowired
    private DressService dressService;

    // Endpoint สำหรับบันทึกเสื้อผ้า
    @PostMapping("/save")
    public Dress addDress(@RequestBody Dress dress) {

        // ถ้าอยากสร้าง imageUrl แบบ mock (เช่น "/images/{id}")
        if (dress.getImageUrl() == null || dress.getImageUrl().isEmpty()) {
            dress.setImageUrl("/images/" + UUID.randomUUID()); // หรือสร้างจากชื่อ
        }

        return dressService.saveDress(dress);
    }

    // Endpoint สำหรับอัปเดตข้อมูลเสื้อผ้า
    @PutMapping("/{id}")
    public Dress updateDress(@PathVariable("id") String id, @RequestBody Dress dress) {
        return dressService.updateDress(id, dress);
    }

    // Endpoint สำหรับลบเสื้อผ้า
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDress(@PathVariable String id) {
        boolean isDeleted = dressService.deleteDress(id);
        if (isDeleted) {
            return ResponseEntity.ok("ชุดถูกลบเรียบร้อยแล้ว");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ไม่พบชุดที่ต้องการลบ");
        }
    }

    // Endpoint สำหรับดึงข้อมูลเสื้อผ้าทั้งหมด
    @GetMapping("")
    public List<Dress> getAllDresses() {
        return dressService.getAllDresses();
    }
}
