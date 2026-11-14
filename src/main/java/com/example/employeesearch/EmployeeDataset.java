package com.example.employeesearch;

import java.util.List;

public final class EmployeeDataset {

    private EmployeeDataset() {
    }

    public static List<Employee> sampleEmployees() {
        return List.of(
                new Employee("E101", "Alice Johnson", "Engineering", "Senior Software Engineer",
                        "Java, Spring Boot, Microservices, Docker, Kubernetes"),
                new Employee("E102", "Rahul Sharma", "Engineering", "Backend Developer",
                        "Python, Django, REST APIs, PostgreSQL, Redis"),
                new Employee("E103", "Maria Garcia", "Data Science", "Machine Learning Engineer",
                        "Python, TensorFlow, PyTorch, NLP, model deployment"),
                new Employee("E104", "David Kim", "Data Science", "Data Analyst",
                        "SQL, Excel, Tableau, Power BI, statistics, reporting"),
                new Employee("E105", "Sneha Patel", "Human Resources", "HR Manager",
                        "recruitment, employee relations, payroll, onboarding, HR policies"),
                new Employee("E106", "James Miller", "Finance", "Financial Analyst",
                        "accounting, budgeting, forecasting, Excel, SAP, auditing"),
                new Employee("E107", "Priya Nair", "Engineering", "DevOps Engineer",
                        "CI/CD, Jenkins, AWS, Terraform, Linux, monitoring"),
                new Employee("E108", "Tom Wilson", "Marketing", "Digital Marketing Specialist",
                        "SEO, content marketing, Google Ads, social media, analytics"),
                new Employee("E109", "Ananya Rao", "Engineering", "Frontend Developer",
                        "React, TypeScript, HTML, CSS, UX design, accessibility"),
                new Employee("E110", "Chen Wei", "Quality Assurance", "QA Lead",
                        "test automation, Selenium, JUnit, performance testing, bug tracking"),
                new Employee("E111", "Fatima Khan", "Customer Support", "Support Engineer",
                        "troubleshooting, ticketing systems, communication, product knowledge"),
                new Employee("E112", "Arjun Mehta", "Engineering", "Security Engineer",
                        "penetration testing, network security, OWASP, incident response, SIEM"));
    }
}
