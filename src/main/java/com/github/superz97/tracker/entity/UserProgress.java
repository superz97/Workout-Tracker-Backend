package com.github.superz97.tracker.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_progress",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "exercise_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
