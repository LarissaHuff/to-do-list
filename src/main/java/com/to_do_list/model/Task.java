package com.to_do_list.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Table
@Entity
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String description;

    private LocalDate done;

    private LocalDate created;

    private LocalDate started;

    @Enumerated(EnumType.STRING)
    private TaskStatusEnum status;

    @Enumerated(EnumType.STRING)
    private Priority priority;
}
