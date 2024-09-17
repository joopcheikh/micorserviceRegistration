package com.registration.registration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.registration.registration.model.LoggingHistory;

import java.time.LocalDate;
import java.util.Optional;

public interface LoggingHistoryRepository extends JpaRepository<LoggingHistory, Long> {
    Optional<LoggingHistory> findByIpAndAttemptDate(String ip, LocalDate attemptDate);
}
