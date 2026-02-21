package com.example.demo.model;
import lombok.Data;

@Data
public class Period {
    private String start; // yyyy-MM-dd HH:mm:ss
    private String end;   // yyyy-MM-dd HH:mm:ss
    private Double fixed; // Used for q rules
    private Double extra; // Used for p rules
}