package com.example.demo;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CamelTest extends RouteBuilder {
    @Autowired
    FileProcessor processor;

    @Override
    public void configure() throws Exception {
        from("{{reconciliation.files.in}}")
                .process(processor)
                .to("{{reconciliation.files.out}}");
    }
}
