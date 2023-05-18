package com.example.carrent.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.example.carrent.exception.CarAlreadyRentedException;
import com.example.carrent.model.Car;
import com.example.carrent.model.CarDTO;
import com.example.carrent.repository.CarRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    @Mock
    private CarRepository carRepository;
    @InjectMocks
    private CarService carService;

    private Car car1;
    private Car car2;
    private CarDTO carDTO1;
    private CarDTO carDTO2;

    @BeforeEach
    void init() {
        car1 = Car.builder()
                .id(1L)
                .color("black")
                .year(2005)
                .build();

        car2 = Car.builder()
                .id(2L)
                .color("black")
                .year(2011)
                .build();

        carDTO1 = new CarDTO(car1);
        carDTO2 = new CarDTO(car2);

    }

    @Test
    void addCar() {

        when(carRepository.save(any(Car.class))).thenReturn(car1);

        CarDTO newCar = carService.save(car1);

        assertNotNull(newCar);
        assertThat(newCar.getColor()).isEqualTo(carDTO1.getColor());

    }

    @Test
    void getAllCars() {
        List<Car> list = new ArrayList<>();
        list.add(car1);
        list.add(car2);

        when(carRepository.findAll()).thenReturn(list);

        List<CarDTO> carDTOs = carService.findAll();
        assertEquals(2, carDTOs.size());

    }

    @Test
    void findCarById() {
        when(carRepository.findById(anyLong())).thenReturn(Optional.of(car1));

        Optional<Car> newCar = carService.findById(car1.getId());

        assertNotNull(newCar);
    }

    @Test
    void rentCar() {
        //carDTO1.setCustomerName("");
        car1 = new Car(carDTO1);

        when(carRepository.findById(anyLong())).thenReturn(Optional.of(car1));
        Car newCar = new Car(car1);
        newCar.setCustomerName("doaa");
        newCar.setRentEndDate(LocalDate.of(2023, Month.JULY, 22));
        when(carRepository.save(any(Car.class))).thenReturn(newCar);

        CarDTO newCarDTO = carService.rent(carDTO1);

        assertEquals("doaa", newCarDTO.getCustomerName());
        assertEquals(LocalDate.of(2023, Month.JULY, 22), newCarDTO.getRentEndDate());
    }

    @Test @Disabled
    void rentedCarException() {

//        carService.save(car1);
//        Car existingCar = carService.findById(car1.getId()).get();
//        existingCar.setCustomerName("doaa");
//        existingCar.setRentEndDate(LocalDate.of(2023, Month.JULY, 22));
//
//
//        Exception exception = assertThrows(CarAlreadyRentedException.class, () -> {
//            //carService.rent();
//        });
//
//        String expectedMessage = "The car is rented by another customer.";
//        String actualMessage = exception.getMessage();
//
//        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void deleteCar() {
        when(carRepository.findById(anyLong())).thenReturn(Optional.of(car1));
        doNothing().when(carService).delete(any(Car.class));

        carService.delete(car1);

        verify(carRepository, times(1)).delete(car1);

    }

    @AfterEach
    void clean() {
        carService.deleteAll();
    }
}
