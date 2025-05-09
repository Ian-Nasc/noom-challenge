package br.com.javaversiontest.integration.service;

import br.com.javaversiontest.domain.SleepLog;
import br.com.javaversiontest.domain.User;
import br.com.javaversiontest.domain.enums.SleepQuality;
import br.com.javaversiontest.repository.SleepLogRepository;
import br.com.javaversiontest.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class SleepLogIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SleepLogRepository sleepLogRepository;

    @BeforeEach
    void setup() {
        User user = new User();
        user.setName("John Doe");
        userRepository.save(user);
    }

    @Test
    void testCreateSleepLog() throws Exception {
        User user = userRepository.findAll().get(0);
        String json = """
        {
            "userId": %d,
            "timeInBedStart": "2025-05-05T22:00:00",
            "timeInBedEnd": "2025-05-06T06:00:00",
            "sleepQuality": "GOOD"
        }
        """.formatted(user.getId());

        mockMvc.perform(post("/api/sleep-logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalTimeInBedMinutes").value(480));
    }

    @Test
    void testGetLastSleepLog() throws Exception {
        User user = userRepository.findAll().get(0);
        SleepLog log = new SleepLog(user, LocalDate.now(), LocalDateTime.now().minusHours(8), LocalDateTime.now(), 480, SleepQuality.GOOD);
        sleepLogRepository.save(log);
        Optional<SleepLog> logs = sleepLogRepository.findAllById(user.getId());
        logs.ifPresent(l -> System.out.println("Log: " + l.getSleepQuality()));

        mockMvc.perform(get("/api/sleep-logs/last/" + user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sleepQuality").value(SleepQuality.GOOD.name()));
    }

    @Test
    void testGet30DayAverages() throws Exception {
        User user = userRepository.findAll().get(0);
        sleepLogRepository.deleteAll();
        SleepLog log = new SleepLog(user, LocalDate.now(), LocalDateTime.now(), LocalDateTime.now().plusHours(8), 480, SleepQuality.BAD);
        sleepLogRepository.save(log);

        mockMvc.perform(get("/api/sleep-logs/average/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageTotalTimeInBedMinutes").value(480));
    }
}

