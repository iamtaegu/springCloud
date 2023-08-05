package com.optimagrowth.license.controller;

import com.optimagrowth.license.model.License;
import com.optimagrowth.license.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 스프링 부트에 이 서비스는 REST 기반 서비스이며
 * 응답은 JSON으로 서비스 요청 및 응답을 자동으로 직렬화 및 역직렬화할 것이라고 지정
 */
@RestController
@RequestMapping(value="v1/organization/{organizationId}/license")
public class LicenseController {

    @Autowired
    private LicenseService licenseService;

    @GetMapping(value="/{licenseId}")
    public ResponseEntity<License> getLicense(
            @PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId
    ) {
        License license = licenseService.getLicense(organizationId, licenseId);
        // License 객체와 200(OK) 상태 코드를 반환
        return ResponseEntity.ok(license);
    }
}
