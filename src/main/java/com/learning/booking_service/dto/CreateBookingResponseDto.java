package com.learning.booking_service.dto;

import com.learning.entityService.models.BookingStatus;
import com.learning.entityService.models.Driver;
import com.learning.entityService.models.Location;
import lombok.*;
import org.springframework.data.geo.Distance;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBookingResponseDto {

    private Long bookingId;

    private BookingStatus bookingStatus;

    private Location pickupPoint;

    private Location dropPoint;

//    private Optional<Driver> drivers;

}
