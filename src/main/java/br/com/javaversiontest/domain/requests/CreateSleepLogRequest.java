package br.com.javaversiontest.domain.requests;

import br.com.javaversiontest.domain.enums.SleepQuality;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateSleepLogRequest {
    private Long userId;
    private LocalDateTime timeInBedStart;
    private LocalDateTime timeInBedEnd;
    private SleepQuality sleepQuality;

}
