package com.optimagrowth.organization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;

@SpringBootApplication
@RefreshScope
// 스프링 클라우드 스트림의 메시지 브로커와 바인딩(메시지를 발행하고 소비)하도록 지정
// Source 클래스에 정의된 채널들을 사용할 것을 스프링 클라우드 스트림에 알림
@EnableBinding(Source.class)
public class OrganizationServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(OrganizationServiceApplication.class, args);
	}

}
