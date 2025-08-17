package com.learning.booking_service.dto;

import com.learning.entityService.models.BookingStatus;
import com.learning.entityService.models.Driver;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UpdateBookingReqDto {
    private Long bookingId;
    private BookingStatus status;
    private Long driverId;
    private String cancelReason;
}
