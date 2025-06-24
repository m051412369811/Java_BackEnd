FROM maven:3.9.8-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
# 使用 -Pprod 來啟動 production profile (如果有的話)，並跳過測試
RUN mvn clean package -DskipTests

# --- 第二階段：建立最終的 Java 17 運行環境 ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# 請將 demo-0.0.1-SNAPSHOT.jar 換成您 target 目錄下實際的 .jar 檔名
COPY --from=builder /app/target/demo-0.0.1-SNAPSHOT.jar application.jar
EXPOSE 5000
ENTRYPOINT ["java", "-jar", "application.jar"]