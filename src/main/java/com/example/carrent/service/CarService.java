package com.example.carrent.service;

import com.example.carrent.exception.CarAlreadyRentedException;
import com.example.carrent.exception.EntityNotFoundException;
import com.example.carrent.model.Car;
import com.example.carrent.model.CarDTO;
import com.example.carrent.repository.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<CarDTO> findAll() {
        return carRepository.findAll()
                .stream()
                .map(car -> new CarDTO(car))
                .collect(Collectors.toList());
    }

    public Optional<Car> findById(Long id) {
        return carRepository.findById(id);
    }

    @Transactional
    public CarDTO save(Car car) {
        return new CarDTO(carRepository.save(car));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE) // or REPEATABLE_READ
    public CarDTO rent(Long id, String customerName, LocalDate rentEndDate) {
        Optional<Car> car = carRepository.findById(id);
        if(car.isEmpty()) {
            throw new EntityNotFoundException("There is no car with id = " + id);
        }
        Car newCar = car.get();
        if(!(newCar.getCustomerName() == null || car.get().getCustomerName().equals(""))) {
            throw new CarAlreadyRentedException("The car is rented by another customer.");
        }
        newCar.setCustomerName(customerName);
        newCar.setRentEndDate(rentEndDate);
        carRepository.updateCar(id, customerName, rentEndDate);
        return new CarDTO(newCar);
    }
}
