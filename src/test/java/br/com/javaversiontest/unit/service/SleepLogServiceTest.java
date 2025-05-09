package br.com.javaversiontest.unit.service;

import br.com.javaversiontest.domain.SleepLog;
import br.com.javaversiontest.domain.User;
import br.com.javaversiontest.domain.enums.SleepQuality;
import br.com.javaversiontest.domain.responses.SleepLogAveragesResponse;
import br.com.javaversiontest.exception.UserNotFoundException;
import br.com.javaversiontest.repository.SleepLogRepository;
import br.com.javaversiontest.repository.UserRepository;
import br.com.javaversiontest.service.SleepLogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SleepLogServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SleepLogRepository sleepLogRepository;

    @InjectMocks
    private SleepLogService sleepLogService;

    private final User user = new User(1L, "Test User");

    @Test
    void createSleepLog_shouldSaveLog() {
        LocalDateTime start = LocalDateTime.of(2025, 5, 17, 22, 0);
        LocalDateTime end = LocalDateTime.of(2025, 5, 18, 6, 0);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(sleepLogRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        SleepLog log = sleepLogService.createSleepLog(1L, start, end, SleepQuality.GOOD);

        assertEquals(LocalDate.of(2025, 5, 17), log.getSleepDate());
        assertEquals(480L, log.getTotalTimeInBed());
        assertEquals(SleepQuality.GOOD, log.getSleepQuality());
    }

    @Test
    void createSleepLog_shouldThrowIfUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                sleepLogService.createSleepLog(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1), SleepQuality.OK)
        );
    }

    @Test
    void getLastSleepLog_shouldReturnLastLog() {
        SleepLog log = new SleepLog();
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(sleepLogRepository.findTopByUserOrderBySleepDateDesc(user)).thenReturn(Optional.of(log));

        Optional<SleepLog> result = sleepLogService.getLastSleepLog(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void getLastThirtyDayAverages_shouldReturnAverages() {
        List<SleepLog> logs = List.of(
                new SleepLog(user, LocalDate.now().minusDays(1), LocalDateTime.now().minusHours(8), LocalDateTime.now(), 480, SleepQuality.GOOD)
        );
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(sleepLogRepository.findAllByUserAndDateAfter(eq(user), any())).thenReturn(logs);

        SleepLogAveragesResponse response = sleepLogService.getLastThirtyDayAverages(1L);
        assertEquals(480, response.getAverageTotalTimeInBedMinutes());
        assertEquals(1, response.getQualityFrequencies().get(SleepQuality.GOOD));
    }
}
