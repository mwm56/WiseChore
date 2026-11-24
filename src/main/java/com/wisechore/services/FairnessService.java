package com.wisechore.services;

import com.wisechore.models.CompletionLog;
import com.wisechore.repositories.ChoreRepository;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * SOLID note (Single Responsibility Principle):
 * FairnessService's only job is tallying completions and turning them
 * into percentages for the Fairness Summary view / pie chart.
 */
public class FairnessService {

    private final ChoreRepository repository;

    public FairnessService(ChoreRepository repository) {
        this.repository = repository;
    }

    public Map<String, Integer> completionCounts(LocalDate since) {
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (CompletionLog log : repository.getAllCompletionLogs()) {
            if (since != null && log.getCompletedOn().isBefore(since)) {
                continue;
            }
            counts.merge(log.getRoommate(), 1, Integer::sum);
        }
        return counts;
    }

    public Map<String, Double> fairnessPercentages(LocalDate since) {
        Map<String, Integer> counts = completionCounts(since);
        int total = counts.values().stream().mapToInt(Integer::intValue).sum();

        Map<String, Double> percentages = new LinkedHashMap<>();
        if (total == 0) {
            for (String roommate : counts.keySet()) {
                percentages.put(roommate, 0.0);
            }
            return percentages;
        }

        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            double percentage = Math.round((entry.getValue() * 1000.0 / total)) / 10.0;
            percentages.put(entry.getKey(), percentage);
        }
        return percentages;
    }

    public Map<String, Double> fairnessPercentages() {
        return fairnessPercentages(null);
    }
}
