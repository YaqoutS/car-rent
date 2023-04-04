package com.example.carrent.model;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Date;

import static java.time.LocalTime.now;

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
    @NonNull
    private String customerName;
    private LocalDate rentEndDate;

    public CarDTO(Car car) {
        id = car.getId();
        name = car.getName();
        model = car.getModel();
        color = car.getColor();
        year = car.getYear();
        customerName = car.getCustomerName();
        if(car.getRentEndDate().isBefore(LocalDate.now())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rent end date can't be an earlier date!");
        }
        else rentEndDate = car.getRentEndDate();
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
