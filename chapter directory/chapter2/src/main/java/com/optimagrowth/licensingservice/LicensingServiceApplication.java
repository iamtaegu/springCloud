package com.optimagrowth.licensingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
@RequestMapping(value="hello")
@EnableEurekaClient // 스프링 부트에 유레카 클라이언트를 활성화하도록 지정
public class LicensingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LicensingServiceApplication.class, args);
	}

	@GetMapping(value="/{firstName}")
	public String helloGet(
			@PathVariable("firstName") String firstName,
			@RequestParam("lastName") String lastName
	) {
		return helloRemoteServiceCall(firstName, lastName);
	}

	public String helloRemoteServiceCall(String firstName, String lastName) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> restExchange =
				restTemplate.exchange(
						"http://logical-service-id/name/" + "{firstName}/{lastName}",
						HttpMethod.GET, null, String.class, firstName, lastName
				);
		return restExchange.getBody();
	}

}
