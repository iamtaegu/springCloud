package com.optimagrowth.license;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 부트스트랩 클래스 - 스프링 부트가 애플리케이션을 시작하고 초기화함
 */
@SpringBootApplication
public class LicensingServiceApplication {

	public static void main(String[] args) {
		/**
		 * 스프링 컨테이너를 시작하고, ApplicationContext 객체를 반환
		 * 지금은 ApplicationContext 객체를 사용하지 않아 변수로 받지 않았음
		 */
		SpringApplication.run(LicensingServiceApplication.class, args);
	}

}
