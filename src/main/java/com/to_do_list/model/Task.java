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
    UUID id;

    String name;

    String description;

    LocalDate done;

    LocalDate created;

    @Enumerated(EnumType.STRING)
    Priority priority;
}
