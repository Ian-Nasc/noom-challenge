package br.com.javaversiontest.controller;

import br.com.javaversiontest.domain.SleepLog;
import br.com.javaversiontest.domain.requests.CreateSleepLogRequest;
import br.com.javaversiontest.domain.responses.SleepLogAveragesResponse;
import br.com.javaversiontest.domain.responses.SleepLogResponse;
import br.com.javaversiontest.service.SleepLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sleep-logs")
public class SleepLogController {

    @Autowired
    private SleepLogService sleepLogService;

    @PostMapping
    public ResponseEntity<SleepLogResponse> createSleepLog(@RequestBody CreateSleepLogRequest request) {
        SleepLog log = sleepLogService.createSleepLog(
                request.getUserId(),
                request.getTimeInBedStart(),
                request.getTimeInBedEnd(),
                request.getSleepQuality()
        );
        return ResponseEntity.ok(new SleepLogResponse(log));
    }

    @GetMapping("/last/{userId}")
    public ResponseEntity<SleepLogResponse> getLastSleepLog(@PathVariable Long userId) {
        return sleepLogService.getLastSleepLog(userId)
                .map(SleepLogResponse::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/average/{userId}")
    public ResponseEntity<SleepLogAveragesResponse> get30DayAverages(@PathVariable Long userId) {
        return ResponseEntity.ok(sleepLogService.getLastThirtyDayAverages(userId));
    }
}
