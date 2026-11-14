# Employee Semantic Search (LangChain4j + Ollama)

Searches an employee directory using natural-language queries. Records are
matched by *meaning* — embeddings and cosine similarity — not exact keywords.

## How it works

1. A sample dataset of 12 employees (ID, name, department, designation,
   skills) is defined in `EmployeeDataset`.
2. Each record is converted to a searchable sentence and embedded with the
   Ollama `nomic-embed-text` model via LangChain4j's `OllamaEmbeddingModel`.
3. Embeddings are stored in LangChain4j's `InMemoryEmbeddingStore`.
4. User queries are embedded and the store returns the top-3 matches by
   cosine similarity (min score 0.45).

## Prerequisites

- Java 21+, Maven
- Ollama running at `http://localhost:11434` with an embedding model:

  ```bash
  ollama pull nomic-embed-text
  ```

## Run

```bash
mvn compile exec:java
```

Then type natural-language queries, e.g.:

- `who is good at machine learning?`
- `someone who handles hiring and payroll`
- `find a person skilled in cloud infrastructure and deployments`
- `exit` to quit

## Configuration

| Env var                   | Default                  |
| ------------------------- | ------------------------ |
| `OLLAMA_BASE_URL`         | `http://localhost:11434` |
| `OLLAMA_EMBEDDING_MODEL`  | `nomic-embed-text`       |

## Structure

```
src/main/java/com/example/employeesearch/
├── Employee.java           # record + searchable-text conversion
├── EmployeeDataset.java    # sample employee data
└── EmployeeSearchApp.java  # embedding, indexing, query loop, results
```
