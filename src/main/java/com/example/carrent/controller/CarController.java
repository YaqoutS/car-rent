package com.example.carrent.controller;

import com.example.carrent.exception.EntityNotFoundException;
import com.example.carrent.model.Car;
import com.example.carrent.model.CarDTO;
import com.example.carrent.service.CarService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/cars")
    public List<CarDTO> showAll() {
        return carService.findAll();
    }

    @GetMapping("/cars/{id}")
    public CarDTO showCar(@PathVariable Long id){
        Optional<Car> optionalCar = carService.findById(id);
        if(optionalCar.isPresent()){
            Car car = optionalCar.get();
            return new CarDTO(car);
        }
        else throw new EntityNotFoundException("There is no car with id = " + id);
    }

    @PostMapping("/save")
    public CarDTO addCar(@RequestBody CarDTO carDTO) {
        return carService.save(new Car(carDTO));
    }

    @PatchMapping("/cars/rent")
    public CarDTO rent(@Valid @RequestBody CarDTO carDTO) {
        return carService.rent(carDTO.getId(), carDTO.getCustomerName(), carDTO.getRentEndDate());
    }

    @GetMapping("/test/{id}")
    public CarDTO update(@PathVariable Long id) throws InterruptedException {
        //carService.writeWithoutWait(id);
        carService.writeWithWait(id, 10000, 1);
        carService.writeWithWait(id, 0, 2);
        Optional<Car> optionalCar = carService.findById(id);
        if(optionalCar.isPresent()){
            Car car = optionalCar.get();
            return new CarDTO(car);
        }
        else throw new EntityNotFoundException("There is no car with id = " + id);
    }

}
