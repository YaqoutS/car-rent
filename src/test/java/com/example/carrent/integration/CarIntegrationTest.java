package com.example.carrent.integration;

import com.example.carrent.model.Car;
import com.example.carrent.model.CarDTO;
import com.example.carrent.repository.CarRepository;
import com.example.carrent.repository.ElasticSearchQuery;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarIntegrationTest {
    @LocalServerPort
    private int port;
    private String baseUrl = "http://localhost";
    private static RestTemplate restTemplate;
    private static TestRestTemplate testRestTemplate;
    @Autowired
    CarRepository carRepository;
    @Autowired
    ElasticSearchQuery elasticSearchQuery;

    private Car car1;
    private Car car2;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        restTemplateBuilder.configure(restTemplate);
        TestRestTemplate testRestTemplate = new TestRestTemplate(restTemplateBuilder);
        //testRestTemplate = new TestRestTemplate();
    }

    @BeforeEach
    public void beforeSetup() {
        baseUrl = baseUrl + ":" +port + "/cars";

        car1 = new Car();
        car1.setColor("red");
        car1.setYear(2010);
        car2 = new Car();
        car2.setColor("blue");
        car2.setYear(2012);

        car1 = carRepository.save(car1);
        car2 = carRepository.save(car2);
    }

    @AfterEach
    public void afterSetup() {
        carRepository.deleteAll();
    }

    @Test
    public void shouldSaveCarTest() {
        CarDTO newCar = restTemplate.postForObject(baseUrl + "/save", car1, CarDTO.class);
        assertNotNull(newCar);
        assertThat(newCar.getId()).isNotNull();
    }

    @Test
    public void shouldFetchCarsTest() {
        List<CarDTO> list = restTemplate.getForObject(baseUrl, List.class);
        assertThat(list.size()).isEqualTo(2);
    }

    @Test
    public void shouldFetchCarById() {
        CarDTO existingCar = restTemplate.getForObject(baseUrl + "/" + car1.getId(), CarDTO.class);
        assertNotNull(existingCar);
        assertEquals("red", existingCar.getColor());
    }

    @Test
    public void shouldRentCar() {
        Car newCar = new Car(car1);
        newCar.setCustomerName("Yaqout");
        newCar.setRentEndDate(new Date(2023, 10, 30));

        CarDTO rentedCar = restTemplate.patchForObject(baseUrl + "/rent", newCar, CarDTO.class);

        assertEquals("Yaqout", rentedCar.getCustomerName());
        assertEquals(new Date(2023, 10, 30), rentedCar.getRentEndDate());
    }

    @Test
    void shouldDeleteCarTest() {
        restTemplate.delete(baseUrl + "/" + car1.getId());
        int count = carRepository.findAll().size();
        assertEquals(1, count);
    }
}
