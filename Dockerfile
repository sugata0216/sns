# ---- Stage 1: Build ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Maven Wrapper と pom.xml を先にコピーして依存関係キャッシュを効かせる
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline -B

# ソースコードをコピーしてビルド
COPY src ./src
RUN ./mvnw clean package -DskipTests

# ---- Stage 2: Runtime ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# ビルド済みJARだけをコピー(軽量化)
COPY --from=build /app/target/*.jar app.jar

# アップロードファイル用ディレクトリ(永続化はされないが、起動時エラー防止のため作成)
RUN mkdir -p /app/uploads

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
