package com.example.demo.model;
import lombok.Data;

@Data
public class Transaction {
    private String date;
    private double amount;
    private double ceiling;
    private double remanent;
}