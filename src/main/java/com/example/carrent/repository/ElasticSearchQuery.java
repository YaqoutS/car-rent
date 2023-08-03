package com.example.carrent.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.carrent.model.Car;
import com.example.carrent.model.Notification;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.util.EntityUtils;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;

import org.apache.http.HttpHost;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import java.io.IOException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ElasticSearchQuery {

    private final ElasticsearchClient elasticsearchClient;
    private final ElasticsearchOperations elasticsearchOperations;

//    private final String indexName = "cars";

    public ElasticSearchQuery(ElasticsearchClient elasticsearchClient, ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchClient = elasticsearchClient;
        this.elasticsearchOperations = elasticsearchOperations;
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

    public List<Car> search(String keywords) {
//        Float nonExistingBoost = null; // even though it exists in SpringBoot, ElasticSearch has no boost for this type of query
//        // when you analyze what matchQuery returns, it also has nothing related to boost
//        Query query = QueryBuilders.matchQuery("name_suggest", keywords, Operator.Or, nonExistingBoost)._toQuery();
//        NativeQuery nativeQuery = NativeQuery.builder().withQuery(query).build();
//        SearchHits<Car> result = this.elasticsearchOperations.search(nativeQuery, Car.class);
//        List<Car> cars = result.stream().map(SearchHit::getContent).collect(Collectors.toList());
//        System.out.println(cars);
//        return cars;

        List<Car> cars = new ArrayList<>();
        List<String> suggestions = new ArrayList<>();
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200, "http")
        ).build();

        String prefix = keywords;

        // Prepare the autocomplete request JSON
        String requestBody = "{\n" +
                "  \"suggest\": {\n" +
                "    \"car_name_suggestion\": {\n" +
                "      \"prefix\": \"" + prefix + "\",\n" +
                "      \"completion\": {\n" +
                "        \"field\": \"name_suggest\",\n" +
                "        \"size\": 5\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        try {
            // Create the search request
            Request request = new Request("POST", "/cars/_search");
            request.setJsonEntity(requestBody);

            // Execute the search request
            Response response = restClient.performRequest(request);

            String responseBody = EntityUtils.toString(response.getEntity());

            // Parse the JSON response using Jackson ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode suggestionNode = root.path("suggest").path("car_name_suggestion").get(0).path("options");

            // Convert autocomplete suggestions to a List of cars
            Iterator<JsonNode> iterator = suggestionNode.elements();
            while (iterator.hasNext()) {
                JsonNode node = iterator.next();
                Car car = new Car();
                car.setName(node.path("text").asText());
                suggestions.add(node.path("_source").path("name").asText());
                cars.add(car);
            }

            System.out.println("Suggestions: " + suggestions);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cars;
    }
}
