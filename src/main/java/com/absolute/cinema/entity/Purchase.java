package com.absolute.cinema.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "purchases")
public class Purchase {

    public enum Status { PENDING, PAID, FAILED, CANCELLED }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private User client;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL)
    private List<Ticket> tickets = new ArrayList<>();

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();

    @Column(name = "total_cents", nullable = false)
    private Integer totalCents;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;
}
