package com.example.carrent.repository;

import com.example.carrent.model.Car;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    @Modifying @Transactional
    @Query(value = "UPDATE CAR SET CUSTOMER_NAME = :customerName, RENT_END_DATE = :rentEndDate WHERE customer_name = null or customer_name = '' and ID = :id and rent_end_date = null", nativeQuery = true)
    void updateNameAndDate(Long id, String customerName, LocalDate rentEndDate);

}
