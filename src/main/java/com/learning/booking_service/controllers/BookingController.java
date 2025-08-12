package com.learning.booking_service.controllers;

import com.learning.booking_service.dto.CreateBookingDto;
import com.learning.booking_service.dto.CreateBookingResponseDto;
import com.learning.booking_service.services.BookingServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/")
public class BookingController {

    private BookingServiceImpl bookingService;

    public BookingController(BookingServiceImpl bookingServiceimpl){
        this.bookingService = bookingServiceimpl;
    }

    @PostMapping("/create-booking")
    public ResponseEntity<CreateBookingResponseDto> createBooking(
            @RequestBody CreateBookingDto createBookingDto){
        CreateBookingResponseDto res =  bookingService.createBooking(createBookingDto);

        return new ResponseEntity<>(res, HttpStatus.CREATED);

    }
}
