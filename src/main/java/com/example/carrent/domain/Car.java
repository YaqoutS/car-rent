package com.example.carrent.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDate;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Car {
    //name, model, color, year, customer_name, rent_end_date
    @Id @GeneratedValue
    private Long id;
    @NonNull @NotEmpty(message = "Please enter a name.")
    private String name;
    private String model;
    private String color;
    private int year;
    private String customerName;
    private String RentEndDate;
}
