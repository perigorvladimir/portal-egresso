# Etapa 1: Construção da imagem
FROM maven:3.9.0-openjdk-21-slim AS builder

# Defina o diretório de trabalho
WORKDIR /app

# Copie o arquivo pom.xml e baixe as dependências
COPY pom.xml .

# Baixar as dependências do Maven sem compilar os arquivos (para cache)
RUN mvn dependency:go-offline

# Copie o código fonte da aplicação
COPY src ./src

# Compile e empacote a aplicação como um arquivo JAR
RUN mvn clean package

# Etapa 2: Imagem para rodar a aplicação
FROM openjdk:21-jdk-slim

# Defina o diretório de trabalho dentro do container
WORKDIR /app

# Copie o arquivo JAR gerado pela etapa de build
COPY --from=builder /app/target/portal-egresso-0.0.1-SNAPSHOT.jar /app/app.jar

# Exponha a porta em que a aplicação irá rodar
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
