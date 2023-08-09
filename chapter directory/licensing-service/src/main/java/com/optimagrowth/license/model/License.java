package com.optimagrowth.license.model;

import lombok.Data;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

/**
 * 캡슐화는 객체지향 프로그래밍의 주요 원칙이고
 * 자바에서 캡슐화하려면 클새르 변수를 private로 선언하고, getter와 setter를 제공
 *
 * 라이선스 정보를 보관하는 POJO(Plain Old Java Object)
 *
 * HATEOAS 구현
 *  스프링부트 hateoas 의존성을 추가하고
 *  모델 클래스가 RepresentationModel를 상속 받음
 *  RepresentationModel<License>의 상속은 License 모델 클래스에 링크를 추가할 수 있게 함
 */
@Data
@ToString
public class License extends RepresentationModel<License> {

    private int id;
    private String licenseId;
    private String description;
    private String organizationId;
    private String productName;
    private String licenseType;

}
