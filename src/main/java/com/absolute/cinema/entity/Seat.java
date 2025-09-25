package com.absolute.cinema.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "seats",
        uniqueConstraints = @UniqueConstraint(columnNames = {"hall_id", "row_number", "seat_number"}))
public class Seat {

    public enum Status { AVAILABLE, RESERVED, SOLD, CANCELLED }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id")
    private Hall hall;

    @Column(name = "row_number", nullable = false)
    private Integer row;

    @Column(name = "seat_number", nullable = false)
    private Integer number;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private SeatCategory category;

    /*
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    ???
    */
}
