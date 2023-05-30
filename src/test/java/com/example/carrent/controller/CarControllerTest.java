package com.example.carrent.controller;

import com.example.carrent.model.Car;
import com.example.carrent.model.CarDTO;
import com.example.carrent.service.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class CarControllerTest {

    @MockBean
    private CarService carService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Car car1;
    private Car car2;
    private CarDTO carDTO1;
    private CarDTO carDTO2;

    @BeforeEach
    void init() {
        car1 = Car.builder()
                .id(1L)
                .color("black")
                .year(2005)
                .build();

        car2 = Car.builder()
                .id(2L)
                .color("black")
                .year(2011)
                .build();

        carDTO1 = new CarDTO(car1);
        carDTO2 = new CarDTO(car2);
    }

    @Test
    void createCar() throws Exception {
        when(carService.save(any(Car.class))).thenReturn(carDTO1);

        mockMvc.perform(post("/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carDTO1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(carDTO1.getId().intValue())))
                .andExpect(jsonPath("$.color", is(carDTO1.getColor())))
                .andExpect(jsonPath("$.year", is(carDTO1.getYear())));
    }

    @Test
    void getAllCars() throws Exception {
        List<CarDTO> cars = new ArrayList();
        cars.add(carDTO1);
        cars.add(carDTO2);

        when(carService.findAll()).thenReturn(cars);

        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(cars.size())));
    }

    @Test
    void getCarById() throws Exception {
        when(carService.findById(anyLong())).thenReturn(Optional.of(car1));

        mockMvc.perform(get("/cars/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color", is(car1.getColor())))
                .andExpect(jsonPath("$.year", is(car1.getYear())));
    }

    @Test
    void rentCar() throws Exception {
        carDTO1.setCustomerName("yaqout");
        carDTO1.setRentEndDate(new Date(2023, 7, 12));
        System.out.println(carDTO1);
        when(carService.rent(any(CarDTO.class))).thenReturn(carDTO1);

        mockMvc.perform(patch("/cars/rent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carDTO1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName", is(carDTO1.getCustomerName())))
                .andExpect(jsonPath("$.rentEndDate", is(carDTO1.getRentEndDate().toString())));
    }

    @Test
    void deleteCar() throws Exception{
        doNothing().when(carService).deleteById(any(Long.class));

        mockMvc.perform(delete("/cars/{id}"))
                .andExpect(status().isNoContent());
    }
}
