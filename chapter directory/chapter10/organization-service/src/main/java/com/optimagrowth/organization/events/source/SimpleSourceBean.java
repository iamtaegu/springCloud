package com.optimagrowth.organization.events.source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.optimagrowth.organization.events.model.OrganizationChangeModel;
import com.optimagrowth.organization.utils.ActionEnum;
import com.optimagrowth.organization.utils.UserContext;

/**
 * 메시지를 받아 직렬화하고 메시지를 채널에 발행
 *
 */
@Component
public class SimpleSourceBean {
    private Source source;

    private static final Logger logger = LoggerFactory.getLogger(SimpleSourceBean.class);

    @Autowired
    public SimpleSourceBean(Source source){
        this.source = source;
    }

    public void publishOrganizationChange(ActionEnum action, String organizationId){
       logger.debug("Sending Kafka message {} for Organization Id: {}", action, organizationId);
        OrganizationChangeModel change =  new OrganizationChangeModel(
                OrganizationChangeModel.class.getTypeName(),
                action.toString(),
                organizationId,
                UserContext.getCorrelationId());

        /**
         * 토픽에 대한 모든 통신은 스프링 클라우드 스트림의 채널(자바 인터페이스 클래스)로 함
         * 여기서는 output() 메서드를 노출하는 Source 인터페이스 사용
         */
        source.output().send(MessageBuilder.withPayload(change).build());
    }
}
