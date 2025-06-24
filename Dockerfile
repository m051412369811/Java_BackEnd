# --- 第一階段：使用 Maven 作為建置環境 ---
# 使用官方的 Maven 映像檔，它包含了 Java (JDK)
FROM maven:3.9.8-eclipse-temurin-21 AS builder

# 將工作目錄設定為 /app
WORKDIR /app

# 先複製 pom.xml，這可以利用 Docker 的快取機制，當依賴不變時，不必重新下載
COPY pom.xml .
RUN mvn dependency:go-offline

# 複製整個專案的原始碼
COPY src ./src

# 執行打包指令
RUN mvn clean package -DskipTests


# --- 第二階段：建立最終的運行環境 ---
# 使用一個更輕量的、只包含 Java 執行環境 (JRE) 的映像檔
FROM eclipse-temurin:17-jre-jammy

# 將工作目錄設定為 /app
WORKDIR /app

# 從第一階段 (builder) 的建置結果中，複製打包好的 .jar 檔案
# 請將 demo-0.0.1-SNAPSHOT.jar 換成您 target 目錄下實際的 .jar 檔名
COPY --from=builder /app/target/demo-0.0.1-SNAPSHOT.jar application.jar

# 設定容器對外開放的 Port (對應您 properties 中的 server.port)
EXPOSE 5000

# 容器啟動時要執行的指令
ENTRYPOINT ["java", "-jar", "application.jar"]