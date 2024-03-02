# 기본 이미지로 OpenJDK를 포함한 이미지 사용
FROM eclipse-temurin:21.0.2_13-jdk-jammy as build

# 애플리케이션 디렉토리 생성
WORKDIR /app

# Gradle 래퍼와 프로젝트 파일 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Gradle을 사용하여 Spring Boot 애플리케이션 빌드
RUN ./gradlew build -x test

# 최종 이미지
FROM eclipse-temurin:21.0.2_13-jre-jammy

WORKDIR /app

# 빌드 단계에서 생성된 jar 파일을 최종 이미지로 복사
COPY --from=build /app/build/libs/*.jar gpt-blog-backend.jar

# 컨테이너 실행 시 애플리케이션 실행
ENV SPRING_PROFILES_ACTIVE=appkey
ENTRYPOINT ["java","-jar","/app/gpt-blog-backend.jar"]