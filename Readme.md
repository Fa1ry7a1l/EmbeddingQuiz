# Embedding Quiz

An experimental quiz application that compares a typed answer with a reference answer using text embeddings. Built with Java 21, Spring Boot, Thymeleaf, PostgreSQL with pgvector, and a local Ollama model.

The same answer is evaluated with cosine distance, Euclidean distance and negative inner product. The UI displays the three results separately.

## Run locally

Docker with Compose is sufficient; the application's multi-stage Dockerfile builds the Java code.

Start PostgreSQL and wait for its health check:

```sh
docker compose up -d --wait db
```

Start Ollama and download the model:

```sh
docker compose up -d ollama
docker compose exec ollama ollama pull nomic-embed-text
```

After the download completes, build and start the application:

```sh
docker compose up -d --build app
docker compose logs -f app
```

Open [localhost:8080](http://localhost:8080) when the application has started.

Start services in this order: the Compose file does not enforce application dependency readiness, and the embedding model must be downloaded before you upload a quiz.

## Create a quiz

Open [localhost:8080/admin](http://localhost:8080/admin), enter a name and paste question/answer pairs. Put the question on the first line and the answer on the next; separate pairs with a line containing `---`:

```text
What does HTTP stand for?
Hypertext Transfer Protocol
---
What is PostgreSQL?
A relational database management system
```

Uploading a quiz calls Ollama to embed each reference answer. Select a quiz from the home or admin page, enter an answer and inspect the three comparison results.

## How it works

| Component | Responsibility |
| --- | --- |
| `controller/` | Quiz pages, answer submission and admin upload |
| `service/OllamaClient` | Request embeddings from Ollama |
| `service/EmbeddingService` | Store reference-answer vectors |
| `service/SimilarityService` | Apply the three distance thresholds |
| `repo/` | JPA access and native pgvector queries |
| `resources/db/migration/` | Flyway schema and HNSW index migrations |
| `resources/templates/` | Thymeleaf UI |

Each reference answer is stored as a 768-dimensional vector. Answer queries compare against the reference for one question ID. HNSW indexes exist for the three operators, but these queries are not a general nearest-neighbor search over all questions.

## Configuration

| Variable | Purpose | Default in Compose |
| --- | --- | --- |
| `DB_URL` | PostgreSQL JDBC URL | `jdbc:postgresql://pg:5432/quiz` |
| `DB_USER`, `DB_PASS` | Database credentials | `quiz` / `quiz` |
| `OLLAMA_BASE_URL` | Ollama API address | `http://ollama:11434` |
| `OLLAMA_EMBED_MODEL` | Embedding model | `nomic-embed-text` |
| `SIM_THRESHOLD` | Input used to derive comparison thresholds | `0.75` |

Outside Compose, the configuration file defaults `SIM_THRESHOLD` to `0.95` and the Ollama address to `http://localhost:11434`; database variables must be supplied. Changing models can require a different vector dimension and recomputing stored embeddings.

PostgreSQL and Ollama data persist in named Docker volumes. To stop the stack while keeping that data, run:

```sh
docker compose down
```

## Scope

This is a local experiment, not a validated grading system. The distance thresholds are heuristic, including a fixed scale factor for inner product; their results are not interchangeable or calibrated accuracy estimates.

The admin routes have no authentication, and uploads perform synchronous embedding calls. There is no automated test suite in the repository. Keep the default setup for local demonstrations.

With JDK 21, the included Maven wrapper can build the application without Docker:

```sh
./mvnw package
```

On Windows use `mvnw.cmd`. Running the resulting JAR still requires PostgreSQL with pgvector, Ollama and the environment variables above.
