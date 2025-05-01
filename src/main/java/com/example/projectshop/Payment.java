package com.example.projectshop;

import jakarta.persistence.*;

import javax.xml.crypto.Data;
import java.util.Date;

@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rental_id")
    private Rental rental;

    @Temporal(TemporalType.TIMESTAMP) //เก็บทั้งเวลาและวันที่
    private Date paydate;
    private Long amount;
    private String status;
}
