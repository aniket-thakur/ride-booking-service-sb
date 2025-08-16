package com.learning.booking_service.services;

import com.learning.booking_service.client.LocationServiceApi;
import com.learning.booking_service.dto.CreateBookingDto;
import com.learning.booking_service.dto.CreateBookingResponseDto;
import com.learning.booking_service.dto.NearByDriverLocResponseDto;
import com.learning.booking_service.dto.NearByDriverLocationDto;
import com.learning.booking_service.repositories.BookingRepository;
import com.learning.booking_service.repositories.PassengerRepository;
import com.learning.entityService.models.Booking;
import com.learning.entityService.models.BookingStatus;
import com.learning.entityService.models.Passenger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import java.util.Arrays;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {
    private final PassengerRepository passengerRepository;
    private final BookingRepository bookingRepository;
    private final RestTemplate restTemplate;
    private final LocationServiceApi locationServiceApi;
    private static String LOCATION_SERVICE_ENDPOINT = "http://localhost:7477/api/v1/location/";

    public BookingServiceImpl(PassengerRepository passengerRepository, BookingRepository bookingRepository,
                              RestTemplate restTemplate, LocationServiceApi locationServiceApi){
        this.passengerRepository = passengerRepository;
        this.bookingRepository = bookingRepository;
        this.restTemplate = restTemplate;
        this.locationServiceApi = locationServiceApi;
    }

    public CreateBookingResponseDto createBooking(CreateBookingDto bookingdetails){
        System.out.printf("asjaj");
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
        NearByDriverLocationDto request = new NearByDriverLocationDto().builder()
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

    // retrofit async
    private void nearByDriverAsync(NearByDriverLocationDto request){
        Call<NearByDriverLocResponseDto[]> call = locationServiceApi.fetchNearbyUser(request);
        call.enqueue(new Callback<NearByDriverLocResponseDto[]>() {
            @Override
            public void onResponse(Call<NearByDriverLocResponseDto[]> call, Response<NearByDriverLocResponseDto[]> response) {
                if(response.isSuccessful() && response.body() != null){
                    for(NearByDriverLocResponseDto res : response.body()){
                        System.out.printf("Driver ID: "+ res.getDriverId() + "Longitude: "+res.getLongitude()
                                +"Latitude: "+ res.getLatitude());
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
