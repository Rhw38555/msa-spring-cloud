package com.example.orderservice.messagequeue;

import com.example.orderservice.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


/*
sink connect 등록
{
    "name": "my-order-sink-connect",
    "config": {
        "connector.class" : "io.confluent.connect.jdbc.JdbcSinkConnector",
        "connection.url" : "jdbc:mysql://localhost:3306/mydb",
        "connection.user" : "root",
        "connection.password" : "",
        "auto.create" : "true",
        "auto.evolve" : "true",
        "delete.enabled" : "false",
        "topics" : "orders",
        "tasks.max" : "1"
    }
}
 */
@Service
@Slf4j
public class OrderProducer {
    private KafkaTemplate<String, String> kafkaTemplate;
    List<Field> fields = Arrays.asList(new Field("string", true, "order_id"),
                new Field("string", true, "user_id"),
                new Field("string", true, "product_id"),
                new Field("int32", true, "qty"),
                new Field("int32", true, "unit_price"),
                new Field("int32", true, "total_price"));

    Schema schema = Schema.builder()
            .type("struct")
            .fields(fields)
            .optional(false)
            .name("orders")
            .build();

    @Autowired
    public OrderProducer(KafkaTemplate<String, String> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    // producer에 메세지를 전달하면 connector가 디비에 직접 바로 저장해야되서 포맷을 맞추어줌.
    public OrderDto send(String topic, OrderDto orderDto){
        Payload payload = Payload.builder()
                .order_id(orderDto.getOrderId())
                .user_id(orderDto.getUserId())
                .product_id(orderDto.getProductId())
                .qty(orderDto.getQty())
                .unit_price(orderDto.getUnitPrice())
                .total_price(orderDto.getTotalPrice())
                .build();

        KafkaOrderDto kafkaOrderDto = new KafkaOrderDto(schema, payload);
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try{
            jsonInString = mapper.writeValueAsString(kafkaOrderDto);
        }catch (JsonProcessingException ex){
            ex.printStackTrace();
        }
        kafkaTemplate.send(topic, jsonInString);
        log.info("Order Producer send data from the Order microservice: " + kafkaOrderDto);

        return orderDto;
    }
}
