package com.example.carrent.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.carrent.model.Car;
import com.example.carrent.model.Notification;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class ElasticSearchQuery {

    private final ElasticsearchClient elasticsearchClient;

//    private final String indexName = "cars";

    public ElasticSearchQuery(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    public String createOrUpdateCarDocument(Car car) throws IOException {
        IndexResponse response = elasticsearchClient.index(i -> i
                .index("cars")
                .id(car.getId() + "")
                .document(car)
        );
        if(response.result().name().equals("Created")){
            return "Document has been successfully created.";
        }else if(response.result().name().equals("Updated")){
            return "Document has been successfully updated.";
        }
        return "Error while performing the operation.";
    }

    public Car getCarDocumentById(String carId) throws IOException{
        Car car = null;
        GetResponse<Car> response = elasticsearchClient.get(g -> g
                        .index("cars")
                        .id(carId),
                Car.class
        );
        if (response.found()) {
            car = response.source();
            System.out.println("Car name " + car.getName());
        } else {
            System.out.println ("Car not found");
        }
        return car;
    }

    public String deleteCarDocumentById(String carId) throws IOException {
        DeleteRequest request = DeleteRequest.of(d -> d.index("cars").id(carId));
        DeleteResponse deleteResponse = elasticsearchClient.delete(request);
        if (Objects.nonNull(deleteResponse.result()) && !deleteResponse.result().name().equals("NotFound")) {
            return "Car with id " + deleteResponse.id() + " has been deleted.";
        }
        System.out.println("Car not found");
        return "Car with id " + deleteResponse.id() + " does not exist.";
    }

    public  List<Car> searchAllCarDocuments() throws IOException {
        SearchRequest searchRequest =  SearchRequest.of(s -> s.index("cars"));
        SearchResponse searchResponse =  elasticsearchClient.search(searchRequest, Car.class);
        List<Hit> hits = searchResponse.hits().hits();
        List<Car> cars = new ArrayList<>();
        for(Hit object : hits){
            cars.add((Car) object.source());
        }
        return cars;
    }

    public String rentCar(Car car) throws IOException {
        Car finalCar = car;
        GetResponse<Car> response = elasticsearchClient.get(g -> g
                        .index("cars")
                        .id(finalCar.getId() + ""),
                Car.class
        );
        if (!response.found()) {
            return ("There is no car with id = " + finalCar.getId());
        }
        Car responseCar = response.source();
        //System.out.println(responseCar);
        if(!Objects.equals(responseCar.getCustomerName(), "") && responseCar.getCustomerName() != null) {
            return ("The car is rented by another customer");
        }
        else {
            return createOrUpdateCarDocument(finalCar);
        }
    }

    public  List<Notification> getAllNotifications() throws IOException {
        SearchRequest searchRequest =  SearchRequest.of(s -> s.index("new-notifications"));
        SearchResponse searchResponse =  elasticsearchClient.search(searchRequest, Notification.class);
        System.out.println("Response: " + searchResponse);
        List<Hit> hits = searchResponse.hits().hits();
        List<Notification> notifications = new ArrayList<>();
        Notification notification;
        String msg;
        for(Hit<Notification> object : hits){
            System.out.println("Object: " + object);
            notification = (Notification) object.source();
            notification.setId(object.id());
            msg = notification.getMessage();
            notification.setMessage(msg.substring(msg.indexOf("Notification")));
            notifications.add(notification);
        }
        return notifications;
    }

    public String deleteNotificationById(String id) throws IOException {
        DeleteRequest deleteRequest = DeleteRequest.of(d -> d.index("new-notifications").id(id));
        DeleteResponse deleteResponse = elasticsearchClient.delete(deleteRequest);
        if (Objects.nonNull(deleteResponse.result()) && !deleteResponse.result().name().equals("NotFound")) {
            return "Notification deleted";
        }
        return "Error: notification can't be found";
    }
}
