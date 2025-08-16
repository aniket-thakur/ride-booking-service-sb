package com.learning.booking_service.client;

import com.learning.booking_service.dto.NearByDriverLocResponseDto;
import com.learning.booking_service.dto.NearByDriverLocationDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LocationServiceApi {

    @POST("/api/v1/location/nearby/drivers")
    Call<NearByDriverLocResponseDto[]> fetchNearbyUser(@Body NearByDriverLocationDto request);
}
