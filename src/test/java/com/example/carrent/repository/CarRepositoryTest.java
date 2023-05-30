package com.example.carrent.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.carrent.model.Car;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    private Car car1;
    private Car car2;

    @BeforeEach
    void init() {
        car1 = new Car();
        car1.setColor("red");
        car1.setCustomerName("sara");
        car1.setRentEndDate(new Date(2023, 7, 22));

        car2 = new Car();
        car2.setColor("black");
        car2.setCustomerName("yaqout");
        car1.setRentEndDate(new Date(2023, 7, 18));
    }

    @Test
    void addCar() {
        Car newCar = carRepository.save(car1);

        assertNotNull(newCar);
        assertThat(newCar.getId()).isNotEqualTo(null);
    }

    @Test
    void getAllCars() {
        carRepository.save(car1);
        carRepository.save(car2);

        List<Car> cars = carRepository.findAll();

        //assertNotNull(cars);
        assertEquals(2, cars.size());
    }

    @Test
    void findCarById() {
        carRepository.save(car1);

        Car newCar = carRepository.findById(car1.getId()).get();

        assertNotNull(newCar);
        assertEquals("red", newCar.getColor());
        assertEquals("sara", newCar.getCustomerName());
    }

    @Test
    void updateCar() {
        carRepository.save(car1);
        Car existingCar = carRepository.findById(car1.getId()).get();
        existingCar.setColor("blue");
        existingCar.setCustomerName("doaa");

        Car newCar = carRepository.save(existingCar);

        assertEquals("blue", newCar.getColor());
        assertEquals("doaa", newCar.getCustomerName());
    }

    @Test
    void deleteCar() {
        carRepository.save(car1);
        Long id = car1.getId();
        carRepository.save(car2);

        carRepository.delete(car1);
        Optional<Car> existingCar = carRepository.findById(id);
        List<Car> cars = carRepository.findAll();

        assertThat(existingCar).isEmpty();
        assertEquals(1, cars.size());
    }

    @AfterEach
    void clean() {
        carRepository.deleteAll();
    }
}