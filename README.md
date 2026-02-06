# Device Tracker — Serviço simples de pings de dispositivos

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![WebFlux](https://img.shields.io/badge/WebFlux-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white)

Descrição curta: serviço leve em Kotlin + Spring Boot (WebFlux) que recebe pings de dispositivos e mantém um registro reativo em MongoDB; rotas selecionadas são protegidas por uma API Key configurável.

---

## Visão geral

Este repositório contém um esqueleto de um sistema de "ping" para dispositivos. O objetivo atual é permitir que dispositivos/clientes registrem pings contendo identificador, versão do firmware e coordenadas (latitude/longitude). O projeto foi concebido como uma base reativa (WebFlux) e usa MongoDB reativo para persistência.

Principais pontos implementados:
- Endpoints REST para registrar e listar pings: `POST /api/v1/pings` e `GET /api/v1/pings`.
- Modelo de domínio `DevicePing` com campos: `id`, `deviceId`, `firmwareVersion`, `timestamp`, `latitude`, `longitude`.
- Repositório reativo `DevicePingRepository` (Spring Data Reactive MongoDB).
- Serviço `DevicePingService` com operações básicas de salvar e listar.
- Filtro reativo `ApiKeyFilter` que protege rotas configuradas verificando o header `X-API-KEY` contra a propriedade `security.api-key`.
- Carregamento de variáveis de ambiente via `dotenv` (no `main`), permitindo uso de `.env` em desenvolvimento.

Observação: o projeto está em estágio inicial (esqueleto). Futuras features planejadas incluem métricas por dispositivo, TTL para pings antigos, autenticação/autorizações mais robustas, endpoints agregados e dashboards.

---

## Tecnologias

- Kotlin
- Spring Boot (WebFlux)
- Spring Data Reactive MongoDB
- Reactor (Flux/Mono)
- dotenv (io.github.cdimascio.dotenv)
- Gradle (wrapper)

---

## Requisitos

- Java 17+
- MongoDB (pode ser local ou em container)
- Gradle (use o wrapper `./gradlew`)

---

## Configuração

As propriedades principais estão em `src/main/resources/application.yml`. As variáveis mais relevantes:

- `MONGO_URI` — URI de conexão com o MongoDB (padrão: `mongodb://localhost:27017/devicetracker`).
- `INTERNAL_API_KEY` — chave que será comparada com o header `X-API-KEY` para acessar rotas protegidas.

Você pode usar um arquivo `.env` na raiz com estas variáveis em desenvolvimento (o bootstrap do app já carrega `.env`):

```
MONGO_URI=mongodb://localhost:27017/devicetracker
INTERNAL_API_KEY=uma_chave_secreta
```

---

## Executando

Modo rápido (Gradle):

```
./gradlew bootRun
```

A aplicação por padrão escuta em `http://localhost:8080`.

Executando testes:

```
./gradlew test
```

Executando com Docker (exemplo manual):
- Inicie um MongoDB via Docker:

```
docker run -d --name dev-mongo -p 27017:27017 -e MONGO_INITDB_DATABASE=devicetracker mongo:6
```

- Em seguida, execute a aplicação localmente (ou crie um `docker-compose` conforme desejar).

---

## Endpoints

Base: `/api/v1/pings`

1) Registrar ping
- Método: POST
- URL: `/api/v1/pings`
- Header: `X-API-KEY: <INTERNAL_API_KEY>` (aplicado por `ApiKeyFilter`)
- Body (JSON):

```json
{
  "deviceId": "device-123",
  "firmwareVersion": "1.0.0",
  "latitude": -23.5489,
  "longitude": -46.6388
}
```

- Resposta: `200 OK` com o objeto `DevicePing` salvo (inclui `id`).

2) Listar pings
- Método: GET
- URL: `/api/v1/pings`
- Header: `X-API-KEY: <INTERNAL_API_KEY>`
- Resposta: `200 OK` com array/stream de `DevicePing` (retorna `Flux<DevicePing>`).

Exemplo curl (registro):

```bash
curl -X POST http://localhost:8080/api/v1/pings \
  -H "Content-Type: application/json" \
  -H "X-API-KEY: uma_chave_secreta" \
  -d '{"deviceId":"device-1","firmwareVersion":"1.0","timestamp":"2026-02-06T12:00:00","latitude":-23.5,"longitude":-46.6}'
```

Exemplo curl (listar):

```bash
curl -X GET http://localhost:8080/api/v1/pings -H "X-API-KEY: uma_chave_secreta"
```

---

## Modelo de dados

`DevicePing` (arquivo: `src/main/kotlin/com/gblins/devicetracker/model/DevicePing.kt`)

- `id: String?` — id gerado pelo MongoDB
- `deviceId: String` — identificador do dispositivo
- `firmwareVersion: String` — versão do firmware
- `timestamp: LocalDateTime` — data/hora do ping (formato ISO-8601)
- `latitude: Double` — coordenada latitude
- `longitude: Double` — coordenada longitude

---

## Notas de desenvolvimento e próximos passos

Futuras features para evolução do projeto:
- Adicionar TTL/expiração para pings antigos (índice TTL no MongoDB).
- Agregações/contadores por dispositivo (quantos online, últimos vistos, bounding box queries).
- Endpoints paginados e filtros (por deviceId, período, área geográfica).
- Métricas (Prometheus) e health checks de dependências.
- Autenticação/authorization mais robusta (token JWT, roles) se necessário.
- Dockerfile / docker-compose com `api` + `mongo` para facilitar testes e CI.

---

## Arquivos importantes

- `src/main/kotlin/com/gblins/devicetracker/config/ApiKeyFilter.kt` — filtro que protege rotas por `X-API-KEY`.
- `src/main/kotlin/com/gblins/devicetracker/DeviceTrackerApplication.kt` — ponto de entrada (carrega `.env`).
- `src/main/kotlin/com/gblins/devicetracker/controller/DevicePingController.kt` — endpoints REST.
- `src/main/kotlin/com/gblins/devicetracker/model/DevicePing.kt` — modelo de domínio.
- `src/main/resources/application.yml` — propriedades (MONGO_URI, INTERNAL_API_KEY, server.port).

---

## Licença

Consulte o arquivo `LICENSE` no repositório.

<p align="center"><sub>Desenvolvido por Gabriel Lins</sub></p>
