package com.example.carrent.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private String model;
    private String color;
    private int year;
    private String customerName;
    private LocalDate rentEndDate;

    public Car(CarDTO carDTO) {
        id = carDTO.getId();
        name = carDTO.getName();
        model = carDTO.getModel();
        color = carDTO.getColor();
        year = carDTO.getYear();
        customerName = carDTO.getCustomerName();
        rentEndDate = carDTO.getRentEndDate();
    }

    public Car(Car car) {
        id = car.getId();
        name = car.getName();
        model = car.getModel();
        color = car.getColor();
        year = car.getYear();
        customerName = car.getCustomerName();
        rentEndDate = car.getRentEndDate();
    }
}
