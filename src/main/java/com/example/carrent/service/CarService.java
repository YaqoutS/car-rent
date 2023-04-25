package com.example.carrent.service;

import com.example.carrent.exception.CarAlreadyRentedException;
import com.example.carrent.exception.EntityNotFoundException;
import com.example.carrent.exception.InternalServerException;
import com.example.carrent.model.Car;
import com.example.carrent.model.CarDTO;
import com.example.carrent.repository.CarRepository;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
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

    @Transactional
    public CarDTO save(Car car) {
        return new CarDTO(carRepository.save(car));
    }

    @Retryable(value = {CannotAcquireLockException.class})
    @Transactional(isolation = Isolation.SERIALIZABLE)
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
        carRepository.updateCar(id, customerName, rentEndDate); //even if this deleted, the database is updated!!!!!
        return new CarDTO(newCar);
    }
    @Recover
    public CarDTO recover(CannotAcquireLockException e) {
        // handle the exception after all retries have failed
        throw new InternalServerException("Sorry, an internal error happened.");
    }

    @Retryable(value = {CannotAcquireLockException.class})
    @Async @Transactional(isolation = Isolation.SERIALIZABLE)
    public void writeWithWait(Long id, int sleepTime, int callNumber) throws InterruptedException {
        System.out.println(callNumber + " Enter the method.. ");
        Optional<Car> optionalCar = carRepository.findById(id);
        System.out.println(callNumber + " read.. " + optionalCar.get().getCustomerName());
        if(optionalCar.isPresent() && optionalCar.get().getCustomerName().isEmpty()) {
            Car car = optionalCar.get();
            System.out.println(callNumber + " read again before wait.. " + carRepository.findById(id).get().getCustomerName());
            System.out.println(callNumber + " method will start wait");
            Thread.sleep(sleepTime);
            System.out.println(callNumber + " method finished waiting");
            System.out.println(callNumber + " read again after wait.. " + carRepository.findById(id).get().getCustomerName());
            if(callNumber == 1) car.setCustomerName("Yaqout");
            else car.setCustomerName("Sara");
            carRepository.save(car);
            System.out.println(callNumber + " write to the database");
        }
        else System.out.println(callNumber + " car is already rented");
    }

    @Recover
    public void recoverTest(CannotAcquireLockException e) {
        // handle the exception after all retries have failed
        System.out.println("Sorry, you can't rent the car");
    }


}
