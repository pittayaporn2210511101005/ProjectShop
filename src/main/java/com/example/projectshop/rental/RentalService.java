package com.example.projectshop.rental;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RentalService {
    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    //ใช้สร้างข้อมูลการเข่า รออนุมัต
    public Rental createRental(Rental rental) {
        rental.setStatus(RentalStatus.WAITING); // ค่าเริ่มต้น
        return rentalRepository.save(rental);
    }

    //ดึงข้อมูลการเช่าจากทั้งหมด
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    //ค้นหาข้อมูลการเช่าจาก id
    public Optional<Rental> getRentalById(Long id) {
        return rentalRepository.findById(id);
    }

    //ค้นหาข้อมูลการเช่าตามid แล้วส่งสถานกับมา
    public Rental updateStatus(Long id, RentalStatus status) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental not found"));
        rental.setStatus(status);
        return rentalRepository.save(rental);
    }

    //ดึงข้อมูลเฉพาะที่มีรายการเช่า พวก กำลังเช่า คืนแล้ว
    public List<Rental> getRentalsByStatus(RentalStatus status) {
        return rentalRepository.findByStatus(status);
    }
}
