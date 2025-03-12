# Etapa 1: Construção da imagem
FROM ubuntu:latest AS build

# Instalar dependências
RUN apt-get update && apt-get install -y openjdk-21-jdk maven

# Definir o diretório de trabalho
WORKDIR /app

# Copiar o arquivo pom.xml e baixar as dependências
COPY pom.xml .

# Baixar as dependências do Maven (sem compilar ainda)
RUN mvn dependency:go-offline

# Copiar o código-fonte da aplicação
COPY src ./src

# Construir o JAR
RUN mvn clean install

# Definir o diretório de trabalho dentro do container
WORKDIR /app

# Copiar o arquivo JAR gerado pelo estágio de build
COPY --from=build /app/target/portal-egresso-0.0.1-SNAPSHOT.jar app.jar

# Expor a porta em que a aplicação vai rodar
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]