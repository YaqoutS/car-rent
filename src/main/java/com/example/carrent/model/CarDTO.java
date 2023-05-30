package com.example.carrent.model;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarDTO {

    @Id
    private Long id;
    private String name;
    private String model;
    private String color;
    private int year;
    @NotEmpty
    private String customerName;
    @NotNull @Future
    private Date rentEndDate;

    public CarDTO(Car car) {
        id = car.getId();
        name = car.getName();
        model = car.getModel();
        color = car.getColor();
        year = car.getYear();
        customerName = car.getCustomerName();
        rentEndDate = car.getRentEndDate();
    }

    public CarDTO(CarDTO car) {
        id = car.getId();
        name = car.getName();
        model = car.getModel();
        color = car.getColor();
        year = car.getYear();
        customerName = car.getCustomerName();
        rentEndDate = car.getRentEndDate();
    }
}
