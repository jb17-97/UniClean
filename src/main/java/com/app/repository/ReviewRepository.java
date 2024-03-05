package com.app.repository;

import com.app.model.Booking;
import com.app.model.Review;
import com.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
//    List<Booking> findByBookedBy(User bookedBy);
//    List<Booking> findByCleaner(User cleaner);
    List<Review> findByBookingId(Long bookingId);
}