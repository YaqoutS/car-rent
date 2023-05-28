package com.example.carrent.integration;

import com.example.carrent.model.Car;
import com.example.carrent.repository.CarRepository;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarIntegrationTest {
    @LocalServerPort
    private int port;
    private String baseUrl = "http://localhost";
    private static RestTemplate restTemplate;
    @Autowired
    CarRepository carRepository;

    private Car car1;
    private Car car2;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }


}
