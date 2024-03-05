package com.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "cleaning_type", nullable = false)
    private String cleaningType;

 /*   @Column(name = "service_type", nullable = false)
    private String serviceType;*/

    @Column(name = "cleaning_date")
    @Temporal(TemporalType.DATE)
    private Date cleaningDate;

    @Column(name = "hour")
    private Double hour;

    @Column(name = "total_price")
    private Double totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cleaner_id", nullable = false)
    private User cleaner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booked_by", nullable = false)
    private User bookedBy;

    @Column(name = "booked_at")
    private Timestamp bookedAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "status")
    private String status;
}
