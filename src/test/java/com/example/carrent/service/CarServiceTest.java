package com.example.carrent.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.carrent.exception.CarAlreadyRentedException;
import com.example.carrent.model.Car;
import com.example.carrent.model.CarDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class CarServiceTest {

    @Autowired
    private CarService carService;

    private CarDTO carDTO1;
    private CarDTO carDTO2;

    @BeforeEach
    void init() {
        carDTO1 = new CarDTO();
        carDTO1.setColor("red");
        carDTO1.setCustomerName("sara");

        carDTO2 = new CarDTO();
        carDTO2.setColor("black");
        carDTO2.setCustomerName("yaqout");
    }

    @Test
    void addCar() {
        CarDTO newCar = carService.save(new Car(carDTO1));

        assertNotNull(newCar);
        assertThat(newCar.getId()).isNotEqualTo(null);
    }

    @Test
    void getAllCars() {
        carService.save(new Car(carDTO1));
        carService.save(new Car(carDTO2));

        List<CarDTO> cars = carService.findAll();

        assertNotNull(cars);
        assertEquals(2, cars.size());
    }

    @Test
    void findCarById() {
        Car car1 = new Car(carDTO1);
        carService.save(car1);

        Car newCar = carService.findById(car1.getId()).get();

        assertNotNull(newCar);
        assertEquals("red", newCar.getColor());
        assertEquals("sara", newCar.getCustomerName());
    }

    @Test
    void updateCar() {
        Car car1 = new Car(carDTO1);
        carService.save(car1);
        Car existingCar = carService.findById(car1.getId()).get();
        existingCar.setColor("blue");
        existingCar.setCustomerName("doaa");
        existingCar.setRentEndDate(LocalDate.of(2023, Month.JULY, 22));

        CarDTO newCar = carService.save(existingCar);

        assertEquals("blue", newCar.getColor());
        assertEquals("doaa", newCar.getCustomerName());
        assertEquals(LocalDate.of(2023, Month.JULY, 22), newCar.getRentEndDate());
    }

    @Test
    void rentCar() {
        carDTO1.setCustomerName("");
        Car car1 = new Car(carDTO1);
        carService.save(car1);
        Car existingCar = carService.findById(car1.getId()).get();
        existingCar.setCustomerName("doaa");
        existingCar.setRentEndDate(LocalDate.of(2023, Month.JULY, 22));

        CarDTO newCar = carService.rent(existingCar.getId(), existingCar.getCustomerName(), existingCar.getRentEndDate());

        assertEquals("doaa", newCar.getCustomerName());
        assertEquals(LocalDate.of(2023, Month.JULY, 22), newCar.getRentEndDate());
    }

    @Test
    void rentedCarException() {
        Car car1 = new Car(carDTO1);
        carService.save(car1);
        Car existingCar = carService.findById(car1.getId()).get();
        existingCar.setCustomerName("doaa");
        existingCar.setRentEndDate(LocalDate.of(2023, Month.JULY, 22));

        Exception exception = assertThrows(CarAlreadyRentedException.class, () -> {
            carService.rent(existingCar.getId(), existingCar.getName(), existingCar.getRentEndDate());
        });

        String expectedMessage = "The car is rented by another customer.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("should delete the existing car")
    void deleteCar() {
        Car car1 = new Car(carDTO1);
        Car car2 = new Car(carDTO2);
        carService.save(car1);
        Long id = car1.getId();
        carService.save(car2);

        carService.delete(car1);
        Optional<Car> existingCar = carService.findById(id);
        List<CarDTO> cars = carService.findAll();

        assertThat(existingCar).isEmpty();
        assertEquals(1, cars.size());
    }

    @AfterEach
    void clean() {
        carService.deleteAll();
    }
}
