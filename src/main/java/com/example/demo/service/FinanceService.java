package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.model.Expense;
import com.example.demo.model.FilterRequest;
import com.example.demo.model.Period;
import com.example.demo.model.Transaction;
@Service
public class FinanceService {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Transaction parseAndRound(Expense expense) {
        Transaction t = new Transaction();
        t.setDate(expense.getDate());
        t.setAmount(expense.getAmount());
        double ceiling = Math.ceil(expense.getAmount() / 100.0) * 100.0;
        if (ceiling == expense.getAmount()) ceiling += 100.0;
        t.setCeiling(ceiling);
        t.setRemanent(ceiling - expense.getAmount());
        return t;
    }

    // Fix for the 404/Empty response: This now handles the K periods
    public List<Map<String, Object>> filterTransactions(FilterRequest request) {
        List<Map<String, Object>> finalResults = new ArrayList<>();
        
        // Ensure k is not null to avoid empty results
        List<Period> kPeriods = request.getK() != null ? request.getK() : new ArrayList<>();

        for (Period kPeriod : kPeriods) {
            double totalKRemanent = 0;
            List<Transaction> kTransactions = new ArrayList<>();

            for (Expense exp : request.getTransactions()) {
                LocalDateTime txDate = LocalDateTime.parse(exp.getDate(), formatter);
                if (isWithin(txDate, kPeriod)) {
                    Transaction t = processRules(exp, request);
                    totalKRemanent += t.getRemanent();
                    kTransactions.add(t);
                }
            }
            Map<String, Object> kResult = new LinkedHashMap<>();
            kResult.put("period", kPeriod);
            kResult.put("total_remanent", totalKRemanent);
            kResult.put("transactions", kTransactions);
            finalResults.add(kResult);
        }
        return finalResults;
    }

    private Transaction processRules(Expense exp, FilterRequest request) {
        Transaction t = parseAndRound(exp);
        LocalDateTime txDate = LocalDateTime.parse(exp.getDate(), formatter);

        // Q Rules
        request.getQ().stream()
            .filter(p -> isWithin(txDate, p))
            .max(Comparator.comparing(p -> LocalDateTime.parse(p.getStart(), formatter)))
            .ifPresent(q -> t.setRemanent(q.getFixed()));

        // P Rules
        double extra = request.getP().stream()
            .filter(p -> isWithin(txDate, p))
            .mapToDouble(Period::getExtra)
            .sum();
        
        t.setRemanent(t.getRemanent() + extra);
        return t;
    }

    // Fix for "cannot find symbol" errors in Controller
    public double calculateRealValue(double p, double rate, int age, double inflation) {
        int t = 60 - age;
        return (p * Math.pow(1 + rate, t)) / Math.pow(1 + inflation, t);
    }

    public double calculateTaxBenefit(double income, double investedAmount) {
        double deduction = Math.min(investedAmount, Math.min(0.10 * income, 200000.0));
        return calculateTax(income) - calculateTax(income - deduction);
    }

    private double calculateTax(double income) {
        if (income <= 700000) return 0;
        if (income <= 1000000) return (income - 700000) * 0.10;
        return 30000 + (income - 1000000) * 0.15; // Simplified for the hackathon
    }

    private boolean isWithin(LocalDateTime date, Period period) {
        LocalDateTime start = LocalDateTime.parse(period.getStart(), formatter);
        LocalDateTime end = LocalDateTime.parse(period.getEnd(), formatter);
        return !date.isBefore(start) && !date.isAfter(end);
    }
}