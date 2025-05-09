package br.com.javaversiontest.domain;

import br.com.javaversiontest.domain.enums.SleepQuality;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "sleep_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SleepLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate sleepDate;

    private LocalTime timeInBedStart;
    private LocalTime timeInBedEnd;

    private Long totalTimeInBed;

    @Enumerated(EnumType.STRING)
    private SleepQuality sleepQuality;

    @ManyToOne
    private User user;


    public SleepLog(User user, LocalDate sleepDate, LocalDateTime start, LocalDateTime end, long minutes, SleepQuality quality) {
        this.user = user;
        this.sleepDate = sleepDate;
        this.timeInBedStart = start.toLocalTime();
        this.timeInBedEnd = end.toLocalTime();
        this.totalTimeInBed = minutes;
        this.sleepQuality = quality;
    }
}
