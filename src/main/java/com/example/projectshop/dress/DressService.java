package com.example.projectshop.dress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DressService {

    @Autowired
    private DressRepository dressRepository;

    // บันทึกเสื้อผ้า
    public Dress saveDress(Dress dress) {
        return dressRepository.save(dress);
    }

    // อัปเดตเสื้อผ้า
    public Dress updateDress(String id, Dress dress) {
        Optional<Dress> existingDress = dressRepository.findById(Long.valueOf(id));
        if (existingDress.isPresent()) {
            Dress updatedDress = existingDress.get();
            updatedDress.setName(dress.getName());
            updatedDress.setBrand(dress.getBrand());
            updatedDress.setPrice(dress.getPrice());
            updatedDress.setSize(dress.getSize()); // เปลี่ยนเป็น setSize แทน setSizes
            updatedDress.setColor(dress.getColor()); // เปลี่ยนเป็น setColor แทน setColors
            updatedDress.setStatus(dress.getStatus());
            return dressRepository.save(updatedDress);
        }
        return null; // ถ้าไม่พบชุดที่ต้องการอัปเดต
    }

    // ลบเสื้อผ้า
    public boolean deleteDress(String id) {
        try {
            Long idLong = Long.parseLong(id);  // แปลง id จาก String เป็น Long
            Dress dress = dressRepository.findById(idLong).orElse(null);
            if (dress != null) {
                dressRepository.delete(dress);  // ลบชุด
                return true;
            }
            return false;
        } catch (Exception e) {
            return false; // ถ้าเกิดข้อผิดพลาด
        }
    }


    // ดึงข้อมูลเสื้อผ้าทั้งหมด
    public List<Dress> getAllDresses() {
        return dressRepository.findAll();
    }
}
