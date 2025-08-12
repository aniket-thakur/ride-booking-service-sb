package com.learning.booking_service.dto;

import com.learning.entityService.models.Location;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingDto {
    private Long passengerId;

    private Location pickupLocation;

    private Location dropLocation;

}
