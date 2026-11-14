package com.example.employeesearch;

public record Employee(
        String id,
        String name,
        String department,
        String designation,
        String skills) {

    /**
     * Natural-language representation used to compute the embedding. Richer,
     * sentence-style text produces better semantic matches than raw field dumps.
     */
    public String toSearchableText() {
        return "%s works in the %s department as a %s. Skills: %s. Employee ID: %s."
                .formatted(name, department, designation, skills, id);
    }
}
