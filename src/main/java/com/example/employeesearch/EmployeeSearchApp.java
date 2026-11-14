package com.example.employeesearch;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Semantic search over an employee directory using LangChain4j + Ollama.
 *
 * Employee records are converted to embeddings with a local Ollama embedding
 * model, stored in an in-memory embedding store, and matched to
 * natural-language queries by cosine similarity.
 */
public class EmployeeSearchApp {

    private static final String OLLAMA_BASE_URL =
            System.getenv().getOrDefault("OLLAMA_BASE_URL", "http://localhost:11434");
    private static final String EMBEDDING_MODEL =
            System.getenv().getOrDefault("OLLAMA_EMBEDDING_MODEL", "nomic-embed-text");
    private static final int MAX_RESULTS = 3;
    private static final double MIN_SCORE = 0.45;

    public static void main(String[] args) {
        EmbeddingModel embeddingModel = OllamaEmbeddingModel.builder()
                .baseUrl(OLLAMA_BASE_URL)
                .modelName(EMBEDDING_MODEL)
                .build();

        // In-memory embedding store; swap for a persistent store (e.g. pgvector,
        // Chroma, Pinecone) without changing the rest of the code.
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        List<Employee> employees = EmployeeDataset.sampleEmployees();
        indexEmployees(employees, embeddingModel, embeddingStore);

        System.out.println();
        System.out.println("=== Employee Semantic Search ===");
        System.out.println("Indexed " + employees.size() + " employees using Ollama model '"
                + EMBEDDING_MODEL + "'.");
        System.out.println("Ask things like \"who knows machine learning?\" or");
        System.out.println("\"someone who handles hiring\". Type 'exit' to quit.");
        System.out.println();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("Search> ");
                if (!scanner.hasNextLine()) {
                    break;
                }
                String query = scanner.nextLine().trim();
                if (query.isEmpty()) {
                    continue;
                }
                if (query.equalsIgnoreCase("exit") || query.equalsIgnoreCase("quit")) {
                    break;
                }
                search(query, embeddingModel, embeddingStore);
            }
        }

        System.out.println("Goodbye!");
    }

    private static void indexEmployees(List<Employee> employees,
                                       EmbeddingModel embeddingModel,
                                       EmbeddingStore<TextSegment> embeddingStore) {
        System.out.println("Embedding " + employees.size() + " employee records...");
        for (Employee employee : employees) {
            TextSegment segment = TextSegment.from(
                    employee.toSearchableText(),
                    dev.langchain4j.data.document.Metadata.from(Map.of(
                            "id", employee.id(),
                            "name", employee.name(),
                            "department", employee.department(),
                            "designation", employee.designation(),
                            "skills", employee.skills())));
            Embedding embedding = embeddingModel.embed(segment).content();
            embeddingStore.add(embedding, segment);
        }
        System.out.println("Indexing complete.");
    }

    private static void search(String query,
                               EmbeddingModel embeddingModel,
                               EmbeddingStore<TextSegment> embeddingStore) {
        Embedding queryEmbedding = embeddingModel.embed(query).content();

        List<EmbeddingMatch<TextSegment>> matches = embeddingStore.search(
                EmbeddingSearchRequest.builder()
                        .queryEmbedding(queryEmbedding)
                        .maxResults(MAX_RESULTS)
                        .minScore(MIN_SCORE)
                        .build()).matches();

        if (matches.isEmpty()) {
            System.out.println("No relevant employees found. Try rephrasing the query.\n");
            return;
        }

        printResults(matches);
    }

    private static void printResults(List<EmbeddingMatch<TextSegment>> matches) {
        String format = "%-6s %-16s %-18s %-28s %-55s %s%n";
        System.out.println();
        System.out.printf(format, "ID", "Name", "Department", "Designation", "Skills", "Score");
        System.out.println("-".repeat(140));
        for (EmbeddingMatch<TextSegment> match : matches) {
            var metadata = match.embedded().metadata();
            System.out.printf(format,
                    metadata.getString("id"),
                    metadata.getString("name"),
                    metadata.getString("department"),
                    metadata.getString("designation"),
                    truncate(metadata.getString("skills"), 53),
                    String.format("%.3f", match.score()));
        }
        System.out.println();
        String names = matches.stream()
                .map(m -> m.embedded().metadata().getString("name"))
                .collect(Collectors.joining(", "));
        System.out.println("Best matches: " + names + "\n");
    }

    private static String truncate(String text, int max) {
        return text.length() <= max ? text : text.substring(0, max - 3) + "...";
    }
}
