#### mvn clean package dockerfile:build && docker-compose -f docker/docker-compose.yml up 

## 10.3.2 조직 서비스에서 메시지 생산자 작성

    * 의도 
        - 조직 데이터가 추가, 수정, 삭제될 때마다 조직 서비스가 카프카 토픽에 메시지를 발행하여 조직 변경 이벤트 알림

    * 참고     
        - 모든 통신은 스프링 클라우드 스트림의 채널(channel)이라는 자바 인터페이스 클래스로 함 
            * output() 메서드를 노출하는 Source 인터페이스를 사용
            * source.output()
        - 메시지 브로커뿐만 아니라 특정 메시지 큐에 조직 서비스를 바인딩하는 방법은 구성 설정으로 수행
        - 아파치 주키퍼는 구성 정보 및 이름 데이터를 유지 관리하는 데 사용되고, 분산 시스템에서 유연한 동기화를 제공

    * 처리 
        1. 의존성 추가 
            - 스프링 클라우드 스트림
            - 스프링 클라우드 카프카
        2. 스프링 클라우드 스트림의 메시지 브러커와 바인딩하도록(메시지를 발행하고 소비) 지정 
            - 부트스트랩 클래스에 @EnableBinding(Source.class) 애너테이션 추가 
        3. UserContext 변수를 ThreadLocal로 만들기
            - 멤버변수를 ThreadLocal로 저장하면 현재 스레드에 대한 데이터를 스레드별로 저장할 수 있음 
            - 여기에 설정된 정보는 그 값을 설정한 스레드만 읽을 수 있음
        4. 메시지 발행 로직 작성
            - SimpleSourceBean
        5. 메시지 발생
            - OrganizationService

    * 검토
        - 어떤 데이터를 메시지에 넣어야 할까 ?
            * 정확하게 얼마나 많은 데이터를 메시지에 넣어야 하는가? 
            * 예제에서는
                - 변경된 조직 레코드의 조직 ID만 전달
                - 메시지에 변경된 데이터의 복사본을 넣지 않고
                - 시스템 이벤트에 기반을 둔 메시지를 사용하여 데이터 상태가 변경되었음을 알리고
                - 항상 다른 서비스가 데이터의 새 복사본을 조회하기 위해 원본에서 가져오게 함
                - 실행 시간 면에서 비용이 더 들지만, 항상 최신 데이터 복사본을 보장할 수 있음

## 10.3.3 라이선싱 서비스에서 메시지 소비자 작성

    * 처리 
        1. 의존성 추가 
            - 스프링 클라우드 스트림
            - 스프링 클라우드 카프카
        2. 스프링 클라우드 스트림의 메시지 브러커와 바인딩하도록(메시지를 발행하고 소비) 지정 
            - 부트스트랩 클래스에 @EnableBinding(Sink.class) 애너테이션 추가 

## 10.4 스프링 클라우드 스트림 사용 사례: 분산 캐싱

    * 의도
        - 메시지로 통신하는 두 서비스가 준비되었지만, 메시지를 주고받아도 실제로 하는 일은 없음
        - 분산 캐싱 예제를 추가로 구축해봄 
            1. 라이선싱 서비스는 특정 라이선스와 연관된 조직 데이터에 대해 분산 레디스 캐시를 확인
            2. 캐시에 없다면 조직 서비스를 호출하고 결과를 레디스 해시에 캐싱
            3. 조직 서비스에서 데이터가 업데이트되면 조직 서비스는 카프카에 메시지를 발행
            4. 라이선싱 서비스는 메시지를 가져와 캐시를 삭제하도록 레디스에 DELETE를 호출
    * 참고
        - 클라우드 캐싱과 메시징
            * 분산 캐시로 레디스를 사용하는 것은 클라우드에서 마이크로서비스 개발과 연관됨
            * 레디스 사용에는 아래 장점이 있음
                1. 보유한 데이터 검색 성능 향상: 캐시를 사용하여 데이터베이스에서 읽어 오지 않아도 됨
                2. 데이터를 보유한 데이터베이스 테이블에 대한 부하(와 비용)를 줄임
                3. 주 데이터 저장소 또는 데이터베이스에 성능 문제가 있다면 정상적으로 저하될 수 있도록 회복력을 높임
        - 레디스는 크고 분산된 인메모리 해시맵과 같은 역할을 하는 키-값 데이터 저장소

    * 처리 
        - 스프링 데이터를 사용하면 레디스를 라이선싱 서비스에 쉽게 도입이 가능 
        
        1. 스프링 데이터 레디스 의존성을 포함 
        2. 레디스 서버에 대한 데이터베이스 커넥션 설정
            - 스프링은 레디스 서버와 통신하기 위해 제디스(Jedis) 오픈소스를 사용 
            - JedisConnectionFactory 클래스를 스프링 빈으로 노출
            - 해당 클래스 빈 등록을 통해 레디스 커넥션 설정이되면 RedisTemplate
        3. 코드에서 레디스 해시와 상호 작용하는 데 사용될 스프링 데이터 레디스 저장소를 정의
        4. 레디스와 라이선싱 서비스가 조직 데이터를 저장하고 읽어 오게 설정
            - 조직 데이터가 필요할 때마다 호출되기 전에 레디스 캐시를 먼저 체크

    * 검토

## 10.4.2 사용자 정의 채널 설정

    * 의도
        - 스프링 클라우드 스트림의 Source 및 Sink 인터페이스가 함께 패키징된 기본 입출력 채널을 사용할 수 있고
        - 애플리케이션에 두 개 이상의 채널을 정의하거나 고유한 채널 이름을 사용하고 싶으면 자체 인터페이스를 정의하여 필요한 만큼 입출력 채널을 노출시킴 
