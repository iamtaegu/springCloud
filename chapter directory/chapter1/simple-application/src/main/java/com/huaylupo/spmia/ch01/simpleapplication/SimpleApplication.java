package com.huaylupo.spmia.ch01.simpleapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
@RequestMapping(value="hello")
public class SimpleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleApplication.class, args);
	}

	@GetMapping(value="/{firstName}")
	public String helloGet(
			@PathVariable("firstName") String firstName,
			@RequestParam("lastName") String lastName
	) {
		return String.format("{\"message\":\"hello %s %s\"}", firstName, lastName);
	}

	@PostMapping
	public String helloPost(@RequestBody HelloRequest request) {
		return String.format("{\"message\":\"hello %s %s\"}", request.getFirstName(), request.getLastName());
	}

}
