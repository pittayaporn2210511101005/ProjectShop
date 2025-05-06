package com.example.projectshop.dress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DressRepository extends JpaRepository<Dress, Long>{


}
