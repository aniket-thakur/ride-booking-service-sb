package com.learning.booking_service.dto;

import com.learning.entityService.models.Booking;
import com.learning.entityService.models.BookingStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateBookingResDto {
    private Long bookingId;
    private BookingStatus bookingStatus;
    private DriverDto driver;
    private String cancelReason;
}
