package com.example.carrent.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "cars")
public class Car {
    //@Field(type = FieldType.Long, name = "id")
    @Id
    private Long id;
    //@Field(type = FieldType.Text, name = "name")
    private String name;
    //@Field(type = FieldType.Text, name = "model")
    private String model;
    //@Field(type = FieldType.Text, name = "color")
    private String color;
    //@Field(type = FieldType.Integer, name = "year")
    private int year;
    //@Field(type = FieldType.Text, name = "customerName")
    private String customerName;
    //@Field(type = FieldType.Date, name = "rentEndDate")
    private Date rentEndDate;

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
