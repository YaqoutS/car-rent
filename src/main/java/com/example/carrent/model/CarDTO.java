package com.example.carrent.model;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO {

    @Id
    private Long id;
    @NonNull
    private String name;
    private String model;
    private String color;
    private int year;
    private String customerName;
    private String rentEndDate;

    public CarDTO(Car car) {
        id = car.getId();
        name = car.getName();
        model = car.getModel();
        color = car.getColor();
        year = car.getYear();
        customerName = car.getCustomerName();
        rentEndDate = car.getRentEndDate();
    }

//    public static CarDTO toDto(Car car) {
//        return CarDTO.builder()
//                .id(car.getId())
//                .name(car.getName())
//                .model(car.getModel())
//                .year(car.getYear())
//                .customerName(car.getCustomerName())
//                .rentEndDate(car.getRentEndDate())
//                .build();
//    }
}
