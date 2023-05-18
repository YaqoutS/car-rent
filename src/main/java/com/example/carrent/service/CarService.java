package com.example.carrent.service;

import com.example.carrent.exception.CarAlreadyRentedException;
import com.example.carrent.exception.EntityNotFoundException;
import com.example.carrent.exception.InternalServerException;
import com.example.carrent.model.Car;
import com.example.carrent.model.CarDTO;
import com.example.carrent.repository.CarRepository;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.CannotAcquireLockException;

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

//    public Car findByyId(Long id) {
//        return carRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("There is no car with id = " + id));
//    }

    @Transactional
    public CarDTO save(Car car) {
        return new CarDTO(carRepository.save(car));
    }

    public void delete(Car car) {
        carRepository.delete(car);
    }

    public void deleteAll() {
        carRepository.deleteAll();
    }

    @Retryable(value = {CannotAcquireLockException.class})
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CarDTO rent(CarDTO carDTO) {
        Optional<Car> car = carRepository.findById(carDTO.getId());
        if(!car.isPresent()) {
            throw new EntityNotFoundException("There is no car with id = " + carDTO.getId());
        }
        Car newCar = car.get();
        if(!(newCar.getCustomerName() == null || car.get().getCustomerName().equals(""))) {
            throw new CarAlreadyRentedException("The car is rented by another customer.");
        }
        newCar.setCustomerName(carDTO.getCustomerName());
        newCar.setRentEndDate(carDTO.getRentEndDate());
        carRepository.save(newCar); //even if this deleted, the database is updated!!!!!
        return new CarDTO(newCar);
    }
    @Recover
    public CarDTO recover(CarAlreadyRentedException exception) {
        throw new CarAlreadyRentedException("The car is rented by another customer.");
    }
    @Recover
    public CarDTO recover(CannotAcquireLockException e) {
        // handle the exception after all retries have failed
        throw new InternalServerException("Sorry, an internal error happened.");
    }
}
