# Usar a imagem do JDK como base para a construção do JAR
FROM maven:3.9.5-eclipse-temurin-21 AS builder

# Diretório de trabalho dentro do contêiner
WORKDIR /app

# Copiar os arquivos do projeto para o contêiner
COPY . .

# Rodar o Maven para compilar o projeto e gerar o JAR
RUN mvn clean package -DskipTests

# Usar a imagem mais leve do JDK para executar a aplicação
FROM eclipse-temurin:21-jdk-jammy

# Criar o diretório para a aplicação dentro do contêiner
WORKDIR /app

# Copiar o JAR gerado na etapa anterior para esta imagem
COPY --from=builder /app/target/*.jar app.jar

# Expor a porta em que o serviço será acessado (substitua pela porta correta)
EXPOSE 8080

# Comando para iniciar o microsserviço
CMD ["java", "-jar", "app.jar"]