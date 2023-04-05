package com.example.carrent.model;

import com.example.carrent.exception.InValidDateException;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO {

    @Id
    private Long id;
    @NonNull @NotEmpty
    private String name;
    private String model;
    private String color;
    private int year;
    @NonNull @NotEmpty
    private String customerName;
    @NonNull
    private LocalDate rentEndDate;

    public CarDTO(Car car) {
        id = car.getId();
        name = car.getName();
        model = car.getModel();
        color = car.getColor();
        year = car.getYear();
        customerName = car.getCustomerName();
        if(car.getRentEndDate().isBefore(LocalDate.now())){
            throw new InValidDateException("Rent end date can't be an earlier date!");
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
