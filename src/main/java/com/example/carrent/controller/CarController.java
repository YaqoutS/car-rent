package com.example.carrent.controller;

import com.example.carrent.model.Car;
import com.example.carrent.model.CarDTO;
import com.example.carrent.service.CarService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
    public List<CarDTO> showAll() {
        return carService.findAll();
    }

    @GetMapping("/cars/{id}")
    public CarDTO showCar(@PathVariable Long id){
        return carService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found!"));
    }

    @PostMapping("/save")
    public CarDTO addCar(@Valid @RequestBody CarDTO carDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Validation error were found while adding new car");
        else {
            return carService.save(new Car(carDTO));
        }

    }

}
