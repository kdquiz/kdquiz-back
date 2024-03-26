FROM openjdk:17 AS builder

# 필요한 패키지 설치
RUN microdnf install findutils

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

RUN chmod +x ./gradlew
RUN ./gradlew bootJAR

FROM openjdk:17

# builder 스테이지에서 생성된 .jar 파일 복사
COPY --from=builder build/libs/*.jar app.jar

# wait-for-it.sh 스크립트 복사 및 실행 권한 부여
COPY wait-for-it.sh wait-for-it.sh
RUN chmod +x wait-for-it.sh

EXPOSE 8080

# wait-for-it.sh 실행 후 애플리케이션 실행
ENTRYPOINT ["./wait-for-it.sh", "mysqldb:3306", "--"]
CMD ["java", "-jar", "/app.jar"]
