package com.optimagrowth.license;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.optimagrowth.license.config.ServiceConfig;
import com.optimagrowth.license.utils.UserContextInterceptor;

@SpringBootApplication
@RefreshScope
@EnableDiscoveryClient
@EnableFeignClients
@EnableEurekaClient
//@EnableBinding(Sink.class)
public class LicenseServiceApplication {
	
	@Autowired
    private ServiceConfig serviceConfig;

	private static final Logger logger = LoggerFactory.getLogger(LicenseServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(LicenseServiceApplication.class, args);
	}

	/**
	 * 스프링은 레디스 서버와 통신하기 위해 제디스(Jedis) 오픈 소스 프로젝트를 사용
	 * 레디스 서버에 대한 데이터베이스 커넥션을 설정
	 */
	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		String hostname = serviceConfig.getRedisServer();
		int port = Integer.parseInt(serviceConfig.getRedisPort());
	    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(hostname, port);
	    //redisStandaloneConfiguration.setPassword(RedisPassword.of("yourRedisPasswordIfAny"));
	    return new JedisConnectionFactory(redisStandaloneConfiguration);
	}

	/**
	 *
	 * 레디스 서버에 액션을 실행할 RedisTemplate을 생성
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(jedisConnectionFactory());
		return template;
	}

//	@StreamListener(Sink.INPUT)
//	public void loggerSink(OrganizationChangeModel orgChange) {
//		logger.debug("Received {} event for the organization id {}", orgChange.getAction(), orgChange.getOrganizationId());
//	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(Locale.US);
		return localeResolver;
	}
	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setBasenames("messages");
		return messageSource;
	}

	@Primary
	@Bean
	public RestTemplate getCustomRestTemplate() {
		RestTemplate template = new RestTemplate();
		List interceptors = template.getInterceptors();
		if (interceptors == null) {
			template.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
		} else {
			interceptors.add(new UserContextInterceptor());
			template.setInterceptors(interceptors);
		}

		return template;
	}


}
