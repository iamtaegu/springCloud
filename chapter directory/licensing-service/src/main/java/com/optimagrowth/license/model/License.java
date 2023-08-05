package com.optimagrowth.license.model;

import lombok.Data;
import lombok.ToString;

/**
 * 캡슐화는 객체지향 프로그래밍의 주요 원칙이고
 * 자바에서 캡슐화하려면 클새르 변수를 private로 선언하고, getter와 setter를 제공
 *
 * 라이선스 정보를 보관하는 POJO(Plain Old Java Object)
 */
@Data
@ToString
public class License {

    private int id;
    private String licenseId;
    private String description;
    private String organizationId;
    private String productName;
    private String licenseType;

}
