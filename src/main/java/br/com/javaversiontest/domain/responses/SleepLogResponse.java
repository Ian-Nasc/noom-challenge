package br.com.javaversiontest.domain.responses;

import br.com.javaversiontest.domain.SleepLog;
import br.com.javaversiontest.domain.enums.SleepQuality;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Data
@AllArgsConstructor
public class SleepLogResponse {
    private LocalDate sleepDate;
    private LocalTime timeInBedStart;
    private LocalTime timeInBedEnd;
    private long totalTimeInBedMinutes;
    private SleepQuality sleepQuality;

    public SleepLogResponse(SleepLog log) {
        this.sleepDate = Optional.ofNullable(log)
                .map(SleepLog::getSleepDate)
                .orElse(null);

        this.timeInBedStart = Optional.ofNullable(log)
                .map(SleepLog::getTimeInBedStart)
                .orElse(null);

        this.timeInBedEnd = Optional.ofNullable(log)
                .map(SleepLog::getTimeInBedEnd)
                .orElse(null);

        this.totalTimeInBedMinutes = Optional.ofNullable(log)
                .map(SleepLog::getTotalTimeInBed)
                .orElse(0L);

        this.sleepQuality = Optional.ofNullable(log)
                .map(SleepLog::getSleepQuality)
                .orElse(null);
    }


}
