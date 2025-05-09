package br.com.javaversiontest.repository;

import br.com.javaversiontest.domain.SleepLog;
import br.com.javaversiontest.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SleepLogRepository extends JpaRepository<SleepLog, Long> {

    Optional<SleepLog> findTopByUserOrderBySleepDateDesc(User user);

    @Query("SELECT s FROM SleepLog s WHERE s.user = :user AND s.sleepDate >= :startDate")
    List<SleepLog> findAllByUserAndDateAfter(
            @Param("user") User user,
            @Param("startDate") LocalDate startDate
    );

    Optional<SleepLog> findAllById(Long id);
}
