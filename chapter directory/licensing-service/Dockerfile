#stage 1
# 도커 런타임에 사용될 도커 이미지를 지정
FROM openjdk:11-slim as build

# Add Maintainer Info
LABEL maintainer="Illary Huaylupo <illaryhs@gmail.com>"

# dockerfile-maven-plugin에 설정된 JAR_FILE 변수를 정의
ARG JAR_FILE

# JAR 파일을 이미지의 파일 시스템에 app.jar로 복사
COPY ${JAR_FILE} app.jar

#unpackage jar file
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf /app.jar)

#stage 2
# 새로운 이미지는 스프링 부트 앱에 대한 통짜 JAR 파일 대신 여러 레이어로 구성
FROM openjdk:11-slim

#Add volume pointing to /tmp
VOLUME /tmp

# state 1에서 build라고 명명된 첫 이미지에서 여러 레이어를 복사
ARG DEPENDENCY=/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# 컨테이너가 생성될 때 이미지의 라이선싱 서비스 애플리케이션을 실행 대상으로 지정
ENTRYPOINT ["java","-cp","app:app/lib/*","com.optimagrowth.license.LicenseServiceApplication"]
