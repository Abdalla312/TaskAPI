package com.example.taskapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Task API application.
 *
 * Django equivalent: this is like manage.py + wsgi.py combined.
 * @SpringBootApplication does three things:
 *   1. @Configuration   — this class can define beans (like Django settings)
 *   2. @ComponentScan   — auto-discovers @Controller, @Service, @Repository classes
 *   3. @EnableAutoConfiguration — Spring Boot configures things for you based on dependencies
 */
@SpringBootApplication
public class TaskApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskApiApplication.class, args);
    }
}
