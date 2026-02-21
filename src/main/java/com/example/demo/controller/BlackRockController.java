package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping; // Don't forget this import
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Expense;
import com.example.demo.model.FilterRequest;
import com.example.demo.model.Transaction;
import com.example.demo.service.FinanceService;

@RestController
@RequestMapping("/blackrock/challenge/v1")
public class BlackRockController {

    private final FinanceService financeService;

    public BlackRockController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @PostMapping("/transactions:parse")
    public List<Transaction> parse(@RequestBody List<Expense> expenses) {
        return expenses.stream().map(financeService::parseAndRound).toList();
    }

// Change this line:
@PostMapping("/transactions:filter")
public List<Map<String, Object>> filter(@RequestBody FilterRequest request) {
    return financeService.filterTransactions(request);
}

    // ADD THIS TO FIX 404 ON PERFORMANCE
    @GetMapping("/performance")
    public Map<String, Object> performance() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("time", LocalDateTime.now().toString());
        metrics.put("memory", (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024) + " MB");
        metrics.put("threads", Thread.activeCount());
        return metrics;
    }

    // ADD THIS TO FIX 404 ON RETURNS:CALCULATE
    @PostMapping("/returns:calculate")
    public Map<String, Object> calculateReturns(@RequestBody Map<String, Object> input) {
        double totalInvested = Double.parseDouble(input.get("totalInvested").toString());
        int age = Integer.parseInt(input.get("age").toString());
        double wage = Double.parseDouble(input.get("wage").toString());
        double inflation = 0.055;

        double npsFinal = financeService.calculateRealValue(totalInvested, 0.0711, age, inflation);
        double taxBenefit = financeService.calculateTaxBenefit(wage * 12, totalInvested);
        double indexFinal = financeService.calculateRealValue(totalInvested, 0.1449, age, inflation);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("nps_real_value", String.format("%.2f", npsFinal));
        response.put("nps_tax_benefit", String.format("%.2f", taxBenefit));
        response.put("index_fund_real_value", String.format("%.2f", indexFinal));
        
        return response;
    }
}