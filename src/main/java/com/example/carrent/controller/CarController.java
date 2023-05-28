package com.example.carrent.controller;

import com.example.carrent.exception.EntityNotFoundException;
import com.example.carrent.model.Car;
import com.example.carrent.model.CarDTO;
import com.example.carrent.repository.ElasticSearchQuery;
import com.example.carrent.service.CarService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.imageio.IIOException;
import javax.imageio.ImageWriteParam;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class CarController {

    private final CarService carService;
    private final ElasticSearchQuery elasticSearchQuery;

    public CarController(CarService carService, ElasticSearchQuery elasticSearchQuery) {
        this.carService = carService;
        this.elasticSearchQuery = elasticSearchQuery;
    }

    @GetMapping("/cars")
    public List<CarDTO> showAll() throws IOException {
        List<Car> cars = elasticSearchQuery.searchAllDocuments();
        System.out.println(cars);
        return carService.findAll();
    }

    @GetMapping("/cars/{id}")
    public CarDTO showCar(@PathVariable Long id) throws IOException {
        Car carE = elasticSearchQuery.getDocumentById(id + "");
        System.out.println(id + "");
        System.out.println(carE);

        Optional<Car> optionalCar = carService.findById(id);
        if(optionalCar.isPresent()){
            Car car = optionalCar.get();
            return new CarDTO(car);
        }
        else throw new EntityNotFoundException("There is no car with id = " + id);
    }

    @PostMapping("/save")
    public CarDTO addCar(@RequestBody CarDTO carDTO) throws IOException{
        String response = elasticSearchQuery.createOrUpdateDocument(new Car(carDTO));
        System.out.println(response);
        return carService.save(new Car(carDTO));
    }

    @PatchMapping("/cars/rent")
    public CarDTO rent(@Valid @RequestBody CarDTO carDTO) {
        return carService.rent(carDTO);
    }

    @DeleteMapping("/cars/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(CarDTO carDTO) throws IOException {
        String response = elasticSearchQuery.deleteDocumentById(carDTO.getId() + "");
        System.out.println(response);

        Car car = new Car(carDTO);
        System.out.println(car);
        carService.delete(car);
    }
}
