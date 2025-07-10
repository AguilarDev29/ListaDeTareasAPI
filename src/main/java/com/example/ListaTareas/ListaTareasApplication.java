package com.example.ListaTareas;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ListaTareasApplication {

	public static void main(String[] args) {
		SpringApplication.run(ListaTareasApplication.class, args);
	}

}