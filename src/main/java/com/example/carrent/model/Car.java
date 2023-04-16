package com.example.carrent.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotEmpty;
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
}
