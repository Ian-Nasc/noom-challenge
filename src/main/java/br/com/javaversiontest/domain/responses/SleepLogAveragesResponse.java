package br.com.javaversiontest.domain.responses;


import br.com.javaversiontest.domain.SleepLog;
import br.com.javaversiontest.domain.enums.SleepQuality;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class SleepLogAveragesResponse {
    private LocalDate startDate;
    private LocalDate endDate;
    private long averageTotalTimeInBedMinutes;
    private LocalTime averageTimeInBedStart;
    private LocalTime averageTimeInBedEnd;
    private Map<SleepQuality, Long> qualityFrequencies;

    public SleepLogAveragesResponse(List<SleepLog> logs) {
        this.startDate = logs.stream().map(SleepLog::getSleepDate).min(LocalDate::compareTo).orElse(null);
        this.endDate = logs.stream().map(SleepLog::getSleepDate).max(LocalDate::compareTo).orElse(null);

        this.averageTotalTimeInBedMinutes = (long) logs.stream()
                .mapToLong(SleepLog::getTotalTimeInBed)
                .average()
                .orElse(0);

        this.averageTimeInBedStart = averageTime(logs.stream().map(SleepLog::getTimeInBedStart).collect(Collectors.toList()));
        this.averageTimeInBedEnd = averageTime(logs.stream().map(SleepLog::getTimeInBedEnd).collect(Collectors.toList()));

        this.qualityFrequencies = logs.stream()
                .collect(Collectors.groupingBy(SleepLog::getSleepQuality, Collectors.counting()));
    }

    private LocalTime averageTime(List<LocalTime> times) {
        if (times.isEmpty()) return null;
        long totalSeconds = times.stream()
                .mapToLong(t -> t.toSecondOfDay())
                .sum();
        return LocalTime.ofSecondOfDay(totalSeconds / times.size());
    }

}

