package com.example.carrent.model;

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
@AllArgsConstructor
@Builder
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
    @NonNull
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

//    public static Car toEntity(CarDTO carDTO) {
//        return builder()
//                .id(carDTO.getId())
//                .name(carDTO.getName())
//                .color(carDTO.getColor())
//                .year(carDTO.getYear())
//                .customerName(carDTO.getCustomerName())
//                .rentEndDate(carDTO.getRentEndDate())
//                .build();
//    }
}
