package com.learning.booking_service.repositories;

import com.learning.entityService.models.Booking;
import com.learning.entityService.models.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Modifying
    @Query("UPDATE Booking b SET b.bookingStatus = :status, b.driver.id = :driverId WHERE b.id = :bookingId")
    void updateBookingStatusAndDriver(@Param("status") BookingStatus status,
                                      @Param("driverId") Long driverId,
                                      @Param("bookingId") Long bookingId);
}


