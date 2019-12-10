package com.example.demo;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CamelController {

    @Autowired
    CamelContext camelContext;

    @Autowired
    ProducerTemplate producerTemplate;

    @RequestMapping(value = "/camel")
    public ResponseEntity<Todo> startCamel() {
        System.out.println("Received call!");
        return new ResponseEntity<>(producerTemplate.requestBody("direct:demoRoute", null, Todo.class), HttpStatus.OK);
    }


}
