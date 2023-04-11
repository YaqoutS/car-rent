package com.example.carrent.repository;

import com.example.carrent.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
}
