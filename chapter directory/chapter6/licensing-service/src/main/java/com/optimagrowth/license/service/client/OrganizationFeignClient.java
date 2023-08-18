package com.optimagrowth.license.service.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.optimagrowth.license.model.Organization;

@FeignClient("organization-service") // Feign에 서비스를 알려줌
public interface OrganizationFeignClient {
    @RequestMapping( // 엔드포인트 경로와 액션을 정의
            method= RequestMethod.GET,
            value="/v1/organization/{organizationId}",
            consumes="application/json")
    Organization getOrganization(@PathVariable("organizationId") String organizationId); // 엔드포인트에 전달되는 매개변수를 정의
}
