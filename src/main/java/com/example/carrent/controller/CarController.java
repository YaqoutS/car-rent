package com.example.carrent.controller;

import com.example.carrent.domain.Car;
import com.example.carrent.service.CarService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/")
    public String home() {
        return "Hello, World";
    }
    @GetMapping("/cars")
    public List<Car> showAll() {
        return carService.findAll();
    }

    @GetMapping("/cars/{id}")
    public Car showCar(@PathVariable Long id){
//        Optional<Car> car = carService.findById(id);
//        if(car.isPresent()) return car.toString();
//        else return "There is no car with id = " + id;

        //or we can say
        return carService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found!"));

    }

    @PostMapping("/save")
    public String addCar(@Valid @RequestBody Car car, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "Validation error were found while adding new car.";
        else {
            carService.save(car);
            return "New car was added successfully";
        }

    }

}
