package com.example.carrent.controller;

import com.example.carrent.exception.EntityNotFoundException;
import com.example.carrent.exception.InValidDateException;
import com.example.carrent.exception.RequiredFieldException;
import com.example.carrent.model.Car;
import com.example.carrent.model.CarDTO;
import com.example.carrent.service.CarService;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public CarDTO addCar(@Valid @RequestBody CarDTO carDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            String field = bindingResult.getFieldError().getField();
            throw new RequiredFieldException("Validation error ! '" + field + "' is required.");
        }
        if((carDTO.getCustomerName() == null || carDTO.getCustomerName().isEmpty()) && carDTO.getRentEndDate() != null) {
            throw new RequiredFieldException("Customer Name is required !");
        }
        if(carDTO.getCustomerName() != null) {
            if(!carDTO.getCustomerName().isEmpty() && carDTO.getRentEndDate() == null)
                throw new RequiredFieldException("Rent End Date is required !");
        }
        if(carDTO.getRentEndDate() != null && carDTO.getRentEndDate().isBefore(LocalDate.now())) {
            throw new InValidDateException("Rent end date can't be an earlier date!");
        }
        return carService.save(new Car(carDTO));
    }

    @PatchMapping("/cars/{id}/rent")
    public CarDTO rent(@PathVariable Long id, @RequestParam String customerName, @RequestParam LocalDate rentEndDate) {
        if(customerName == null || customerName.equals("")) {
            throw new RequiredFieldException("Customer Name is required !");
        }
        if(rentEndDate == null) {
            throw new RequiredFieldException("Rent End Date is required !");
        }
        if(rentEndDate.isBefore(LocalDate.now())) {
            throw new InValidDateException("Rent end date can't be an earlier date!");
        }
        return carService.rent(id, customerName, rentEndDate);
    }
}
