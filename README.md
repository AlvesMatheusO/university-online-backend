# 🎓 Sistema Acadêmico (academic-system)

Este projeto é uma API de gestão acadêmica desenvolvida com **Quarkus 3**, utilizando as capacidades mais recentes do **Java 21**, como **Virtual Threads** para alta escalabilidade.

---

## 🚀 Tecnologias e Versões

- **Java:** 21 (LTS)  
- **Framework:** Quarkus 3.15.1  
- **Banco de Dados:** PostgreSQL 16  
- **Persistência:** Hibernate ORM com Panache (Jakarta EE 10)  
- **Segurança:** Keycloak (OIDC)  
- **Migração de Banco:** Flyway  

---

## 🛠️ Pré-requisitos

Antes de começar, você precisará ter instalado:

- JDK 21 (configurado na variável `$JAVA_HOME`)
- Docker e Docker Compose
- Maven 3.9+ (ou utilize o `./mvnw` incluso no projeto)

---

## 🐳 Configurando o Ambiente (Docker)

O projeto depende de containers para o banco de dados e para a autenticação.

### ▶️ Subir os serviços

Para iniciar o PostgreSQL e o Keycloak:

```bash
docker-compose up -d
```

### 🔄 Resetar o ambiente

Caso precise limpar o banco de dados (remover todas as tabelas e volumes):

```bash
docker-compose down -v
docker-compose up -d
```

### 💻 Executando a Aplicação

O Quarkus permite alterar o código e visualizar as mudanças em tempo real sem reiniciar o servidor:

```bash
./mvnw quarkus:dev
```

🌐 Acessos

```bash
API: http://localhost:8080

Swagger UI (Documentação): http://localhost:8080/q/swagger-ui

Dev UI: http://localhost:8080/q/dev/
```

### 🧪 Executando Testes
O projeto utiliza JUnit 5 e REST Assured.

```bash
./mvnw test
./mvnw verify -Pnative
```

### 🔑 Segurança e Keycloak
A API está protegida via OIDC.

```bash
Realm: academic-system
Client ID: academic-api
URL do Servidor: http://localhost:8180
```
