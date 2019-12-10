package com.example.demo;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.stereotype.Component;

@Component
public class MyRouter extends RouteBuilder {

    private String route1 = "http://jsonplaceholder.typicode.com/todos/1";
    private String route2 = "http://jsonplaceholder.typicode.com/users/${body.userId}";


    @Override
    public void configure() throws Exception {
        from("direct:demoRoute")
                .setHeader("CamelHttpMethod", constant("GET"))
                .to(route1)
                .unmarshal().json(JsonLibrary.Jackson, Todo.class)
                .enrich("direct:enricher", new ExampleAggregationStrategy());

        from("direct:enricher").setHeader(Exchange.HTTP_URI, simple(route2))
                .transform().simple("${null}").to("http://ignored").unmarshal().json(JsonLibrary.Jackson, User.class);
    }

    public static class ExampleAggregationStrategy implements AggregationStrategy {

        public Exchange aggregate(Exchange original, Exchange resource) {
            Todo originalBody = (Todo) original.getIn().getBody();
            User resourceResponse = (User) resource.getIn().getBody();
            originalBody.setUser(resourceResponse);

            if (original.getPattern().isOutCapable()) {
                original.getOut().setBody(originalBody);
            }
            return original;
        }
    }

}
