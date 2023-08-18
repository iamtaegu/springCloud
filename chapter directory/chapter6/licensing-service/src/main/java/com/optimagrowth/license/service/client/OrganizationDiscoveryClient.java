package com.optimagrowth.license.service.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.optimagrowth.license.model.Organization;

@Component
public class OrganizationDiscoveryClient {

    // 1. Discovery Client 주입
    // 이 클래스를 사용하여 스프링 클라우드 로드 밸런서와 상호 작용
    @Autowired
    private DiscoveryClient discoveryClient;

    public Organization getOrganization(String organizationId) {
        RestTemplate restTemplate = new RestTemplate();
        // 2. 조직 서비스의 모든 인스턴스 리스트를 가져옴
        // 호스트 이름, 포트, URI 같은 인스턴스 정보
        List<ServiceInstance> instances = discoveryClient.getInstances("organization-service");

        if (instances.size()==0) return null;
        String serviceUri = String.format("%s/v1/organization/%s",instances.get(0).getUri().toString(), organizationId);

        // 서비스 호출을 위해 표준 스프링 RestTemplate 클래스를 사용
        ResponseEntity< Organization > restExchange =
                restTemplate.exchange(
                        serviceUri,
                        HttpMethod.GET,
                        null, Organization.class, organizationId);

        return restExchange.getBody();
    }
}
