package com.example.carrent.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.carrent.model.Car;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class ElasticSearchQuery {

    private final ElasticsearchClient elasticsearchClient;

    private final String indexName = "cars";

    public ElasticSearchQuery(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    public String createOrUpdateDocument(Car car) throws IOException {
        IndexResponse response = elasticsearchClient.index(i -> i
                .index(indexName)
                .id(String.valueOf(car.getId()))
                .document(car)
        );
        if(response.result().name().equals("Created")){
            return "Document has been successfully created.";
        }else if(response.result().name().equals("Updated")){
            return "Document has been successfully updated.";
        }
        return "Error while performing the operation.";
    }

    public Car getDocumentById(String carId) throws IOException{
        Car car = null;
        GetResponse<Car> response = elasticsearchClient.get(g -> g
                        .index(indexName)
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

    public String deleteDocumentById(String carId) throws IOException {

        DeleteRequest request = DeleteRequest.of(d -> d.index(indexName).id(carId));

        DeleteResponse deleteResponse = elasticsearchClient.delete(request);
        if (Objects.nonNull(deleteResponse.result()) && !deleteResponse.result().name().equals("NotFound")) {
            return "Car with id " + deleteResponse.id() + " has been deleted.";
        }
        System.out.println("Car not found");
        return "Car with id " + deleteResponse.id() + " does not exist.";
    }

    public  List<Car> searchAllDocuments() throws IOException {
        SearchRequest searchRequest =  SearchRequest.of(s -> s.index(indexName));
        SearchResponse searchResponse =  elasticsearchClient.search(searchRequest, Car.class);
        List<Hit> hits = searchResponse.hits().hits();
        List<Car> cars = new ArrayList<>();
        for(Hit object : hits){
            System.out.print(((Car) object.source()));
            cars.add((Car) object.source());
        }
        return cars;
    }
}
