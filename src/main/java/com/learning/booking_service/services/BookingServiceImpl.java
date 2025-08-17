package com.learning.booking_service.services;

import com.learning.booking_service.client.LocationServiceApi;
import com.learning.booking_service.dto.*;
import com.learning.booking_service.repositories.BookingRepository;
import com.learning.booking_service.repositories.DriverRepository;
import com.learning.booking_service.repositories.PassengerRepository;
import com.learning.entityService.models.Booking;
import com.learning.entityService.models.BookingStatus;
import com.learning.entityService.models.Driver;
import com.learning.entityService.models.Passenger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {
    private final PassengerRepository passengerRepository;
    private final BookingRepository bookingRepository;
    private final RestTemplate restTemplate;
    private final LocationServiceApi locationServiceApi;
    private final DriverRepository driverRepository;
    private static String LOCATION_SERVICE_ENDPOINT = "http://localhost:7477/api/v1/location/";

    public BookingServiceImpl(PassengerRepository passengerRepository,
                              BookingRepository bookingRepository,
                              RestTemplate restTemplate,
                              LocationServiceApi locationServiceApi,
                              DriverRepository driverRepository){
        this.passengerRepository = passengerRepository;
        this.bookingRepository = bookingRepository;
        this.restTemplate = restTemplate;
        this.locationServiceApi = locationServiceApi;
        this.driverRepository = driverRepository;
    }

    public CreateBookingResponseDto createBooking(CreateBookingDto bookingdetails){

        Optional<Passenger> passenger = passengerRepository.findById(bookingdetails.getPassengerId());
        Booking booking = new Booking().builder()
                        .bookingStatus(BookingStatus.ASSIGNING_DRIVER)
                .pickupPoint(bookingdetails.getPickupLocation())
                .dropPoint(bookingdetails.getDropLocation())
                .passenger(passenger.get())
                .build();
        Booking newBooking =  bookingRepository.save(booking);

        // make an api call to location service to fetch the nearby drivers

        // create the payload
        NearByDriverLocationDto request = NearByDriverLocationDto.builder()
                .latitude(bookingdetails.getPickupLocation().getLatitude())
                .longitude(bookingdetails.getPickupLocation().getLongitude())
                .build();

        // Expecting a JSON array in the result, so we map it to a Java array of DTOs
//        ResponseEntity<NearByDriverLocResponseDto[]> result = restTemplate.postForEntity(LOCATION_SERVICE_ENDPOINT + "/nearby/drivers", request, NearByDriverLocResponseDto[].class);
//        if(result.getStatusCode().is2xxSuccessful() && result.hasBody()){
//            assert result.getBody() != null;
//            for(NearByDriverLocResponseDto res : result.getBody()){
//                System.out.printf("Driver ID: "+ res.getDriverId() + "Longitude: "+res.getLongitude()
//                        +"Latitude: "+ res.getLatitude());
//            }
//        }


        nearByDriverAsync(request);

        return CreateBookingResponseDto.builder()
                .bookingId(newBooking.getId())
                .bookingStatus(newBooking.getBookingStatus())
                .pickupPoint(newBooking.getPickupPoint())
                .dropPoint((newBooking.getDropPoint()))
//                .drivers(Optional.of(newBooking.getDriver()))
                .build();
    }

    @Override
    @Transactional
    public UpdateBookingResDto updateBookingStatus(UpdateBookingReqDto updatedBooking) {
        if(updatedBooking.getStatus() == BookingStatus.CANCELLED){
            return UpdateBookingResDto.builder()
                    .bookingId(updatedBooking.getBookingId())
                    .bookingStatus(updatedBooking.getStatus())
                    .cancelReason(updatedBooking.getCancelReason())
                    .build();
        }
        Driver driver = driverRepository.findById(updatedBooking.getDriverId())
                .orElseThrow(()-> new RuntimeException("Driver not found"));

        bookingRepository.updateBookingStatusAndDriver(
                updatedBooking.getStatus(),
                driver.getId(),
                updatedBooking.getBookingId()
                );

        DriverDto driverDetails= DriverDto.builder()
                .id(driver.getId())
                .name(driver.getName())
                .license_number(driver.getLicenseNumber())
                .age(driver.getAge())
                .mobile_number(driver.getMobileNumber())
                .rating(driver.getRating())
                .build();


        return UpdateBookingResDto.builder()
                .bookingId(updatedBooking.getBookingId())
                .bookingStatus(updatedBooking.getStatus())
                .driver(driverDetails)
                .build();
    }

    // retrofit async
    private void nearByDriverAsync(NearByDriverLocationDto request){
        Call<NearByDriverLocResponseDto[]> call = locationServiceApi.fetchNearbyUser(request);
        call.enqueue(new Callback<NearByDriverLocResponseDto[]>() {
            @Override
            public void onResponse(Call<NearByDriverLocResponseDto[]> call, Response<NearByDriverLocResponseDto[]> response) {
                if(response.isSuccessful() && response.body() != null){
                    for(NearByDriverLocResponseDto res : response.body()){
                        System.out.println("Driver ID: "+ res.getDriverId() + " || Longitude: "+res.getLongitude()
                                +" || Latitude: "+ res.getLatitude());
                    }
                }
                else{
                    System.out.println("No driver found!! "+ Arrays.toString(response.body()) + " || "+response.message());
                }
            }

            @Override
            public void onFailure(Call<NearByDriverLocResponseDto[]> call, Throwable t) {
                System.out.println("Error occurred fetching nearby drivers: "+ t.getMessage());
            }
        });

    }
}
