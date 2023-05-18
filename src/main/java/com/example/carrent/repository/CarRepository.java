package com.example.carrent.repository;

import com.example.carrent.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
//    @Modifying
//    @Query(value = "UPDATE Car car SET car.customerName = :customerName, car.rentEndDate = :rentEndDate WHERE car.id = :id")
//    void rentCar(Long id, String customerName, LocalDate rentEndDate);
}
