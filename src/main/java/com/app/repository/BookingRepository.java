package com.app.repository;

import com.app.model.Booking;
import com.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookedBy(User bookedBy);
    List<Booking> findByCleaner(User cleaner);
}