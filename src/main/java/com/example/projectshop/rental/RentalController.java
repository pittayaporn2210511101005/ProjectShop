package com.example.projectshop.rental;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    // เพิ่มรายการเช่า
    @PostMapping
    public Rental createRental(@RequestBody Rental rental) {
        return rentalService.createRental(rental);
    }

    // ดึงรายการเช่าทั้งหมด
    @GetMapping
    public List<Rental> getAllRentals() {
        return rentalService.getAllRentals();
    }

    // ดึงรายการเช่าตามสถานะ
    @GetMapping("/status")
    public List<Rental> getByStatus(@RequestParam RentalStatus status) {
        return rentalService.getRentalsByStatus(status);
    }

    // เปลี่ยนสถานะการเช่า (แอดมินใช้)
    @PutMapping("/{id}/status")
    public Rental updateStatus(@PathVariable Long id, @RequestParam RentalStatus status) {
        return rentalService.updateStatus(id, status);
    }
}
