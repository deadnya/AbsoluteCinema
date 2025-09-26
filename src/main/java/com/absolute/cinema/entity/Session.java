package com.absolute.cinema.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "film_id")
    private Film film;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id")
    private Hall hall;

    @Column(name = "start_at", nullable = false)
    private OffsetDateTime startAt;

    @Column(name = "slot_start_at", nullable = false)
    private OffsetDateTime slotStartAt;

    @Column(name = "slot_end_at", nullable = false)
    private OffsetDateTime slotEndAt;

    @PrePersist @PreUpdate
    private void computeTimeslot() {
        if (startAt != null && film != null && film.getDurationMinutes() != null) {
            var before = 20L; var after = 20L;
            slotStartAt = startAt.minusMinutes(before);
            slotEndAt = startAt.plusMinutes(film.getDurationMinutes() + after);
        }
    }
}
