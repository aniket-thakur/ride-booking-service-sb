package com.learning.booking_service.services;

import com.learning.booking_service.dto.CreateBookingDto;
import com.learning.booking_service.dto.CreateBookingResponseDto;
import com.learning.booking_service.dto.UpdateBookingReqDto;
import com.learning.booking_service.dto.UpdateBookingResDto;
import com.learning.entityService.models.Booking;

public interface BookingService {
    public CreateBookingResponseDto createBooking(CreateBookingDto booking);

    public UpdateBookingResDto updateBookingStatus(UpdateBookingReqDto UpdatedBooking);
}
