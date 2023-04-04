package com.example.carrent.service;

import com.example.carrent.model.Car;
import com.example.carrent.model.CarDTO;
import com.example.carrent.repository.CarRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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

    public Optional<CarDTO> findById(Long id) {
        Optional<Car> car = carRepository.findById(id);
        return Optional.ofNullable(new CarDTO(car.get()));

    }

    @Transactional //To roll back adding the car if an exception thrown
    public CarDTO save(Car car) {
        return new CarDTO(carRepository.save(car));
    }

}
