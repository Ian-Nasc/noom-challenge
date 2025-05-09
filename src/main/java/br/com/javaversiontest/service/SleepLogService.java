package br.com.javaversiontest.service;

import br.com.javaversiontest.domain.SleepLog;
import br.com.javaversiontest.domain.User;
import br.com.javaversiontest.domain.enums.SleepQuality;
import br.com.javaversiontest.domain.responses.SleepLogAveragesResponse;
import br.com.javaversiontest.exception.UserNotFoundException;
import br.com.javaversiontest.repository.SleepLogRepository;
import br.com.javaversiontest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SleepLogService {


    @Autowired
    private SleepLogRepository sleepLogRepository;

    @Autowired
    private UserRepository userRepository;

    public SleepLog createSleepLog(Long userId, LocalDateTime start, LocalDateTime end, SleepQuality quality) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end times must not be null");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Duration total = Duration.between(start, end);
        if (total.isNegative()) {
            total = total.plusHours(24);
        } else if (total.isZero()) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        LocalDate sleepDate = start.toLocalDate();

        SleepLog log = new SleepLog(user, sleepDate, start, end, total.toMinutes(), quality);

        return sleepLogRepository.save(log);
    }

    public Optional<SleepLog> getLastSleepLog(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        User user = userRepository.findById(userId).orElseThrow();
        return sleepLogRepository.findTopByUserOrderBySleepDateDesc(user);
    }

    public SleepLogAveragesResponse getLastThirtyDayAverages(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        User user = userRepository.findById(userId).orElseThrow();
        LocalDate from = LocalDate.now().minusDays(30);
        List<SleepLog> logs = sleepLogRepository.findAllByUserAndDateAfter(user, from);


        return new SleepLogAveragesResponse(logs);
    }
}
