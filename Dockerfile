# ====== 第一阶段：Maven 构建 ======
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /build
# 先复制 pom.xml 利用 Docker 缓存加速
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN mvn dependency:go-offline -B -q || true

# 复制源码并打包
COPY src ./src
RUN mvn package -DskipTests -B -q

# ====== 第二阶段：运行 ======
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 创建非 root 用户
RUN addgroup -g 1000 app && adduser -u 1000 -G app -D app

# 复制 JAR
COPY --from=builder /build/target/*.jar app.jar

# 创建数据目录
RUN mkdir -p /app/tmp && chown -R app:app /app

USER app

EXPOSE 8123

HEALTHCHECK --interval=30s --timeout=5s --retries=3 \
  CMD wget -qO- http://localhost:8123/api/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
