package com.example.carrent.controller;

import com.example.carrent.domain.Car;
import com.example.carrent.service.CarService;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/showAll")
    public List<Car> showAll() {
        return carService.findAll();
    }

    @GetMapping("/view/{id}")
    public String showCar(@PathVariable Long id){
        Optional<Car> car = carService.findById(id);
        if(car.isPresent()) return car.toString();
        else return "There is no car with id = " + id;
    }

    @PostMapping("/save")
    public String addCar(@Valid Car car, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "Validation error were found while adding new car.";
        else {
            carService.save(car);
            return "New car was saved successfully";
        }

    }

}
