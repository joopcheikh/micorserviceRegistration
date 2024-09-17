package com.registration.registration.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "logging_history")
public class LoggingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ip;

    @Column(nullable = true)
    private String city;

    @Column(nullable = false)
    private LocalDate attemptDate;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private int attempts;
}
