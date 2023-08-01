package com.example.carrent.model;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "notifications")
public class Notification {
    private String id;
    @JsonAlias({"@timestamp", "_index"}) //I know it is not correct to put "_index" as an alias, but it is the only way I found to extract the timestamp from the response
    private String timestamp;
    private String loglevel;
    private String thread;
    private String logger;
    private String message;
}
