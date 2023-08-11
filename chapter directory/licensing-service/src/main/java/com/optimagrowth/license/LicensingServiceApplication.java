package com.optimagrowth.license;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * 부트스트랩 클래스 - 스프링 부트가 애플리케이션을 시작하고 초기화함
 * @RefreshScopr
 * 	- 스프링 애플리케이션이 구성 정보를 다시 읽게 만드는 /refresh 엔드포인트에 접근할 수 있게 함
 * 	- 단, 사용자가 정의한 스프링 프로퍼티만 다시 로드함
 */
@SpringBootApplication
@RefreshScope
public class LicensingServiceApplication {

	public static void main(String[] args) {
		/**
		 * 스프링 컨테이너를 시작하고, ApplicationContext 객체를 반환
		 * 지금은 ApplicationContext 객체를 사용하지 않아 변수로 받지 않았음
		 */
		SpringApplication.run(LicensingServiceApplication.class, args);
	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(Locale.US);
		return localeResolver;
	}
	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource messageSource =
				new ResourceBundleMessageSource();
		messageSource.setUseCodeAsDefaultMessage(true); //메시지가 발견되지 않아도 에러를 던지지 않고, 대신 메시지 코드를 반환
		messageSource.setBasenames("messages"); // 언어 프로퍼티 파일의 기본 이름을 설정
		return messageSource;
	}

}
