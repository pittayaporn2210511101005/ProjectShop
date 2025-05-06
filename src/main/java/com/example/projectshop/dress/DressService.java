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
            updatedDress.setSize(dress.getSize());
            updatedDress.setColor(dress.getColor());
            updatedDress.setStatus(dress.getStatus());
            return dressRepository.save(updatedDress);
        }
        return null; // ถ้าไม่พบข้อมูล
    }

    // ดึงข้อมูลเสื้อผ้าทั้งหมด
    public List<Dress> getAllDresses() {
        return dressRepository.findAll();  // ดึงข้อมูลทั้งหมดจากฐานข้อมูล
    }
}
