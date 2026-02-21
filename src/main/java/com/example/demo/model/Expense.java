package com.example.demo.model;
import lombok.Data;

@Data
public class Expense {
    private String date; // Format: YYYY-MM-DD HH:mm:ss
    private double amount;
}