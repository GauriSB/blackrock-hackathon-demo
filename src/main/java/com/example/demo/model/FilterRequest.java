package com.example.demo.model;

import java.util.List;

import lombok.Data;

@Data
public class FilterRequest {
    private List<Period> q; // Fixed amounts
    private List<Period> p; // Extra additions
    private List<Period> k; // Evaluation ranges
    private double wage;
    private List<Expense> transactions;
}