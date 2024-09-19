package com.registration.registration.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.registration.registration.model.LoggingHistory;
import com.registration.registration.repository.LoggingHistoryRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Map;

@Service
public class LoggingHistoryService {

    @Autowired
    private LoggingHistoryRepository loggingHistoryRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String IP_API_URL = "https://ipapi.co/{ip}/json/";

    public void logFailedAttempt(String ip_adress, String email) {
        String ip = getUserIp(ip_adress);
        String city = getCityFromIp(ip);

        LocalDate today = LocalDate.now();
        Optional<LoggingHistory> loggingHistoryOpt = loggingHistoryRepository.findByIpAndAttemptDate(ip, today);

        LoggingHistory loggingHistory;
        if (loggingHistoryOpt.isPresent()) {
            loggingHistory = loggingHistoryOpt.get();
            loggingHistory.setAttempts(loggingHistory.getAttempts() + 1);
        } else {
            loggingHistory = new LoggingHistory();
            loggingHistory.setIp(ip);
            loggingHistory.setCity(city);
            loggingHistory.setAttemptDate(today);
            loggingHistory.setAttempts(1);
            loggingHistory.setEmail(email);
        }

        loggingHistoryRepository.save(loggingHistory);
    }

    public String getUserIp(String ip_adress) {
        return ip_adress;
    }

    public String getCityFromIp(String ip) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(IP_API_URL, Map.class, ip);
            return response != null ? (String) response.get("city") : "Unknown";
        } catch (Exception e) {
            System.err.println("Error fetching city from IP: " + e.getMessage());
            return "Unknown";
        }
    }
}
